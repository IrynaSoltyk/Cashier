package com.cashier.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cashier.dao.ConnectionProvider;

public class LogoutController extends ControllerBase {
	public LogoutController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}
	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
			request.getSession().invalidate();
		return new RedirectControllerResponse("login.jsp");
	}

}
