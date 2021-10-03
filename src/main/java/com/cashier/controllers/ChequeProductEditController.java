package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ChequeDao;
import com.cashier.dao.ChequeProductDao;
import com.cashier.dao.ConnectionProvider;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.exeptions.UserMessageException;
import com.cashier.models.Cheque;
import com.cashier.models.ChequeProduct;

public class ChequeProductEditController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ChequeProductEditController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		Cheque cheque = null;
		int cpId = -1;
		int amount = -1;
		int chequeId = -1;
		if (request.getParameter("cpId") != null) {
			cpId = Integer.parseInt(request.getParameter("cpId"));
		}
		if (request.getParameter("amount") != null) {
			amount = Integer.parseInt(request.getParameter("amount"));
		}

		if (request.getParameter("cancel") != null && request.getParameter("cancel").equals("yes")) {
			amount = 0;
		}
		if (request.getParameter("chequeId") != null) {
			chequeId = Integer.parseInt(request.getParameter("chequeId"));
		}
		try {
			logger.info(cpId + "|" + amount + "|" + chequeId);
			if (cpId > 0 && amount >= 0 && chequeId > 0) {
				ChequeProduct cp = new ChequeProduct();
				cp.setId(cpId);
				cp.setAmount(amount);
				ChequeProductDao cpDao = new ChequeProductDao(connectionProvider);
				cpDao.updateProduct(cp);
			} else
				throw new UnsuccessfulRequestException("Wrong parameters");
			ChequeDao dao = new ChequeDao(connectionProvider);
			cheque = dao.get(chequeId);
			//request.setAttribute("cheque", cheque);
			//request.setAttribute("action", "edit");

		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed adding/editing product", e);
			request.getSession().setAttribute("errorMsg", "There was a problem adding/ editing product");
		} catch (UserMessageException e) {
			request.getSession().setAttribute("errorMsg", e);
		}
		return new RedirectControllerResponse("chequeedit?chequeId="+cheque.getId());
	}

}
