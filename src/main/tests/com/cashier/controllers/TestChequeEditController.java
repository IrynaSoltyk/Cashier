package com.cashier.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;


import org.junit.Test;
import org.mockito.Mockito;

import com.cashier.CashierTestBase;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.Role;


public class TestChequeEditController extends CashierTestBase{
	@Test
	public void chequeEditShouldNotReturnChequeInfoIfChequeIdIsNotSetTest() throws UnsuccessfulRequestException, SQLException{
		mockRoles.add(Role.CASHIER);

		Controller controller = new  ChequeEditController(connectionProvider);
		ControllerResponse controllerResponse = controller.process(mockedRequest, mockedResponse);	

		assertNotNull(controllerResponse);
		assertTrue(mockedAttributes.containsKey("cheque"));
		Cheque cheque = (Cheque)mockedAttributes.get("cheque");
		assertEquals(0, cheque.getId());
	}
	
	@Test
	public void chequeEditShouldReturnChequeInfoIfChequeIdIsSetTest() throws UnsuccessfulRequestException, SQLException{
		mockedRequest.setAttribute("chequeId", defaultChequeId);
		mockRoles.add(Role.CASHIER);
		
		Controller controller = new  ChequeEditController(connectionProvider);
		ControllerResponse controllerResponse = controller.process(mockedRequest, mockedResponse);	

		assertNotNull(controllerResponse);
		assertTrue(mockedAttributes.containsKey("cheque"));
		Cheque cheque = (Cheque)mockedAttributes.get("cheque");
		assertEquals(defaultChequeId, cheque.getId());
	}
	
}
