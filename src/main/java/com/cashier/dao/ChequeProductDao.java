package com.cashier.dao;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.exeptions.UserMessageException;
import com.cashier.models.ChequeProduct;
import com.cashier.utils.ProductsLock;

public class ChequeProductDao {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private final ConnectionProvider connectionProvider;

	public ChequeProductDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}
	
	public void insertProduct(ChequeProduct product) throws UnsuccessfulRequestException, UserMessageException {
		final String amountSql = "SELECT AMOUNT FROM PRODUCTS WHERE ID = ?";
		final String sql = "INSERT INTO CHEQUES_PRODUCTS (CHEQUE_ID, PRODUCT_ID, AMOUNT, PRICE) VALUES (?, ?, ?,"
				+ "(SELECT PRICE FROM PRODUCTS WHERE ID = ?))";
		final String changeProductAmountSql = "UPDATE PRODUCTS SET AMOUNT = AMOUNT-? WHERE ID = ?";
		int actualAmount;
		synchronized (ProductsLock.class) {
			try (Connection con = connectionProvider.getConnection()) {
				try (PreparedStatement amountSt = con.prepareStatement(amountSql);
						PreparedStatement st = con.prepareStatement(sql);
						PreparedStatement reduceSt = con.prepareStatement(changeProductAmountSql)) {
					
					int k = 0;
					amountSt.setInt(++k,product.getProductId());
					try (ResultSet rs = amountSt.executeQuery()){
						if (!rs.next()) {
							logger.error("No products found in products table with id "+product.getId());
							throw new UnsuccessfulRequestException("No products found in products table with id "+product.getId());
						}
					actualAmount = rs.getInt(1);	
					}
					 if(actualAmount < product.getAmount()) {
							throw new UserMessageException("There only "+actualAmount+"of this product left");
						
					 }
					k = 0;
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

	public void updateProduct(ChequeProduct product) throws UnsuccessfulRequestException, UserMessageException {
		final String amountSql = "SELECT AMOUNT FROM PRODUCTS WHERE ID = ?";
		final String retrospectiveSql = "SELECT PRODUCT_ID, AMOUNT FROM CHEQUES_PRODUCTS WHERE ID = ?";
		final String changeProductAmountSql = "UPDATE PRODUCTS SET AMOUNT = AMOUNT - ? WHERE ID = ?";
		final String sql = "UPDATE CHEQUES_PRODUCTS SET AMOUNT = ? WHERE ID = ?";
		int actualAmount;
		synchronized (ProductsLock.class) {
			try (Connection con = connectionProvider.getConnection()) {

				try (PreparedStatement amountSt = con.prepareStatement(amountSql);
						PreparedStatement retrospectiveSt = con.prepareStatement(retrospectiveSql);
						PreparedStatement st = con.prepareStatement(sql);
						PreparedStatement changeSt = con.prepareStatement(changeProductAmountSql)) {

					int k = 0;
					amountSt.setInt(++k,product.getId());
					try (ResultSet rs = amountSt.executeQuery()){
						if (!rs.next()) {
							logger.error("No products found in products table with id "+product.getId());
							throw new UnsuccessfulRequestException("No products found in products table with id "+product.getId());
						}
					actualAmount = rs.getInt(1);	
					}
					 if(actualAmount < product.getAmount()) {
							throw new UserMessageException("There s only "+actualAmount+"of this product left");
						
					 }
					k = 0;
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
	
	public  boolean isInCheque(int productId, int chequeId) throws UnsuccessfulRequestException {
		final String sql = "SELECT 1 FROM CHEQUES_PRODUCTS WHERE CHEQUE_ID = ? AND PRODUCT_ID = ?";
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			int k = 0;
			st.setInt(++k, chequeId);
			st.setInt(++k, productId);

			try(ResultSet rs = st.executeQuery()){
				if (rs.next()) {
					return true;
				}
			}
						
		} catch (SQLException e) {
			logger.error("Failed create cheque", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'insert' request", e);
		}
		return false;

	}
	

	public void deleteProduct(int cpId) throws UnsuccessfulRequestException {
		final String retrospectiveSql = "SELECT PRODUCT_ID, AMOUNT FROM CHEQUES_PRODUCTS WHERE ID = ?";
		final String changeProductAmountSql = "UPDATE PRODUCTS SET AMOUNT = AMOUNT + ? WHERE ID = ?";
		final String sql = "DELETE FROM CHEQUES_PRODUCTS WHERE ID = ?";
		
		

		synchronized (ProductsLock.class) {
			try (Connection con = connectionProvider.getConnection()) {
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
