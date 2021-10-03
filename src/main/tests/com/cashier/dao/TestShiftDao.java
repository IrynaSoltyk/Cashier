package com.cashier.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cashier.CashierTestBase;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Shift;

public class TestShiftDao  extends CashierTestBase {
	
	@Test
	public void openShouldNotOpenNewShift() throws UnsuccessfulRequestException, SQLException {
		ShiftDao dao = new ShiftDao(connectionProvider);
		Shift shift = new Shift();
		shift.setUserId(defaultUserCashierId);
		dao.create(shift);
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM SHIFTS WHERE ID=" + shift.getId())) {
			if (rs.next()) {
				assertEquals(shift.getUserId(), rs.getInt("USER_ID"));
			} else {
				fail("No shift has been created");
			}

		}
	}

	@Test
	public void closeShouldCloseShift() throws UnsuccessfulRequestException, SQLException {
		ShiftDao dao = new ShiftDao(connectionProvider);
		dao.closeShift(defaultShiftId);
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT ID FROM SHIFTS WHERE END_DATE IS NOT NULL")) {
			if (rs.next()) {
				assertEquals(defaultShiftId, rs.getInt("ID"));
			} else {
				fail("No shift has been closed");
			}

		}
	}
	
	@Test
	public void getAllShiftsShouldGetAllShifts() throws UnsuccessfulRequestException, SQLException {
		ShiftDao dao = new ShiftDao(connectionProvider);
		Shift shift = new Shift();
		shift.setUserId(defaultUserCashierId);
		dao.create(shift);
		Shift shift1 = new Shift();
		shift1.setUserId(defaultUserCashierId);
		dao.create(shift1);

		List<Shift> actual = (List<Shift>) dao.getAllShifts(Integer.MAX_VALUE, 0).getObjects();
		List<Shift> expected = null;
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT ID FROM SHIFTS")) {
			expected = new ArrayList<>();
			while (rs.next()) {
				Shift s = new Shift();
				s.setId(rs.getInt("ID"));
				expected.add(s);
			}

		}
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
	}
	
	@Test
	public void getShiftsShouldGetShiftById() throws UnsuccessfulRequestException, SQLException {
		ShiftDao dao = new ShiftDao(connectionProvider);
		Shift actual = dao.getShift(defaultShiftId);
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT ID FROM SHIFTS WHERE ID = " + defaultShiftId)) {
			if(!rs.next()) {
				fail("No shift found");
			} else {
				assertEquals(rs.getInt("ID"), actual.getId());
			}
			
		}
	}
	
	@Test
	public void getCurrentShiftsShouldGetCurrentShift() throws UnsuccessfulRequestException, SQLException {
		ShiftDao dao = new ShiftDao(connectionProvider);
		int actualId = dao.getCurrentShiftId();
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT ID FROM SHIFTS WHERE END_DATE IS NULL")) {
			if(!rs.next()) {
				fail("No shift found");
			} else {
				assertEquals(rs.getInt("ID"), actualId);
			}
			
		}
	}
}
