package com.cashier.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Servlet Filter implementation class AuthentificationFilter
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		
		String originalPath = request.getRequestURI();
		request.setAttribute("originalPath", originalPath);
				
		boolean loggedIn = session != null && session.getAttribute("user") != null;
		boolean loginRequest = request.getRequestURI().contains("login");

		if (loggedIn || loginRequest) {
			chain.doFilter(request, response);
		} else {
			response.sendRedirect("login.jsp");
		}
	}
}
