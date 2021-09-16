package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.service.ShiftService;

public class ShiftCloseController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		ShiftService service = new ShiftService();
		try {
			// get id of current shift
			int openShiftId = service.getCurrentShiftId();
			if (openShiftId > 0) {
				service.closeShift(openShiftId);
				request.removeAttribute("shiftId");
			} else {
				request.removeAttribute("shiftId");
				request.getSession().setAttribute("errorMsg", "This shift ids alredy closed. No open shifts available");
			}

		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed to close shift", e);
		}
		return new RedirectControllerResponse("shiftgetall");
	}
}
