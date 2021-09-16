package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.Shift;
import com.cashier.models.User;
import com.cashier.service.ChequeService;
import com.cashier.service.ShiftService;

public class ChequeAddController implements Controller {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) {
	
		try {
		
			
			
			} catch (UnsuccessfulRequestException e) {
				logger.error("Failed closing cheque", e);
				request.getSession().setAttribute("errorMsg", "There was a problem");
				return "chequegetall";
			}

		return "cheque.jsp";
	}
}
