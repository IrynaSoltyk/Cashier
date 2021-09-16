package com.cashier.models;

import java.time.Instant;
import java.util.List;

public class Shift {
	private int shiftId;
	private Instant beginDate;
	private Instant endDate;
	private List<Cheque> cheques;
	private int userId;
	
	public int getId() {
		return shiftId;
	}
	public Instant getBeginDate() {
		return beginDate;
	}
	public Instant getEndDate() {
		return endDate;
	}
	public int getUserId() {
		return userId;
	}
	public void setId(int shiftId) {
		this.shiftId = shiftId;
	}
	public void setBeginDate(Instant beginDate) {
		this.beginDate = beginDate;
	}
	public void setEndDate(Instant endDate) {
		this.endDate = endDate;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public List<Cheque> getCheques() {
		return cheques;
	}
	public void setCheques(List<Cheque> cheques) {
		this.cheques = cheques;
	}
	
	
	}
