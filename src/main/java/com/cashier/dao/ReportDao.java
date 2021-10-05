package com.cashier.dao;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.ChequeProduct;
import com.cashier.models.Report;

public class ReportDao {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private final ConnectionProvider connectionProvider;

	public ReportDao(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public Report create(int shiftId) throws UnsuccessfulRequestException {
		Report report = null;
		final String sql = "SELECT CHEQUE_ID, PRODUCT_ID, AMOUNT, PRICE, (c.CANCELLED_SHIFT_ID IS NOT NULL) AS CANCELLED "
				+ "FROM CHEQUES_PRODUCTS cp "
				+ " INNER JOIN CHEQUES c  ON c.ID = cp.CHEQUE_ID"
				+ " WHERE CANCELLED_SHIFT_ID = ? OR SHIFT_ID = ? ORDER BY CHEQUE_ID";
		
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement st = con.prepareStatement(sql)) {

			int k = 0;
			st.setInt(++k, shiftId);
			st.setInt(++k, shiftId);
			report = new Report();
			try (ResultSet rs = st.executeQuery()) {
				//int i = 0;
				int previous = -1;
				Cheque cheque;
				List<Cheque> closed = new ArrayList<>();
				List<Cheque> cancelled = new ArrayList<>();
				List<ChequeProduct> products = null;
				while (rs.next()) {
					int chequeId = rs.getInt("CHEQUE_ID");
												
					if(chequeId != previous){
						cheque = new Cheque();
						products = new ArrayList<>();
						cheque.setProducts(products);
						
						if (rs.getBoolean("CANCELLED")) {
								cancelled.add(cheque);
							}else {
								closed.add(cheque);
							}
						
						previous = chequeId;
						
					}
					
					ChequeProduct cp = new ChequeProduct();
					products.add(cp);
					int productId = rs.getInt("PRODUCT_ID");
					int amount = rs.getInt("AMOUNT");
					BigDecimal price = rs.getBigDecimal("PRICE");
					cp.setProductId(productId);
					cp.setAmount(amount);
					cp.setPrice(price);
				}
				
				report.setCancelled(cancelled);
				report.setClosed(closed);
			}
		} catch (SQLException e) {
			logger.error("Failed to form report", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'create report' request", e);
		}
		return report;

	}
}
