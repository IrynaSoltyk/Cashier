package com.cashier.dao;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Product;
import com.cashier.models.RequestEntity;
import com.cashier.models.Units;
import com.cashier.service.ConnectionPool;

public class ProductDao {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	private ProductDao() {
	}

	public static void create(Product product) throws UnsuccessfulRequestException {
		String sql = "INSERT INTO PRODUCTS (NAME, UNITS, PRICE, AMOUNT) VALUES (?,?,?,?)";
		ConnectionPool cp = ConnectionPool.getInstance();

		try (Connection con = cp.getConnection();
				PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			int k = 0;
			st.setString(++k, product.getName());
			st.setInt(++k, product.getUnits().getId());
			st.setBigDecimal(++k, product.getPrice());
			st.setInt(++k, product.getAmount());
			st.executeUpdate();
			try (ResultSet rs = st.getGeneratedKeys()) {
				if (!rs.next()) {
					throw new UnsuccessfulRequestException("Unsuccessfull 'create' request");
				}
				int id = rs.getInt(1);

				product.setId(id);
			}
		} catch (SQLException e) {
			logger.error("Failed to create product", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'create' request", e.getCause());
		}
	}

	public static RequestEntity searchAllProducts(String pattern, int limit, int offset)
			throws UnsuccessfulRequestException {
		RequestEntity result = new RequestEntity();
		String countSql = "SELECT COUNT(ID) FROM PRODUCTS WHERE NAME LIKE ? OR CAST(ID AS CHAR(20)) LIKE ?";
		String sql = "SELECT * FROM PRODUCTS WHERE NAME LIKE ? OR CAST(ID AS CHAR(20)) LIKE ? LIMIT ? OFFSET ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		try (Connection con = cp.getConnection(); 
				PreparedStatement countSt = con.prepareStatement(countSql);
				PreparedStatement st = con.prepareStatement(sql)) {
			String searchPattern = "%"+pattern+"%";
			countSt.setString(1, searchPattern);
			countSt.setString(2, searchPattern);
			int k = 0;
			st.setString(++k, searchPattern);
			st.setString(++k, searchPattern);
			st.setInt(++k, limit);
			st.setInt(++k, offset);
			createProductsList(result, st, countSt);
		} catch (SQLException e) {
			logger.error("Failed to search products", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'search' request", e.getCause());
		}
		return result;
	}

	public static RequestEntity getAll(int limit, int offset) throws UnsuccessfulRequestException {
		RequestEntity result = new RequestEntity();
		String countSql = "SELECT COUNT(ID) FROM PRODUCTS";
		String sql = "SELECT * FROM PRODUCTS LIMIT ? OFFSET ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		try (Connection con = cp.getConnection(); 
				PreparedStatement st = con.prepareStatement(sql);
				PreparedStatement countSt = con.prepareStatement(countSql)) {
			
			int k = 0;
			st.setInt(++k, limit);
			st.setInt(++k, offset);
			createProductsList(result, st, countSt);
		} catch (SQLException e) {
			logger.error("Failed to get all products", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'Get all products' request", e.getCause());
		}
		return result;
	}

	private static void createProductsList(RequestEntity result, PreparedStatement st, PreparedStatement countSt) throws SQLException {
		try(ResultSet rs = countSt.executeQuery()){
			if(!rs.next()) {
				return;
				
			}
			result.setCount(rs.getInt(1));
		}
		List<Product> objects = new ArrayList<>();
		try (ResultSet rs = st.executeQuery()) {
			while (rs.next()) {
				Product product = new Product();
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				Units units = Units.values()[rs.getInt("UNITS") - 1];
				BigDecimal price = rs.getBigDecimal("PRICE");
				int amount = rs.getInt("AMOUNT");

				product.setId(id);
				product.setName(name);
				product.setUnits(units);
				product.setAmount(amount);
				product.setPrice(price);

				objects.add(product);
			}
			result.setObjects(objects);
		}
	}

	public static Product get(int productId) throws UnsuccessfulRequestException {
		String sql = "SELECT * FROM PRODUCTS WHERE ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		Product result = null;
		try (Connection con = cp.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, productId);
			try (ResultSet rs = st.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				int id = rs.getInt("ID");
				String name = rs.getString("NAME");
				Units units = Units.values()[rs.getInt("UNITS") - 1];
				BigDecimal price = rs.getBigDecimal("PRICE");
				int amount = rs.getInt("AMOUNT");

				result = new Product();
				result.setId(id);
				result.setName(name);
				result.setUnits(units);
				result.setAmount(amount);
				result.setPrice(price);
			}

		} catch (SQLException e) {
			logger.error("Failed to get product", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'Get' request", e.getCause());
		}
		return result;
	}

	public static void update(Product product) throws UnsuccessfulRequestException {
		String sql = "UPDATE PRODUCTS SET NAME = ?, UNITS = ?, PRICE = ?, AMOUNT = ? WHERE ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		try (Connection con = cp.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			int k = 0;
			st.setString(++k, product.getName());
			st.setInt(++k, product.getUnits().getId());
			st.setBigDecimal(++k, product.getPrice());
			st.setInt(++k, product.getAmount());
			st.setInt(++k, product.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			logger.error("Failed to update product", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'update' request", e.getCause());
		}
	}

	public static void delete(Product product) throws UnsuccessfulRequestException {

		String sql = "DELETE FROM PRODUCTS WHERE ID = ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		try (Connection con = cp.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
			st.setInt(1, product.getId());
			st.execute();
		} catch (SQLException e) {
			logger.error("Failed delete product", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'delete' request", e.getCause());
		}
	}

	public static int countRecords() throws UnsuccessfulRequestException {
		int result = 0;
		String sql = "SELECT COUNT * FROM PRODUCTS";
		ConnectionPool cp = ConnectionPool.getInstance();
		try (Connection con = cp.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery(sql)) {
			if (!rs.next()) {
				throw new UnsuccessfulRequestException("No products");
			}
			result = rs.getInt(1);
		} catch (SQLException e) {
			logger.error("Failed to count records", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'countrecords' request", e.getCause());
		}
		return result;
	}

}
