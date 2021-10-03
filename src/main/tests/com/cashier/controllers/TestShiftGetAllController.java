package com.cashier.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cashier.CashierTestBase;
import com.cashier.dao.ShiftDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Role;
import com.cashier.models.Shift;

public class TestShiftGetAllController extends CashierTestBase{
	@Test
	public void shiftGetAllTest() throws UnsuccessfulRequestException, SQLException{
		mockRoles.add(Role.MANAGER);
		ShiftDao dao = new ShiftDao(connectionProvider);
		dao.closeShift(defaultShiftId);
		Shift shift = new Shift();
		shift.setUserId(defaultUserManagerId);
		dao.create(shift);
		Controller controller = new  ShiftGetAllController(connectionProvider);
		
		ControllerResponse controllerResponse = controller.process(mockedRequest, mockedResponse);	

		assertNotNull(controllerResponse);
		assertTrue(mockedAttributes.containsKey("shifts"));
		List<Shift> shifts = (ArrayList<Shift>)mockedAttributes.get("shifts");
		assertEquals(2, shifts.size());			
					
	}
}
