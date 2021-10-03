package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ShiftDao;
import com.cashier.exeptions.UnsuccessfulRequestException;

public class ShiftCloseController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ShiftCloseController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		ShiftDao dao = new ShiftDao(connectionProvider);
		try {
			// get id of current shift
			int openShiftId = dao.getCurrentShiftId();
			if (openShiftId > 0) {
				
				dao.closeShift(openShiftId);
				
				//request.removeAttribute("shiftId");
			} else {
				//request.removeAttribute("shiftId");
				request.getSession().setAttribute("errorMsg", "This shift ids alredy closed. No open shifts available");
			}

		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed to close shift", e);
		}
		return new RedirectControllerResponse("shiftgetall");
	}
}
