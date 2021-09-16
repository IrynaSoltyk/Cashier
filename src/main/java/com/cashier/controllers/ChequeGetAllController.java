package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.RequestEntity;
import com.cashier.models.Role;
import com.cashier.models.User;
import com.cashier.service.ChequeService;
import com.cashier.service.ShiftService;

public class ChequeGetAllController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

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
			if (request.getParameter("shiftId") != null && request.getParameter("shiftId")!="") {
				shiftId = Integer.parseInt(request.getParameter("shiftId"));
			} else {
				ShiftService shiftService = new ShiftService();
				shiftId = shiftService.getCurrentShiftId();
				request.setAttribute("shiftId", shiftId);
			}
			
			User user = ((User)request.getSession().getAttribute("user"));
			logger.info("user "+ user.getName());
				ChequeService service = new ChequeService();
				RequestEntity re = null;
				if (user.getRoles().contains(Role.MANAGER)) {
					re = service.getAllCheques(page-1, limit);
				}
				else {
					re = service.getAllInShiftForUser(shiftId, user.getUserId(), page-1, limit);
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
