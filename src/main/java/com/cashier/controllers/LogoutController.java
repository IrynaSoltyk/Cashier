package com.cashier.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController implements Controller{

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
			request.getSession().invalidate();
		return new RedirectControllerResponse("login.jsp");
	}

}
