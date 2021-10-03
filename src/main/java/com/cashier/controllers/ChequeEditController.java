package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ChequeDao;
import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ShiftDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.User;

public class ChequeEditController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ChequeEditController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		ChequeDao dao = new ChequeDao(connectionProvider);
		Cheque cheque = new Cheque();
		User createdBy = null;
		int chequeId = -1;
		String action = "edit";
		try {
			if ((User) request.getSession().getAttribute("user") != null) {
				createdBy = (User)request.getSession().getAttribute("user");
			}
			if (request.getParameter("chequeId") != null) {
				chequeId = Integer.parseInt(request.getParameter("chequeId"));
			}
			if (request.getParameter("action") != null) {
				action = request.getParameter("action");
			}

			if (action.equals("edit")) { //edit
				cheque = dao.get(chequeId);
				request.setAttribute("cheque", cheque);
			}
			else { //add
				ShiftDao shiftDao = new ShiftDao(connectionProvider);
				int openShiftId = shiftDao.getCurrentShiftId();
				if (openShiftId < 1) {
					request.getSession().setAttribute("errorMsg", "No open shifts. Unable to create new cheque");
					return new RedirectControllerResponse("chequegetall");
				}
				cheque.setCreatedBy(createdBy);
				cheque.setShiftId(openShiftId);
				dao.create(cheque);
				logger.info("id="+cheque.getId());
				request.setAttribute("cheque", cheque);
				
			}

		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed " + action + " cheque", e);
			request.getSession().setAttribute("errorMsg", "There was a problem " + action + " cheque");
			return  new RedirectControllerResponse("chequegetall");
		}

		return new ForwardControllerResponse("cheque.jsp");
	}
}
