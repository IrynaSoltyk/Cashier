package com.cashier.controllers;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.UserDao;
import com.cashier.models.Role;
import com.cashier.models.User;


public class LoginController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public LoginController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		String returnPath = request.getRequestURI();
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		try {
			UserDao  dao = new UserDao(connectionProvider);
			User user = dao.getUser(login);
			if (user != null && password.equals(user.getPassword())) {
				if(user.getRoles() == null) {
					return new ForwardControllerResponse("about.jsp");
				}
				user.setPassword(null);
				request.getSession().invalidate();
				request.getSession().setAttribute("user", user);
				if(returnPath.equals("/Cashier/login")) {
					List<Role> roles = user.getRoles();
					int max = roles.stream().map(r -> r.getId()).reduce(Integer.MIN_VALUE, Integer::max);
					return new RedirectControllerResponse(Role.values()[max-1].getStartPage());
				}
				return new ForwardControllerResponse(returnPath); 
			} else {
				request.getSession().setAttribute("loginErrorMsg", "Please check your login or password");
			}
		} catch(Exception e) {
			logger.error("Failed to login", e);
			return new RedirectControllerResponse("login.jsp");
		}
		return new RedirectControllerResponse("login.jsp");
	}
}

