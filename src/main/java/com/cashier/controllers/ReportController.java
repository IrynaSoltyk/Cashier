package com.cashier.controllers;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.Report;
import com.cashier.models.RequestEntity;
import com.cashier.service.ChequeService;

public class ReportController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public ControllerResponse process(HttpServletRequest request, HttpServletResponse response) {
		int shiftId = -1;
		String type = "X";
		if (request.getParameter("shiftId") != null && request.getParameter("shiftId") != "") {
			shiftId = Integer.parseInt(request.getParameter("shiftId"));
		}
		if (request.getParameter("type") != null) {
			type = request.getParameter("type");
		}
		try {
			ChequeService service = new ChequeService();
			RequestEntity re = service.getAllinShift(shiftId);
			int cancelled = 0;
			int closed = 0;
			BigDecimal cancelledCost = BigDecimal.ZERO;
			BigDecimal closedCost = BigDecimal.ZERO;
			List<Cheque> cheques = (List<Cheque>)re.getObjects();
			for (Cheque c : cheques) {
				if (c.getDate() != null) {
					if (c.getCancelledDate() == null) {
						closed++;
						closedCost = closedCost.add(c.getCost()); 
					} else {
						cancelled++;
						cancelledCost = cancelledCost.add(c.getCost());
					}
				}
			}
			Report report = new Report();
			report.setClosed(closed);
			report.setCancelled(cancelled);
			report.setCancelledCost(cancelledCost);
			report.setClosedCost(closedCost);
			request.setAttribute("report", report);
			request.setAttribute("type", type);
			request.setAttribute("shiftId", shiftId);
		} catch (UnsuccessfulRequestException e) {
			logger.error("Failed to form report", e);
			request.setAttribute("errorMsg", "Failed to form " + type + " report");
			return new RedirectControllerResponse("shiftgetall");
		}
		return new ForwardControllerResponse("report.jsp");
	}
}
