package com.cashier;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.cashier.dao.ConnectionProvider;

public class TestHelper {
	     
	public static void beforeTest() throws SQLException, ClassNotFoundException {
			ConnectionProvider connectionProvider = new MockConnectionProvider();
		
	        try (Connection con = connectionProvider.getConnection();
	        	Statement statement = con.createStatement()) {
	        	String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
	                    " id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
	                    " login VARCHAR(20) NOT NULL, \n" +
	                    " password VARCHAR(20) NOT NULL, \n" +
	                    " name VARCHAR(30) NOT NULL, \n" +
	                    " PRIMARY KEY (id));";
	            statement.execute(sql);

	            sql = "CREATE TABLE IF NOT EXISTS roles (\n" +
	                    " id INTEGER NOT NULL AUTO_INCREMENT,\n" +
	                    " user_id INTEGER NOT NULL, \n" +
	                    " role INTEGER NOT NULL, \n" +
	                    " PRIMARY KEY (id),"+
	                    " INDEX(user_id),"+
	                    " FOREIGN KEY (user_id) REFERENCES users(id));";
	            statement.execute(sql);

	            sql = "CREATE TABLE IF NOT EXISTS products (\n" +
	                    " id INTEGER NOT NULL AUTO_INCREMENT,\n" +
	                    " name VARCHAR(30) NOT NULL,\n" +
	                    " units INTEGER NOT NULL, \n" +
	                    " price DECIMAL(19,2) NOT NULL, \n" +
	                    " amount INTEGER NOT NULL, \n" +
	                    " deleted BOOLEAN DEFAULT FALSE, \n" +
	                    " PRIMARY KEY (id)"+
	                    " );";
	            statement.execute(sql);


	            sql = "CREATE TABLE IF NOT EXISTS shifts (\n" +
	                    " id INTEGER NOT NULL AUTO_INCREMENT,\n" +
	                    " begin_date TIMESTAMP NULL, \n" +
	                    " end_date TIMESTAMP NULL, \n" +
	                    " user_id INTEGER NOT NULL, \n" +
	                    "  PRIMARY KEY (id),"+
	                    " INDEX(user_id),"+
	                    " FOREIGN KEY(user_id) REFERENCES users(id));";

	            statement.execute(sql);
	            sql = "CREATE TABLE IF NOT EXISTS cheques (\n" +
	                    " id INTEGER NOT NULL AUTO_INCREMENT,\n" +
	                    " created_date TIMESTAMP NULL, \n" +
	                    " created_by INTEGER NOT NULL, \n" +
	                    " shift_id INTEGER NOT NULL, \n" +
	                    " cancelled_date TIMESTAMP NULL, \n" +
	                    " cancelled_by INTEGER NULL, \n" +
	                    " cancelled_shift_id INTEGER NULL, \n" +
	                    " PRIMARY KEY (id),"+
	                    " INDEX(created_by), INDEX(shift_id), INDEX(cancelled_by),"+
	                    " FOREIGN KEY(created_by) REFERENCES users(id),"+
	                    " FOREIGN KEY(cancelled_by) REFERENCES users(id),"+
	                    " FOREIGN KEY(shift_id) REFERENCES shifts(id) ON DELETE RESTRICT);";
	            statement.execute(sql);
	          
	            sql = "CREATE TABLE IF NOT EXISTS cheques_products (\n" +
	                    " id INTEGER NOT NULL AUTO_INCREMENT,\n" +
	                    " cheque_id INTEGER NOT NULL, \n" +
	                    " product_id INTEGER NOT NULL, \n" +
	                    " amount DECIMAL(10) NOT NULL, \n" +
	                    " price DECIMAL(19,2) NOT NULL, \n"+
	                    " PRIMARY KEY (id), "+
	                    " INDEX ix_cheques_products_cheque_id (cheque_id)," +
	                    " INDEX ix_cheques_products_product_id (product_id),"+
	                    " FOREIGN KEY(cheque_id) REFERENCES cheques(id) ON DELETE CASCADE,"+
	                    " FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE RESTRICT);";
	            statement.execute(sql);
		        }
	    }
	}
