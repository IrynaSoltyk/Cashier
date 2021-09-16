package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.service.ChequeProductService;
import com.cashier.service.ChequeService;

public class ChequeProductRemoveController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		int cpId = -1;
		int amount = -1;
		int chequeId = -1;
		Cheque cheque = null;
		if (request.getParameter("cpId") != null) {
			cpId = Integer.parseInt(request.getParameter("cpId"));
		}

		if (request.getParameter("chequeId") != null) {
			chequeId = Integer.parseInt(request.getParameter("chequeId"));
		}
		try {
			logger.info(cpId+"|"+amount+"|"+chequeId);
			if( cpId > 0 ) {
				ChequeProductService cpService = new ChequeProductService();
				cpService.deleteProductFromCheque(cpId);
			} else throw new UnsuccessfulRequestException("Wrong parameters");
				ChequeService service = new ChequeService();
				cheque = service.get(chequeId);
				//request.setAttribute("cheque", cheque);
				//request.setAttribute("action", "edit");
			
	}catch(	UnsuccessfulRequestException e) {
		logger.error("Failed adding/editing product", e);
		request.getSession().setAttribute("errorMsg", "There was a problem adding/ editing product");
	}return new RedirectControllerResponse("chequeedit?chequeId="+cheque.getId());
}

}
