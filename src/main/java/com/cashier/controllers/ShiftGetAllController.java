package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ShiftDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.dao.RequestEntity;

public class ShiftGetAllController extends ControllerBase{
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ShiftGetAllController(ConnectionProvider connectionProvider) {
		super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		RequestEntity re = null;
		ShiftDao dao = new ShiftDao(connectionProvider);
		int page = 1;
		int limit = 5;
		try {
			if (request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			if (request.getParameter("limit") != null) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			
			int openShiftId = dao.getCurrentShiftId();
			
			if (openShiftId > 0) {
				request.setAttribute("shiftId", openShiftId);
			}
			re = dao.getAllShifts(limit, (page-1) * limit);

			int count = re.getCount();
			request.setAttribute("shifts", re.getObjects());
			request.setAttribute("count", count);
			request.setAttribute("pageN", page);
			request.setAttribute("pagesN", count%limit == 0? count/limit: count/limit+1);
			request.setAttribute("limit", limit);
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed to form list of shifts", e);
			return new RedirectControllerResponse("error.jsp");
		}
	return new ForwardControllerResponse("shifts.jsp");
}

}
