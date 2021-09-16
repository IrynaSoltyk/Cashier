package com.cashier.controllers;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Product;
import com.cashier.models.Units;
import com.cashier.service.ProductService;


public class ProductUpdateController implements Controller{
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
	Product product = new Product();
	product.setId(Integer.parseInt(request.getParameter("id")));
	product.setName(request.getParameter("name"));
	product.setPrice(new BigDecimal(request.getParameter("price")));
	product.setAmount(Integer.parseInt(request.getParameter("amount")));
	product.setUnits(Units.valueOf(request.getParameter("units")));	
		ProductService pus = new ProductService();
	
		try {
			pus.updateProduct(product);
			request.getSession().setAttribute("successMsg", "Changes have been successfully saved");
			
		} catch (UnsuccessfulRequestException e) {	
			logger.error("Failed to save changes after editing product", e);
			request.getSession().setAttribute("errorMsg", "Unable to save your changes");
		}
	return  new RedirectControllerResponse("productgetall");
	}

}
