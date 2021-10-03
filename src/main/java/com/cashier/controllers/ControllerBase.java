package com.cashier.controllers;

import com.cashier.dao.ConnectionProvider;

public abstract class ControllerBase implements Controller {

	protected final ConnectionProvider connectionProvider;

	protected ControllerBase(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

}
