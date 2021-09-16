package com.cashier.filters;

import java.io.IOException;
import java.util.Set;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.cashier.RouteMapping;
import com.cashier.models.Role;
import com.cashier.models.RoutingEntity;
import com.cashier.models.User;

@WebFilter("/*")
public class AuthorizationFilter implements Filter {
	private static User getLoggedInUser(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		
		Object userObject = session.getAttribute("user");
		return userObject instanceof User ? (User)userObject : null;
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		User loggedInUser = getLoggedInUser(request);
		if (loggedInUser == null) {
			chain.doFilter(request, response);
			return;
		}
		
		RoutingEntity route = RouteMapping.getRoute(request);
		if (route == null) {
			chain.doFilter(request, response);
			return;
		}
		
		Set<Role> requiredRoles = route.getRequiredRoles();					
		List<Role> userRoles = loggedInUser.getRoles();
		System.out.println(requiredRoles);
		if (requiredRoles == null || !Collections.disjoint(requiredRoles, userRoles)) {
			chain.doFilter(request, response);
			return;
		}
		
		response.sendRedirect("unauthorized.jsp");
	}
}
