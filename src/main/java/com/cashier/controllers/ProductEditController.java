package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ProductDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Product;

public class ProductEditController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ProductEditController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			int id = -1; 
			if (request.getParameter("productId") != null) {
				id = Integer.parseInt(request.getParameter("productId"));
			}
			if (id > 0) {
				ProductDao dao = new ProductDao(connectionProvider);
				Product product = dao.get(id);
				request.setAttribute("product", product);
				request.setAttribute("action", "edit");			
			}				
			
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed to load product for editing", e);
		}
		return new ForwardControllerResponse("product.jsp");
	}

}
