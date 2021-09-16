package com.cashier.controllers;

import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.cashier.RouteMapping;

public class ControllerFactory {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	private ControllerFactory() {}
	
	public static Controller produce(HttpServletRequest request) {
		try {
			Controller result = RouteMapping.getRoute(request).getControllerClass().newInstance();
			return result;

		} catch (InstantiationException | IllegalAccessException e) {
			logger.error("Failed to instantiate controller", e);
			return null;
		}
	}
}
