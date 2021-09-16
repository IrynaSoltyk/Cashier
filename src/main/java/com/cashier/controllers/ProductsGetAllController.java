package com.cashier.controllers;

import java.lang.invoke.MethodHandles;
//import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.service.ProductService;
//import com.cashier.models.Product;
import com.cashier.models.RequestEntity;

public class ProductsGetAllController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		RequestEntity objects;
		int count;
		String pattern = "";
		int page = 1;
		int limit = 5;
		String action = "";
		try {
			if (request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			if (request.getParameter("limit") != null) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			if(request.getParameter("searchPattern")!= null) {
				pattern = request.getParameter("searchPattern");			
			}

			ProductService service = new ProductService();
			
			if (request.getParameter("action") != null) {
				action = request.getParameter("action");
			}
			if(action.equals("search")) {
				objects = service.searchAllProducts(pattern, page-1, limit);
			} else {
				objects = service.getAllProducts(page-1, limit);
			}
			count = objects.getCount();
			request.setAttribute("products", objects.getObjects());
			request.setAttribute("count", count);
			request.setAttribute("pageN", page);
			request.setAttribute("pagesN", count%limit == 0? count/limit: count/limit+1);
			request.setAttribute("limit", limit);
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed form list of products", e);
		}
		return new ForwardControllerResponse("products.jsp");
	}

}
