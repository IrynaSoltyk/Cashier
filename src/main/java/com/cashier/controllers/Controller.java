package com.cashier.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Controller {
	ControllerResponse process(HttpServletRequest request, HttpServletResponse response);
}
