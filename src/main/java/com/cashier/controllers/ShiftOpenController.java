package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ShiftDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Shift;
import com.cashier.models.User;

public class ShiftOpenController extends ControllerBase{
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ShiftOpenController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		ShiftDao dao = new ShiftDao(connectionProvider);
		try {
			int openShiftId = dao.getCurrentShiftId();
			if (openShiftId > 0) {
				request.getSession().setAttribute("errorMsg", "There is open shift. You should close it before opening new one");
				return new RedirectControllerResponse("shiftgetall");
			} 
			Shift shift = new Shift();
			User user = (User)request.getSession().getAttribute("user");
			shift.setUserId(user.getUserId());
			
			dao.create(shift);
			
			//request.setAttribute("shiftId", shift.getId());
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed open shift", e);
			return new RedirectControllerResponse("shiftgetall");
		}
		return new RedirectControllerResponse("shiftgetall");
	}
}
