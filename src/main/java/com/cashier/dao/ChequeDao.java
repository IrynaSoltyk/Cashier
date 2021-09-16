package com.cashier.dao;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.ChequeProduct;
import com.cashier.models.RequestEntity;
import com.cashier.models.Units;
import com.cashier.models.User;
import com.cashier.service.ConnectionPool;
import com.cashier.utils.ProductsLock;

public class ChequeDao {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	private ChequeDao() {
	}

	public static Cheque create(Cheque cheque) throws UnsuccessfulRequestException {
		String sql = "INSERT INTO CHEQUES (CREATED_BY, SHIFT_ID) VALUES (?,?)";
		ConnectionPool cp = ConnectionPool.getInstance();
		int createdId = cheque.getCreatedBy().getUserId();
		int shiftId = cheque.getShiftId();
		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			int k = 0;
			st.setInt(++k, createdId);
			st.setInt(++k, shiftId);

			st.executeUpdate();
			try (ResultSet rs = st.getGeneratedKeys()) {
				if (!rs.next()) {
					return null;
				}
				int id = rs.getInt(1);

				cheque.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Failed create cheque", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'insert' request", e);
		}
		return cheque;

	}

	public static Cheque get(int checkId) throws UnsuccessfulRequestException {
		String sql = "SELECT c.ID, c.SHIFT_ID, c.CREATED_DATE, c.CANCELLED_DATE,"
				+ " ucreated.ID AS created_id, ucreated.NAME AS created_name," + " cp.AMOUNT, cp.PRICE, cp.ID as CP_ID,"
				+ " p.NAME, p.UNITS, p.ID as P_ID,"
				+ " ucancelled.ID AS cancelled_id, ucancelled.NAME AS cancelled_name" + " FROM CHEQUES AS c"
				+ " INNER JOIN USERS AS ucreated ON c.CREATED_BY = ucreated.ID"
				+ " LEFT JOIN CHEQUES_PRODUCTS AS cp ON c.ID = cp.CHEQUE_ID"
				+ " LEFT JOIN PRODUCTS AS p ON cp.PRODUCT_ID = p.ID "
				+ " LEFT JOIN USERS as ucancelled ON c.CANCELLED_BY = ucancelled.ID" + " WHERE c.ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		Cheque result = new Cheque();
		try (Connection con = cp.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

			st.setInt(1, checkId);

			try (ResultSet rs = st.executeQuery()) {
				int i = 0;
				Integer id = null;
				List<ChequeProduct> products = new ArrayList<>();
				while (rs.next()) {
					if (i++ == 0) {
						Instant date = null;
						Instant cancelledDate = null;
						id = rs.getInt("ID");
						int shiftId = rs.getInt("SHIFT_ID");
						if (rs.getTimestamp("CREATED_DATE") != null) {
							date = rs.getTimestamp("CREATED_DATE").toInstant();
						}
						Integer createdId = rs.getInt("CREATED_ID");
						String createdName = rs.getString("CREATED_NAME");
						if (rs.getTimestamp("CANCELLED_DATE") != null) {
							cancelledDate = rs.getTimestamp("CANCELLED_DATE").toInstant();
						}
						Integer cancelledId = rs.getInt("CANCELLED_ID");
						String cancelledName = rs.getString("CANCELLED_NAME");

						result.setId(id);
						result.setDate(date);
						result.setShiftId(shiftId);
						if (createdId != null) {
							User createdBy = new User();
							createdBy.setUserId(createdId);
							createdBy.setName(createdName);
							result.setCreatedBy(createdBy);
						}
						if (cancelledId != null) {
							User cancelledBy = new User();
							cancelledBy.setUserId(cancelledId);
							cancelledBy.setName(cancelledName);
							result.setCancelledBy(cancelledBy);
							result.setCancelledDate(cancelledDate);
						}
					}

					BigDecimal price = rs.getBigDecimal("PRICE");
					int units = rs.getInt("UNITS");
					String name = rs.getString("NAME");
					int amount = rs.getInt("AMOUNT");
					int cpId = rs.getInt("CP_ID");
					int productId = rs.getInt("P_ID");

					if (name != null) {
						ChequeProduct product = new ChequeProduct();
						product.setId(cpId);
						product.setProductId(productId);
						product.setName(name);
						product.setPrice(price);
						product.setAmount(amount);
						product.setUnits(Units.values()[units - 1]);
						product.setCheckId(id);
						products.add(product);
					}

				}
				result.setProducts(products);
			}
		} catch (SQLException e) {
			logger.error("Failed to get cheque", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'get' request", e.getCause());
		}
		return result;
	}

	public static RequestEntity getAllInShift(int shiftId) throws UnsuccessfulRequestException {
		String countSql = "SELECT COUNT(ID) FROM CHEQUES WHERE SHIFT_ID = ?";

		String sql = "SELECT c.ID, c.SHIFT_ID, c.CREATED_DATE, c.CANCELLED_DATE, "
				+ "ucreated.ID AS created_id, ucreated.NAME AS created_name," + "cp.AMOUNT, cp.PRICE, cp.ID as CP_ID,"
				+ "p.NAME, p.UNITS," + "ucancelled.ID AS cancelled_id, ucancelled.NAME AS cancelled_name"
				+ " FROM (SELECT ID, SHIFT_ID, CREATED_DATE, CANCELLED_DATE, CREATED_BY, CANCELLED_BY "
				+ " FROM CHEQUES WHERE SHIFT_ID = ? ORDER BY ID DESC) AS c"
				+ " INNER JOIN USERS AS ucreated ON c.CREATED_BY = ucreated.ID"
				+ " LEFT JOIN CHEQUES_PRODUCTS AS cp ON c.ID = cp.CHEQUE_ID"
				+ " LEFT JOIN PRODUCTS AS p ON cp.PRODUCT_ID = p.ID "
				+ " LEFT JOIN USERS as ucancelled ON c.CANCELLED_BY = ucancelled.ID " + "ORDER BY c.ID DESC";

		ConnectionPool cp = ConnectionPool.getInstance();
		RequestEntity result = new RequestEntity();
		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				PreparedStatement countSt = con.prepareStatement(countSql)) {
			int k = 0;
			st.setInt(++k, shiftId);
			k = 0;
			countSt.setInt(++k, shiftId);
			
			collectCheques(result, st, countSt);

		} catch (SQLException e) {
			logger.error("Failed get all cheques", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'getAll' request", e.getCause());
		}
		return result;
	}

	public static RequestEntity getAllInShiftForUser(int shiftId, int userId, int limit, int offset)
			throws UnsuccessfulRequestException {
		String countSql = "SELECT COUNT(ID) FROM CHEQUES WHERE SHIFT_ID = ? AND CREATED_BY = ?";

		String sql = "SELECT c.ID, c.SHIFT_ID, c.CREATED_DATE, c.CANCELLED_DATE, "
				+ "ucreated.ID AS created_id, ucreated.NAME AS created_name," + "cp.AMOUNT, cp.PRICE, cp.ID as CP_ID,"
				+ "p.NAME, p.UNITS," + "ucancelled.ID AS cancelled_id, ucancelled.NAME AS cancelled_name"
				+ " FROM (SELECT ID, SHIFT_ID, CREATED_DATE, CANCELLED_DATE, CREATED_BY, CANCELLED_BY "
				+ " FROM CHEQUES WHERE SHIFT_ID = ? AND CREATED_BY =  ? ORDER BY ID DESC LIMIT ? OFFSET ?) AS c"
				+ " INNER JOIN USERS AS ucreated ON c.CREATED_BY = ucreated.ID"
				+ " LEFT JOIN CHEQUES_PRODUCTS AS cp ON c.ID = cp.CHEQUE_ID"
				+ " LEFT JOIN PRODUCTS AS p ON cp.PRODUCT_ID = p.ID "
				+ " LEFT JOIN USERS as ucancelled ON c.CANCELLED_BY = ucancelled.ID " + "ORDER BY c.ID DESC";

		ConnectionPool cp = ConnectionPool.getInstance();
		RequestEntity result = new RequestEntity();
		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				PreparedStatement countSt = con.prepareStatement(countSql)) {
			int k = 0;
			st.setInt(++k, shiftId);
			st.setInt(++k, userId);
			st.setInt(++k, limit);
			st.setInt(++k, offset);
			k = 0;
			countSt.setInt(++k, shiftId);
			countSt.setInt(++k, userId);
			collectCheques(result, st, countSt);

		} catch (SQLException e) {
			logger.error("Failed to get all cheques for user in current shift", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'get all in shift for user' request", e.getCause());
		}
		return result;
	}

	public static RequestEntity getAll(int limit, int offset) throws UnsuccessfulRequestException {
		String countSql = "SELECT COUNT(ID)" + " FROM CHEQUES";

		String sql = "SELECT c.ID, c.SHIFT_ID, c.CREATED_DATE, c.CANCELLED_DATE, "
				+ " ucreated.ID AS created_id, ucreated.NAME AS created_name," + " cp.AMOUNT, cp.PRICE, cp.ID as CP_ID,"
				+ " p.NAME, p.UNITS," + " ucancelled.ID AS cancelled_id, ucancelled.NAME AS cancelled_name"
				+ " FROM (SELECT ID, SHIFT_ID, CREATED_DATE, CANCELLED_DATE, CREATED_BY, CANCELLED_BY "
				+ " FROM CHEQUES ORDER BY ID DESC LIMIT ? OFFSET ?) AS c"
				+ " INNER JOIN USERS AS ucreated ON c.CREATED_BY = ucreated.ID"
				+ " LEFT JOIN CHEQUES_PRODUCTS AS cp ON c.ID = cp.CHEQUE_ID"
				+ " LEFT JOIN PRODUCTS AS p ON cp.PRODUCT_ID = p.ID "
				+ " LEFT JOIN USERS as ucancelled ON c.CANCELLED_BY = ucancelled.ID " + "ORDER BY c.ID DESC";
		ConnectionPool cp = ConnectionPool.getInstance();
		RequestEntity result = new RequestEntity();
		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				PreparedStatement countSt = con.prepareStatement(countSql)) {

			int k = 0;
			st.setInt(++k, limit);
			st.setInt(++k, offset);

			collectCheques(result, st, countSt);

		} catch (SQLException e) {
			logger.error("Failed to get all cheques", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'get all cheques' request", e.getCause());
		}
		return result;
	}

	public static RequestEntity getAllforUser(int limit, int offset) throws UnsuccessfulRequestException {
		String countSql = "SELECT COUNT(c.ID)" + " FROM CHEQUES AS c"
				+ " INNER JOIN USERS AS ucreated ON c.CREATED_BY = ucreated.ID"
				+ " LEFTJOIN CHEQUES_PRODUCTS AS cp ON c.ID = cp.CHEQUE_ID"
				+ " LEFTJOIN PRODUCTS AS p ON cp.PRODUCT_ID = p.ID "
				+ " LEFTJOIN USERS as ucancelled ON c.CANCELLED_BY = ucancelled.ID";

		String sql = "SELECT c.ID, c.SHIFT_ID, c.CREATED_DATE, c.CANCELLED_DATE, "
				+ " ucreated.ID AS created_id, ucreated.NAME AS created_name," + " cp.AMOUNT, cp.PRICE, cp.ID as CP_ID,"
				+ " p.NAME, p.UNITS," + " ucancelled.ID AS cancelled_id, ucancelled.NAME AS cancelled_name"
				+ " FROM CHEQUES AS c" + " INNER JOIN USERS AS ucreated ON c.CREATED_BY = ucreated.ID"
				+ " LEFT JOIN CHEQUES_PRODUCTS AS cp ON c.ID = cp.CHEQUE_ID"
				+ " LEFT JOIN PRODUCTS AS p ON cp.PRODUCT_ID = p.ID "
				+ " LEFT JOIN USERS as ucancelled ON c.CANCELLED_BY = ucancelled.ID" + " WHERE ucreated.NAME =?"
				+ " ORDER BY c.CREATED_DATE DESC, c.ID ASC" + " LIMIT = ? OFFSET = ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		RequestEntity result = new RequestEntity();
		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql);
				PreparedStatement countSt = con.prepareStatement(countSql)) {

			int k = 0;
			st.setInt(++k, limit);
			st.setInt(++k, offset);

			collectCheques(result, st, countSt);

		} catch (SQLException e) {
			logger.error("Failed to get all cheques for user", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'get all for user' request", e.getCause());
		}
		return result;
	}

	private static void collectCheques(RequestEntity result, PreparedStatement st, PreparedStatement countSt)
			throws SQLException {
		List<Cheque> objects = new ArrayList<>();

		try (ResultSet rs = countSt.executeQuery()) {
			if (!rs.next()) {
				return;
			}
			result.setCount(rs.getInt(1));

		}

		try (ResultSet rs = st.executeQuery()) {
			List<ChequeProduct> products = new ArrayList<>();
			Cheque cheque = new Cheque();
			Integer previousId = null;
			while (rs.next()) {
				int id = rs.getInt("ID");

				if (previousId == null || id != previousId) {
					cheque = new Cheque();
					objects.add(cheque);

					products = new ArrayList<>();
					cheque.setProducts(products);

					previousId = id;
				}

				int shiftId = rs.getInt("SHIFT_ID");
				Instant date = null;
				if (rs.getTimestamp("c.CREATED_DATE") != null) {
					date = rs.getTimestamp("CREATED_DATE").toInstant();

				}
				Integer createdId = rs.getInt("CREATED_ID");
				String createdName = rs.getString("CREATED_NAME");
				Instant cancelledDate = null;
				if (rs.getTimestamp("CANCELLED_DATE") != null) {
					cancelledDate = rs.getTimestamp("CANCELLED_DATE").toInstant();
				}
				Integer cancelledId = rs.getInt("CANCELLED_ID");
				String cancelledName = rs.getString("CANCELLED_NAME");

				cheque.setId(id);
				cheque.setDate(date);
				cheque.setShiftId(shiftId);
				if (createdId != null) {
					User createdBy = new User();
					createdBy.setUserId(createdId);
					createdBy.setName(createdName);
					cheque.setCreatedBy(createdBy);
				}
				if (cancelledId != null) {
					User cancelledBy = new User();
					cancelledBy.setUserId(cancelledId);
					cancelledBy.setName(cancelledName);
					cheque.setCancelledBy(cancelledBy);
					cheque.setCancelledDate(cancelledDate);
				}

				BigDecimal price = rs.getBigDecimal("PRICE");
				int units = rs.getInt("UNITS");
				String name = rs.getString("NAME");
				int amount = rs.getInt("AMOUNT");
				int cpId = rs.getInt("CP_ID");

				if (name != null) {
					ChequeProduct product = new ChequeProduct();
					product.setId(cpId);
					product.setName(name);
					product.setPrice(price);
					product.setAmount(amount);
					product.setUnits(Units.values()[units - 1]);
					product.setCheckId(id);
					products.add(product);
				}

			}

			result.setObjects(objects);
		}
	}

	public static void closeCheque(int chequeId) throws UnsuccessfulRequestException {
		String sql = "UPDATE CHEQUES SET CREATED_DATE = ? WHERE ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();

		try (Connection con = cp.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

			int k = 0;
			st.setTimestamp(++k, Timestamp.from(Instant.now()));
			st.setInt(++k, chequeId);
			st.executeUpdate();

		} catch (SQLException e) {
			logger.error("Failed to close cheque", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'close cheque' request", e.getCause());
		}
	}

	public static void deleteCheque(int chequeId) throws UnsuccessfulRequestException {
		String sql = "DELETE FROM CHEQUES WHERE ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();

		try (Connection con = cp.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {

			int k = 0;
			st.setInt(++k, chequeId);
			st.executeUpdate();

		} catch (SQLException e) {
			logger.error("Failed to delete cheque", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'cheque deletion' request", e.getCause());
		}
	}

	public static void cancelCheque(Cheque cheque) throws UnsuccessfulRequestException {
		String sql = "UPDATE CHEQUES SET CANCELLED_DATE = ?, CANCELLED_BY = ? WHERE ID = ?";

		String changeSql = "UPDATE PRODUCTS AS p INNER JOIN CHEQUES_PRODUCTS AS cp ON cp.PRODUCT_ID = p.ID "
				+ "set p.AMOUNT = p.AMOUNT + cp.AMOUNT WHERE cp. CHEQUE_ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		synchronized (ProductsLock.class) {
			try (Connection con = cp.getConnection()) {
				try (PreparedStatement st = con.prepareStatement(sql);
						PreparedStatement changeSt = con.prepareStatement(changeSql)) {

					int k = 0;
					changeSt.setInt(++k, cheque.getId());

					k = 0;
					st.setTimestamp(++k, Timestamp.from(Instant.now()));
					st.setInt(++k, cheque.getCancelledBy().getUserId());
					st.setInt(++k, cheque.getId());

					con.setAutoCommit(false);
					changeSt.executeUpdate();
					st.executeUpdate();
					con.commit();
					con.setAutoCommit(true);
				} catch (SQLException e) {
					con.rollback();
					logger.error("Failed to cancel cheque", e);
					throw new UnsuccessfulRequestException("Unsuccessfull 'cheque cancel' request", e.getCause());
				}
			} catch (SQLException e) {
				logger.error("Failed to cancel cheque", e);
				throw new UnsuccessfulRequestException("Unsuccessfull 'cheque cancel' request", e.getCause());
			}
		}
	}
}
