package com.cashier.controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass; 
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.cashier.CashierTestBase;
import com.cashier.MockConnectionProvider;
import com.cashier.TestHelper;
import com.cashier.dao.ConnectionProvider;
import com.cashier.dao.ShiftDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.User;

public class TestShiftOpenController extends CashierTestBase {
	
	
	@Test
	public void openShiftShouldNotOpenShiftWhenThereIsOpenShift() throws UnsuccessfulRequestException, SQLException{

		Controller controller = new  ShiftOpenController(connectionProvider);
		ControllerResponse controllerResponse = controller.process(mockedRequest, mockedResponse);	

		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT COUNT(ID) FROM SHIFTS WHERE END_DATE IS NULL")) {
			if(!rs.next()) {
				fail("No shift found");
			} else {
				assertEquals(1, rs.getInt(1));
			}
			
		}	
		assertEquals(true , controllerResponse.isRedirected());
		assertEquals("shiftgetall", controllerResponse.getAddress());
	}
	
	@Test
	public void openShiftShouldOpenShiftWhenThereIsNoOpenShift() throws UnsuccessfulRequestException, SQLException{
		ShiftDao dao = new ShiftDao(connectionProvider);
		dao.closeShift(defaultShiftId);
		Controller controller = new  ShiftOpenController(connectionProvider);
		ControllerResponse controllerResponse = controller.process(mockedRequest, mockedResponse);	

		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT COUNT(ID), ID FROM SHIFTS WHERE END_DATE IS NULL")) {
			if(!rs.next()) {
				fail("No shift found");
			} else {
				assertEquals(1, rs.getInt(1));
				assertNotEquals(defaultShiftId, rs.getInt(2));
			}
			
		}	
		assertEquals(true , controllerResponse.isRedirected());
		assertEquals("shiftgetall", controllerResponse.getAddress());
	}
}
