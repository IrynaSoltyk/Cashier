package com.cashier.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cashier.dao.ConnectionProvider;

public class NotFoundController extends ControllerBase {

	public NotFoundController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}
	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		return new RedirectControllerResponse("404.jsp");
	}

}
