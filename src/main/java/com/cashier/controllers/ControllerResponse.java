package com.cashier.controllers;

public class ControllerResponse {
	private boolean redirected;
	private String address;

	public ControllerResponse(boolean redirected, String address) {
		super();
		this.redirected = redirected;
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isRedirected() {
		return redirected;
	}
	
	
}
