package com.cashier.models;

import java.math.BigDecimal;

public class ChequeProduct {
	private int id;
	private int checkId;
	private int productId;
	private String name;
	private BigDecimal price;
	private int amount;
	private Units units;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCheckId() {
		return checkId;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public int getAmount() {
		return amount;
	}

	public Units getUnits() {
		return units;
	}

	public void setCheckId(int checkId) {
		this.checkId = checkId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setUnits(Units units) {
		this.units = units;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public BigDecimal getTotalPrice() {
		return price.multiply(BigDecimal.valueOf(amount));
	}

}
