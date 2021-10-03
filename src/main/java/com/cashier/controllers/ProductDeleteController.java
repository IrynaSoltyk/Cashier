package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ProductDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Product;

public class ProductDeleteController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ProductDeleteController(ConnectionProvider connectionProvider) {
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
				Product product = new Product();
				product.setId(id);
				
				ProductDao dao = new ProductDao(connectionProvider);
				
				dao.delete(product);
				
				request.getSession().setAttribute("successMsg", "the product has been succesfully deleted");
				
			} else
				throw new UnsuccessfulRequestException("Unable to retrieve id from request");
			
		} catch ( UnsuccessfulRequestException e) {
			logger.error("Failed delete product", e);
			request.getSession().setAttribute("errorMsg", "Failed to delete product");
			return new RedirectControllerResponse("productgetall");
		}
		return new RedirectControllerResponse("productgetall");
		
	}

}
