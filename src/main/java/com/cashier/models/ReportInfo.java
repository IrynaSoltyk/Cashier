package com.cashier.models;

import java.math.BigDecimal;

public class ReportInfo {
	private int shiftId;
	private int closed;
	private int cancelled;
	private BigDecimal cancelledCost;
	private BigDecimal closedCost;
	
	public int getClosed() {
		return closed;
	}
	public void setClosed(int closed) {
		this.closed = closed;
	}
	public int getCancelled() {
		return cancelled;
	}
	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}
	public BigDecimal getCancelledCost() {
		return cancelledCost;
	}
	public void setCancelledCost(BigDecimal cancelledCost) {
		this.cancelledCost = cancelledCost;
	}
	public BigDecimal getClosedCost() {
		return closedCost;
	}
	public void setClosedCost(BigDecimal closedCost) {
		this.closedCost = closedCost;
	}
	public int getShiftId() {
		return shiftId;
	}
	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}
}
