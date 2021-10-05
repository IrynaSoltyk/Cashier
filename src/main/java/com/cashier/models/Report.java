package com.cashier.models;

import java.math.BigDecimal;
import java.util.List;

public class Report {
	private int shiftId;
	private List<Cheque> closed;
	private List<Cheque> cancelled;
	
	
	
	public int getShiftId() {
		return shiftId;
	}
	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}
	public List<Cheque> getClosed() {
		return closed;
	}
	public void setClosed(List<Cheque> closed) {
		this.closed = closed;
	}
	public List<Cheque> getCancelled() {
		return cancelled;
	}
	public void setCancelled(List<Cheque> cancelled) {
		this.cancelled = cancelled;
	}

}
