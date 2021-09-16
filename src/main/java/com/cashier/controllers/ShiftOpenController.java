package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Shift;
import com.cashier.models.User;
import com.cashier.service.ShiftService;

public class ShiftOpenController implements Controller{
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		ShiftService service = new ShiftService();
		try {
			int openShiftId = service.getCurrentShiftId();
			if (openShiftId > 0) {
				request.getSession().setAttribute("errorMsg", "There is open shift. You should close it before opening new one");
				return new RedirectControllerResponse("shiftgetall");
			} 
			Shift shift = new Shift();
			User user = (User)request.getSession().getAttribute("user");
			shift.setUserId(user.getUserId());
			service.createShift(shift);
			request.setAttribute("shiftId", shift.getId());
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed open shift", e);
			return new RedirectControllerResponse("shiftgetall");
		}
		return new RedirectControllerResponse("shiftgetall");
	}
}
