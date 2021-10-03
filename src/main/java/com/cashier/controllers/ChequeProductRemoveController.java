package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ChequeDao;
import com.cashier.dao.ChequeProductDao;
import com.cashier.dao.ConnectionProvider;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;


public class ChequeProductRemoveController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ChequeProductRemoveController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		int cpId = -1;
		int chequeId = -1;
		Cheque cheque = null;
		if (request.getParameter("cpId") != null) {
			cpId = Integer.parseInt(request.getParameter("cpId"));
		}

		if (request.getParameter("chequeId") != null) {
			chequeId = Integer.parseInt(request.getParameter("chequeId"));
		}
		try {
			if( cpId > 0 ) {
				ChequeProductDao cpDao = new ChequeProductDao(connectionProvider);
				
				cpDao.deleteProduct(cpId);
				
			} else throw new UnsuccessfulRequestException("Wrong parameters");
				ChequeDao dao = new ChequeDao(connectionProvider);
				cheque = dao.get(chequeId);
				//request.setAttribute("cheque", cheque);
				//request.setAttribute("action", "edit");
			
	}catch(	UnsuccessfulRequestException e) {
		logger.error("Failed adding/editing product", e);
		request.getSession().setAttribute("errorMsg", "There was a problem adding/ editing product");
	}return new RedirectControllerResponse("chequeedit?chequeId="+cheque.getId());
}

}
