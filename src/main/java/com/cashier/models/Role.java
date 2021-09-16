package com.cashier.models;

public enum Role {
	COMMODITY_EXPERT(1, "productgetall"),
	CASHIER(2, "chequegetall"),
	MANAGER(3, "shiftgetall");
	private int id;
	private String startPage;
	
	Role(int id, String startPage) {
		this.id = id;
		this.startPage = startPage;
		
	}
	
	public int getId() {
		return id;
	}
	
	public String getStartPage() {
		return startPage;
	}
	
	public String toString() {
		return this.name();
	}
	
}
