package com.cashier.controllers;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.cashier.RouteMapping;
import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.PooledConnectionProvider;

public class ControllerFactory {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	private static final ConnectionProvider connectionProvider = PooledConnectionProvider.getInstance();
	
	private ControllerFactory() {}
	
	public static Controller produce(HttpServletRequest request) {
		try {
			Class<? extends Controller> controllerClass = RouteMapping.getRoute(request).getControllerClass();
			if (controllerClass == null) {
				//TODO: redirect to page not found, i.e. page not found controller
			}
			
			return controllerClass.getConstructor(ConnectionProvider.class).newInstance(connectionProvider); 

		} catch (InstantiationException
				| IllegalAccessException
				| NoSuchMethodException 
				| IllegalArgumentException
				| InvocationTargetException
				| SecurityException e) {
			logger.error("Failed to instantiate controller", e);
			return null;
		}
	}
}
