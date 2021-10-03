package com.cashier.models;

import java.math.BigDecimal;

public class Product {
	private int id;
	private String name;
	private Units units;
	private BigDecimal price;
	private int amount;
	private boolean deleted;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Units getUnits() {
		return units;
	}

	public int getAmount() {
		return amount;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(BigDecimal price) {
		this.price = new BigDecimal(price.unscaledValue(), 2);
	}

	public void setUnits(Units units) {
		this.units = units;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
