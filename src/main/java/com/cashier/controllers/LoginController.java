package com.cashier.controllers;

import java.lang.invoke.MethodHandles;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.models.Role;
import com.cashier.models.User;
import com.cashier.service.UserService;


public class LoginController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		String returnPath = request.getRequestURI();
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		try {
			UserService  service = new UserService();
			User user = service.getUser(login);
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
				request.getSession().setAttribute("errorMsg", "Please check your login or password");
			}
		} catch(Exception e) {
			logger.error("Failed to login", e);
			return new RedirectControllerResponse("login.jsp");
		}
		return new RedirectControllerResponse("login.jsp");
	}
}

