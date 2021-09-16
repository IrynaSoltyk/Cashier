package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.ChequeProduct;
import com.cashier.service.ChequeProductService;
import com.cashier.service.ChequeService;

public class ChequeAddProductController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		Cheque cheque = null;
		int chequeId =-1;
		int amount = 0;
		int productId = -1;
		if (request.getParameter("chequeId") != null) {
			 chequeId = Integer.parseInt(request.getParameter("chequeId")); 
		}
		if (request.getParameter("amount") != null) {
			amount = Integer.parseInt(request.getParameter("amount"));
		}
		if (request.getParameter("productId") != null) {
			productId = Integer.parseInt(request.getParameter("productId"));
		}
		try {

		ChequeProductService cpService = new ChequeProductService();
		ChequeProduct cp = new ChequeProduct();
		if (amount > 0 && chequeId > 0 && productId > 0) {
			cp.setAmount(amount);
			cp.setCheckId(chequeId);
			cp.setProductId(productId); 		
			cpService.insertIntoCheque(cp);
		} else throw new UnsuccessfulRequestException("Wrong parameters");
		ChequeService service = new ChequeService();
		cheque = service.get(chequeId);
		request.setAttribute("cheque", cheque);
		request.setAttribute("action", "edit");
		//request.setAttribute("chequeId", chequeId);
	}catch(	UnsuccessfulRequestException e) {
		logger.error("Failed adding product to cheque", e);
		request.getSession().setAttribute("errorMsg", "There was a problem adding product to cheque");
	}return new RedirectControllerResponse("chequeedit?chequeId="+cheque.getId());
}

}
