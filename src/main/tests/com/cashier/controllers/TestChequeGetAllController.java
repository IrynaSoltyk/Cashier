package com.cashier.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cashier.CashierTestBase;
import com.cashier.dao.ChequeDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.Role;
import com.cashier.models.User;

public class TestChequeGetAllController extends CashierTestBase {

	@Test
	public void chequeGetAllControllerShouldReturnOnlyChequesFromThisShiftByThisUserIfRoleCashierTest() throws UnsuccessfulRequestException, SQLException{
		mockRoles.add(Role.CASHIER);
		ChequeDao dao = new ChequeDao(connectionProvider);
		Cheque cheque = new Cheque();
		User user = new User();
		user.setUserId(defaultUserManagerId);
		cheque.setCreatedBy(user);
		cheque.setShiftId(defaultShiftId);
		dao.create(cheque);
		Controller controller = new  ChequeGetAllController(connectionProvider);
		
		ControllerResponse controllerResponse = controller.process(mockedRequest, mockedResponse);	

		assertNotNull(controllerResponse);
		assertTrue(mockedAttributes.containsKey("cheques"));
		List<Cheque> cheques = (ArrayList<Cheque>)mockedAttributes.get("cheques");
		assertEquals(1, cheques.size());
			
			
		
	}
	
	public void chequeGetAllControllerShouldReturAllChequesIfRoleManagerTest() throws UnsuccessfulRequestException, SQLException{
		mockRoles.add(Role.MANAGER);
		ChequeDao dao = new ChequeDao(connectionProvider);
		Cheque cheque = new Cheque();
		User user = new User();
		user.setUserId(defaultUserManagerId);
		cheque.setCreatedBy(user);
		cheque.setShiftId(defaultShiftId);
		dao.create(cheque);
		Controller controller = new  ChequeGetAllController(connectionProvider);
		
		ControllerResponse controllerResponse = controller.process(mockedRequest, mockedResponse);	

		assertTrue(mockedAttributes.containsKey("cheques"));
		List<Cheque> cheques = (List<Cheque>)mockedAttributes.get("cheques");
		assertEquals(2, cheques.size());
		
	}
}
