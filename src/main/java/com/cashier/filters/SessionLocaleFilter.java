package com.cashier.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class LanguageFilter
 */
@WebFilter(filterName = "SessionLocaleFilter", urlPatterns = {"/*"})
public class SessionLocaleFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
		      throws IOException, ServletException {
		        HttpServletRequest req = (HttpServletRequest) request;
		        
		        String langParameter = req.getParameter("sessionLocale");
		        //String defaultLocale = "en";
		        if (langParameter != null) {// && req.getSession().getAttribute("lang") == null) {
		       //	langParameter = defaultLocale;
		        	
		      //  }
		        req.getSession().setAttribute("lang", langParameter);
		        }
		        System.out.println("here"+ req.getSession().getAttribute("lang"));
		        
		       
		        chain.doFilter(request, response);
		    }
 
}
