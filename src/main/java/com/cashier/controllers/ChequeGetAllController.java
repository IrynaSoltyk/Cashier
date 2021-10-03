package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.dao.ChequeDao;
import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ShiftDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.dao.RequestEntity;
import com.cashier.models.Role;
import com.cashier.models.User;


public class ChequeGetAllController extends ControllerBase {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public ChequeGetAllController(ConnectionProvider connectionProvider) {
	super(connectionProvider);
	}

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		int page = 1;
		int limit = 5;
		int shiftId = -1;

		try {
			if (request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			if (request.getParameter("limit") != null) {
				limit = Integer.parseInt(request.getParameter("limit"));
			}
			//if (request.getParameter("shiftId") != null && request.getParameter("shiftId")!="") {
			//	shiftId = Integer.parseInt(request.getParameter("shiftId"));
			//} else {
				ShiftDao shiftDao = new ShiftDao(connectionProvider);
				shiftId = shiftDao.getCurrentShiftId();
				request.setAttribute("shiftId", shiftId);
		//	}
			
			User user = ((User)request.getSession().getAttribute("user"));
				ChequeDao dao = new ChequeDao(connectionProvider);
				RequestEntity re = null;
				
				int offset = (page - 1) * limit;
				
				if (user.getRoles().contains(Role.MANAGER)) {
					re = dao.getAll(limit, offset);
				}
				else {
					re = dao.getAllInShiftForUser(shiftId, user.getUserId(), limit, offset);
				}
				int count = re.getCount();
				request.setAttribute("cheques", re.getObjects());
				request.setAttribute("count", count);
				request.setAttribute("pageN", page);
				request.setAttribute("pagesN", count%limit == 0? count/limit: count/limit+1);
				request.setAttribute("limit", limit);

		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed to form cheques list", e);
		}
		return new ForwardControllerResponse("cheques.jsp");
	}

}
