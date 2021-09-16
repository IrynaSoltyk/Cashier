package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.User;
import com.cashier.service.ChequeService;
import com.cashier.service.ShiftService;

public class ChequeEditController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		ChequeService service = new ChequeService();
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
				cheque = service.get(chequeId);
				request.setAttribute("cheque", cheque);
			}
			else { //add
				ShiftService shiftService = new ShiftService();
				int openShiftId = shiftService.getCurrentShiftId();
				if (openShiftId < 1) {
					request.getSession().setAttribute("errorMsg", "No open shifts. Unable to create new cheque");
					return new RedirectControllerResponse("chequegetall");
				}
				cheque.setCreatedBy(createdBy);
				cheque.setShiftId(openShiftId);
				service.create(cheque);
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
