package com.cashier;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cashier.controllers.Controller;
import com.cashier.controllers.ControllerFactory;
import com.cashier.controllers.ControllerResponse;
import com.cashier.controllers.RedirectControllerResponse;

@WebServlet("/")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String forwardAddress = process(request, response);
		// request.getRequestDispatcher(forwardAddress).forward(request, response);
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String forwardAddress = process(request, response);
		// request.getRequestDispatcher(forwardAddress).forward(request,
		// response);//.sendRedirect(redirectAddress);
		process(request, response);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Controller controller = ControllerFactory.produce(request);
		ControllerResponse controllerResponse = controller.process(request, response);
		if (controllerResponse instanceof RedirectControllerResponse) {
			response.sendRedirect(controllerResponse.getAddress());
		} else {
			request.getRequestDispatcher(controllerResponse.getAddress()).forward(request, response);
		}
	}

}
