package com.cashier.models;

import java.math.BigDecimal;

public class Report {
	private int shiftId;
	private int closed;
	private int cancelled;
	private BigDecimal closedCost;
	private BigDecimal cancelledCost;

	
	public int getClosed() {
		return closed;
	}

	public int getCancelled() {
		return cancelled;
	}

	public BigDecimal getClosedCost() {
		return closedCost;
	}

	public BigDecimal getCancelledCost() {
		return cancelledCost;
	}

	public void setClosed(int closed) {
		this.closed = closed;
	}

	public void setCancelled(int cancelled) {
		this.cancelled = cancelled;
	}

	public void setClosedCost(BigDecimal closedCost) {
		this.closedCost = closedCost;
	}

	public void setCancelledCost(BigDecimal cancelledCost) {
		this.cancelledCost = cancelledCost;
	}

	public int getShiftId() {
		return shiftId;
	}

	public void setShiftId(int shiftId) {
		this.shiftId = shiftId;
	}

}
