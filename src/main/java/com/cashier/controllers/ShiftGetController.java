package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ShiftDao;
import com.cashier.models.Shift;

public class ShiftGetController implements Controller{
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
		if(request.getSession().getAttribute("shiftId")!=null) {
			Shift shift = ShiftDao.get(Integer.parseInt("shiftId"));
			request.setAttribute("shift", shift);
		}
		return "/shift.jsp";
	}

}
