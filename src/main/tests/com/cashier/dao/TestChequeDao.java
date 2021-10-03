package com.cashier.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cashier.CashierTestBase;
import com.cashier.MockConnectionProvider;
import com.cashier.TestHelper;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.Shift;
import com.cashier.models.User;

public class TestChequeDao  extends CashierTestBase {
	
	@Test
	public void createShouldCreateCheque() throws UnsuccessfulRequestException, SQLException {
		ChequeDao dao = new ChequeDao(connectionProvider);
		Cheque cheque = new Cheque();
		User u = new User();
		u.setUserId(defaultUserCashierId);
		cheque.setCreatedBy(u);
		cheque.setShiftId(defaultShiftId);
		dao.create(cheque);
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM CHEQUES WHERE ID=" + cheque.getId())) {
			if (rs.next()) {
				assertEquals(cheque.getCreatedBy().getUserId(), rs.getInt("CREATED_BY"));
				assertEquals(cheque.getId(), rs.getInt("ID"));
			} else {
				fail("No cheque has been created");
			}

		}
	}
	
	@Test
	public void getShouldgetCheque() throws UnsuccessfulRequestException, SQLException {
		ChequeDao dao = new ChequeDao(connectionProvider);
		dao.get(defaultChequeId);
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM CHEQUES WHERE ID=" + defaultChequeId)) {
			if (rs.next()) {
				assertEquals(defaultChequeId, rs.getInt("ID"));
			} else {
				fail("No cheque with this id");
			}

		}
	}
	
	@Test
	public void getAllInShift() throws UnsuccessfulRequestException, SQLException {
		ChequeDao dao = new ChequeDao(connectionProvider);
		List<Cheque> actual = (List<Cheque>)dao.getAllInShift(defaultShiftId).getObjects();
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT COUNT(ID) FROM CHEQUES WHERE SHIFT_ID=" + defaultShiftId)) {
			if (rs.next()) {
				assertEquals(actual.size(), rs.getInt(1));
			} else {
				fail("No cheques");
			}

		}
	}
	
	@Test
	public void getAllInShiftForUserTest() throws UnsuccessfulRequestException, SQLException {
		ChequeDao dao = new ChequeDao(connectionProvider);
		List<Cheque> actual = (List<Cheque>)dao.getAllInShiftForUser(defaultShiftId, defaultUserCashierId, Integer.MAX_VALUE, 0).getObjects();
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT COUNT(ID) FROM CHEQUES WHERE SHIFT_ID=" + defaultShiftId + "AND CREATED_BY = " + defaultUserCashierId)) {
			if (rs.next()) {
				assertEquals(actual.size(), rs.getInt(1));
			} else {
				fail("No cheques");
			}

		}
	}
	
	@Test
	public void getAllTest() throws UnsuccessfulRequestException, SQLException {
		ChequeDao dao = new ChequeDao(connectionProvider);
		List<Cheque> actual = (List<Cheque>)dao.getAll(Integer.MAX_VALUE, 0).getObjects();
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT COUNT(ID) FROM CHEQUES")) {
			if (rs.next()) {
				assertEquals(actual.size(), rs.getInt(1));
			} else {
				fail("No cheques");
			}

		}
	}
	
	@Test
	public void closeChequeTest() throws UnsuccessfulRequestException, SQLException {
		ChequeDao dao = new ChequeDao(connectionProvider);
		dao.closeCheque(defaultChequeId);
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT 1 FROM CHEQUES WHERE CREATED_DATE IS NOT NULL AND ID = " + defaultChequeId)) {
			if (rs.next()) {
				assertEquals(1, rs.getInt(1));
			} else {
				fail("No closed cheques with this id");
			}

		}
	}
	
	@Test
	public void cancellChequeTest() throws UnsuccessfulRequestException, SQLException {
		ChequeDao dao = new ChequeDao(connectionProvider);
		Cheque cheque = dao.get(defaultChequeId);
		dao.closeCheque(defaultChequeId);
		User u =  new User();
		u.setUserId(defaultUserCashierId);
		cheque.setCancelledBy(u);
		
		dao.cancelCheque(cheque);
		
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT 1 FROM CHEQUES WHERE CANCELLED_DATE IS NOT NULL AND ID = " + defaultChequeId)) {
			if (rs.next()) {
				assertEquals(1, rs.getInt(1));
			} else {
				fail("No cancelled cheques with this id");
			}

		}
	}
	
	@Test
	public void deleteChequeTest() throws UnsuccessfulRequestException, SQLException {
		ChequeDao dao = new ChequeDao(connectionProvider);
		Cheque cheque = dao.get(defaultChequeId);
		dao.deleteCheque(defaultChequeId);
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT 1 FROM CHEQUES WHERE ID = " + defaultChequeId)) {
			if (rs.next()) {
				fail("No shift has been created");
			} 

		}
	}

}
