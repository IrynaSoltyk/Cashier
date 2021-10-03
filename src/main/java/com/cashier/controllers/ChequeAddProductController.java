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

public class ChequeAddProductController extends ControllerBase{
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ChequeAddProductController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		Cheque cheque = null;
		int chequeId = -1;
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
			ChequeProductDao dao = new ChequeProductDao(connectionProvider);

			ChequeProduct cp = new ChequeProduct();
			if (amount > 0 && chequeId > 0 && productId > 0) {
				if(dao.isInCheque(productId, chequeId)) {
					request.getSession().setAttribute("errorMsg", "There is such product in cheque. You can edit it s amount");
				} else {
					cp.setAmount(amount);
					cp.setCheckId(chequeId);
					cp.setProductId(productId);
				
					dao.insertProduct(cp);
				}
			} else {
				throw new UnsuccessfulRequestException("Wrong parameters");
			}

			ChequeDao chequeDao = new ChequeDao(connectionProvider);
			cheque = chequeDao.get(chequeId);

		} catch (UnsuccessfulRequestException e) {
			
			logger.error("Failed adding product to cheque", e);
			request.getSession().setAttribute("errorMsg", "There was a problem adding product to cheque");
		} catch (UserMessageException e) {
			request.getSession().setAttribute("errorMsg", e);
		}
		
		return new RedirectControllerResponse("chequeedit?chequeId=" + cheque.getId());
	}

}
