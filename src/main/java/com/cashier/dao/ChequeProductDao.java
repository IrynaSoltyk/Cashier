package com.cashier.dao;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.ChequeProduct;
import com.cashier.service.ConnectionPool;
import com.cashier.utils.ProductsLock;

public class ChequeProductDao {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	public static void insertProduct(ChequeProduct product) throws UnsuccessfulRequestException {
		String sql = "INSERT INTO CHEQUES_PRODUCTS (CHEQUE_ID, PRODUCT_ID, AMOUNT, PRICE) VALUES (?, ?, ?,("
				+ "SELECT PRICE FROM PRODUCTS WHERE ID = ?))";
		String changeProductAmountSql = "UPDATE PRODUCTS SET AMOUNT = AMOUNT-? WHERE ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		synchronized (ProductsLock.class) {
			try (Connection con = cp.getConnection()) {
				try (PreparedStatement st = con.prepareStatement(sql);
						PreparedStatement reduceSt = con.prepareStatement(changeProductAmountSql)) {
					int k = 0;
					reduceSt.setInt(++k, product.getAmount());
					reduceSt.setInt(++k, product.getProductId());

					k = 0;
					st.setInt(++k, product.getCheckId());
					st.setInt(++k, product.getProductId());
					st.setInt(++k, product.getAmount());
					st.setInt(++k, product.getProductId());
					con.setAutoCommit(false);

					reduceSt.execute();
					st.execute();

					con.commit();
					con.setAutoCommit(true);
				} catch (SQLException e) {
					con.rollback();
					logger.error("Failed to insert ptoduct to cheque", e);
					throw new UnsuccessfulRequestException("Unsuccessfull 'insert product into cheque' request",
							e.getCause());
				}
			} catch (SQLException e) {
				logger.error("Failed to insert ptoduct to cheque", e);
				throw new UnsuccessfulRequestException("Unsuccessfull 'insert product into cheque' request",
						e.getCause());
			}
		}
	}

	public static void updateProduct(ChequeProduct product) throws UnsuccessfulRequestException {
		String retrospectiveSql = "SELECT PRODUCT_ID, AMOUNT FROM CHEQUES_PRODUCTS WHERE ID = ?";
		String changeProductAmountSql = "UPDATE PRODUCTS SET AMOUNT = AMOUNT - ? WHERE ID = ?";
		String sql = "UPDATE CHEQUES_PRODUCTS SET AMOUNT = ? WHERE ID = ?";

		ConnectionPool cp = ConnectionPool.getInstance();

		synchronized (ProductsLock.class) {
			try (Connection con = cp.getConnection()) {

				try (PreparedStatement retrospectiveSt = con.prepareStatement(retrospectiveSql);
						PreparedStatement st = con.prepareStatement(sql);
						PreparedStatement changeSt = con.prepareStatement(changeProductAmountSql)) {

					int k = 0;
					retrospectiveSt.setInt(++k, product.getId());
					con.setAutoCommit(false);
					int productId;
					int amount;
					try (ResultSet rs = retrospectiveSt.executeQuery()) {
						if (!rs.next()) {
							throw new UnsuccessfulRequestException("No records found in database");
						}
						productId = rs.getInt("PRODUCT_ID");
						amount = rs.getInt("AMOUNT");
						logger.info("amount" + amount);

					}
					k = 0;
					int changeAmount = product.getAmount() - amount;
					logger.info("changeAmount" + changeAmount);
					changeSt.setInt(++k, changeAmount);
					changeSt.setInt(++k,productId); logger.info("update pr# "+product.getId());

					k = 0;
					st.setInt(++k, product.getAmount());
					st.setInt(++k, product.getId());

					changeSt.execute();
					st.execute();

					con.commit();
					con.setAutoCommit(true);
				} catch (SQLException e) {
					con.rollback();
					logger.error("Failed to edit product in cheque", e);
					throw new UnsuccessfulRequestException("Unsuccessfull 'edit product in cheque' request",
							e.getCause());
				}
			} catch (SQLException e) {
				logger.error("Failed to insert ptoduct to cheque", e);
				throw new UnsuccessfulRequestException("Unsuccessfull 'insert product into cheque' request",
						e.getCause());
			}
		}
	}

	public static void deleteProduct(int cpId) throws UnsuccessfulRequestException {
		String retrospectiveSql = "SELECT PRODUCT_ID, AMOUNT FROM CHEQUES_PRODUCTS WHERE ID = ?";
		String changeProductAmountSql = "UPDATE PRODUCTS SET AMOUNT = AMOUNT + ? WHERE ID = ?";
		String sql = "DELETE FROM CHEQUES_PRODUCTS WHERE ID = ?";
		
		ConnectionPool cp = ConnectionPool.getInstance();

		synchronized (ProductsLock.class) {
			try (Connection con = cp.getConnection()) {
				try (PreparedStatement st = con.prepareStatement(sql);
						PreparedStatement changeSt = con.prepareStatement(changeProductAmountSql);
						PreparedStatement retrospectiveSt = con.prepareStatement(retrospectiveSql)) {
					
					int k = 0;
					retrospectiveSt.setInt(++k, cpId);
					
					con.setAutoCommit(false);
					int amount;
					int productId;
					try (ResultSet rs = retrospectiveSt.executeQuery()) {
						if (!rs.next()) {
							throw new UnsuccessfulRequestException("No records found in database");
						}
						productId = rs.getInt("PRODUCT_ID");
						amount = rs.getInt("AMOUNT");
						logger.info("amount" + amount);

					}
					
					k = 0;
					changeSt.setInt(++k, amount);
					changeSt.setInt(++k, productId);

					k = 0;
					st.setInt(++k, cpId);

					changeSt.execute();
					st.execute();
					con.commit();
					con.setAutoCommit(true);
				} catch (SQLException e) {
					con.rollback();
					logger.error("Failed to delete product from cheque", e);
					throw new UnsuccessfulRequestException("Unsuccessfull 'delete product from cheque' request",
							e.getCause());
				}
			} catch (SQLException e) {
				logger.error("Failed to insert ptoduct to cheque", e);
				throw new UnsuccessfulRequestException("Unsuccessfull 'insert product into cheque' request",
						e.getCause());
			}
		}
	}

}
