package com.cashier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.cashier.dao.ConnectionProvider;

public class MockConnectionProvider implements ConnectionProvider {
	private final String url = "jdbc:h2:mem:test;user=;password=;DB_CLOSE_DELAY=-1;mode=mysql";

	@Override
	public Connection getConnection() throws SQLException {	
		return  DriverManager.getConnection(url);
	}

}
