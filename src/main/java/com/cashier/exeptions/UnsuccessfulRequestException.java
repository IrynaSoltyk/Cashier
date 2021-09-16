package com.cashier.exeptions;

public class UnsuccessfulRequestException extends Exception {
	private static final long serialVersionUID = 999L;
	public UnsuccessfulRequestException(String message, Throwable cause){
		super(message+cause);
	}
	public UnsuccessfulRequestException(String message){
		super(message);
		
	}
}
