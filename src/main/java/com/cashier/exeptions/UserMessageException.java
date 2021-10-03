package com.cashier.exeptions;

public class UserMessageException  extends Exception {
	private static final long serialVersionUID = 998L;
	
	public UserMessageException(String message){
		super(message);
		
	}
}