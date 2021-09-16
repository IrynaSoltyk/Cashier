package com.cashier.models;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class Cheque {
	private int id;
	private User createdBy;
	private int shiftId;
	private Instant date;
	private List<ChequeProduct> products;
	private Instant cancelledDate;
	private User cancelledBy;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<ChequeProduct> getProducts() {
		return products;
	}

	public void setProducts(List<ChequeProduct> products) {
		this.products = products;
	}


	public int getShiftId() {
		return shiftId;
	}

	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}

	public Instant getCancelledDate() {
		return cancelledDate;
	}

	public void setCancelledDate(Instant cancelledDate) {
		this.cancelledDate = cancelledDate;
	}

	public User getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(User cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	
	public BigDecimal getCost() {
		return products.stream().map(a -> a.getTotalPrice()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
	}
}
