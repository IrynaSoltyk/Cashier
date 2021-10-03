package com.cashier.controllers;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ProductDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Product;
import com.cashier.models.Units;

public class ProductAddController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ProductAddController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		Product product = new Product();
		product.setName(request.getParameter("name"));
		product.setPrice(new BigDecimal(request.getParameter("price")));
		product.setAmount( Integer.parseInt(request.getParameter("amount")));
		product.setUnits(Units.valueOf(request.getParameter("units")));
		try {
			ProductDao dao = new ProductDao(connectionProvider);
			dao.create(product);
			request.getSession().setAttribute("successMsg", "New product has been successfully added");
			
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed to form product list", e);
			request.getSession().setAttribute("errorMsg", "Error adding new product");
			}
		return new RedirectControllerResponse("productgetall");
	}

}
