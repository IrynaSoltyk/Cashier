package com.cashier.models;

import java.util.Set;
import java.util.EnumSet;

import com.cashier.controllers.Controller;

public class RoutingEntity {
	private Set<Role> requiredRoles;
	private String urlPattern;
	private Class<? extends Controller> controllerClass;

	public Set<Role> getRequiredRoles() {
		return requiredRoles;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public Class<? extends Controller> getControllerClass() {
		return controllerClass;
	}

	public void setRequiredRoles(Role...requiredRoles) {
		this.requiredRoles = EnumSet.noneOf(Role.class);
		
		for (Role requiredRole : requiredRoles) {
			this.requiredRoles.add(requiredRole);
		}
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public void setControllerClass(Class<? extends Controller> controllerClass) {
		this.controllerClass = controllerClass;
	}
}
