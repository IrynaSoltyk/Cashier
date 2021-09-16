package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.User;
import com.cashier.service.ChequeService;

public class ChequeCancelController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		int chequeId = -1;
		if (request.getParameter("chequeId") != null) {
			chequeId = Integer.parseInt(request.getParameter("chequeId"));
		}

		ChequeService service = new ChequeService();
		try {
			Cheque cheque = new Cheque();
			cheque.setId(chequeId);
			User user = (User) request.getSession().getAttribute("user");
			cheque.setCancelledBy(user);
			service.cancelCheque(cheque);
			request.getSession().setAttribute("successMsg", "Cheque has been successfully canceled");
			return new RedirectControllerResponse("chequegetall");
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed canceling cheque", e);
			request.getSession().setAttribute("errorMsg", "Cant cancel this cheque");
			return new RedirectControllerResponse("chequegetall");
		}

	}

}
