package com.cashier.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class PooledConnectionProvider implements ConnectionProvider {
	
	private static PooledConnectionProvider instance;

	public static synchronized PooledConnectionProvider getInstance() {
		if (instance == null) {
			instance = new PooledConnectionProvider();
		}
		return instance;
	}

	private PooledConnectionProvider() {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/cashier_database");
		} catch (NamingException ex) {
			throw new IllegalStateException("Cannot init DBManager", ex);
		}
	}

	private DataSource ds;

	@Override
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
}
