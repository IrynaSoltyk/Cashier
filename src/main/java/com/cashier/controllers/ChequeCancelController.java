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

public class ChequeCancelController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ChequeCancelController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		int chequeId = -1;
		if (request.getParameter("chequeId") != null) {
			chequeId = Integer.parseInt(request.getParameter("chequeId"));
		}

		ChequeDao dao = new ChequeDao(connectionProvider);
		try {
			Cheque cheque = new Cheque();
			cheque.setId(chequeId);
			User user = (User) request.getSession().getAttribute("user");
			cheque.setCancelledBy(user);

			ShiftDao shiftDao = new ShiftDao(connectionProvider);
			// get id of current shift
			int openShiftId = shiftDao.getCurrentShiftId();
			if (openShiftId > 0) {
				cheque.setCancelled_shift_id(openShiftId);
				
				dao.cancelCheque(cheque);

				request.getSession().setAttribute("successMsg", "Cheque has been successfully canceled");
				return new RedirectControllerResponse("chequegetall");
			} else {
				request.getSession().setAttribute("errorMsg", "No open shifts available. You can cancell shift only with open shift");
				return new RedirectControllerResponse("chequegetall");
			}
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed canceling cheque", e);
			request.getSession().setAttribute("errorMsg", "Cant cancel this cheque");
			return new RedirectControllerResponse("chequegetall");
		}

	}

}
