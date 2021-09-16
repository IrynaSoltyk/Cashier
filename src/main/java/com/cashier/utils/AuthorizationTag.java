package com.cashier.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.cashier.RouteMapping;
import com.cashier.models.Role;
import com.cashier.models.RoutingEntity;

public class AuthorizationTag extends SimpleTagSupport {
	private List<Role> userRoles;
	private String uri;
	
	public void doTag() throws JspException, IOException {
		
	}
	
	//public void setUserRoles(List<Role> userRoles) {
	//	this.userRoles = userRoles;
	//}
	
	//public void setURI(String uri) {
	//	this.uri = uri;
	//}
	
	
}