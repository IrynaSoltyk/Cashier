package com.cashier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.cashier.dao.ConnectionProvider;
import com.cashier.models.Role;
import com.cashier.models.User;

public class CashierTestBase {
	protected int defaultUserCashierId;
	protected int defaultUserManagerId;
	protected int defaultShiftId;
	protected int defaultProductId;
	protected int defaultChequeId;
	protected int defaultChequeProductId;
	
	protected HttpServletRequest  mockedRequest;
	protected HttpServletResponse mockedResponse;
	protected Map<String, Object> mockedAttributes;
	protected List<Role> mockRoles;
	
	protected ConnectionProvider connectionProvider = new MockConnectionProvider();
	private Connection con;
	
	@BeforeClass
	public static void initClass() throws ClassNotFoundException, SQLException {
		TestHelper.beforeTest();
	}

	@Before
	public void before() throws SQLException {
		clearDatabase();
	}

	private void clearDatabase() throws SQLException {
		try (Connection con = connectionProvider.getConnection(); Statement st = con.createStatement()) {
			st.addBatch("DELETE FROM CHEQUES_PRODUCTS");
			st.addBatch("DELETE FROM CHEQUES");
			st.addBatch("DELETE FROM SHIFTS");
			st.addBatch("DELETE FROM PRODUCTS");
			st.addBatch("DELETE FROM USERS");
			st.executeBatch();
		}
		
		defaultUserCashierId = procedeInsertAndReturnId("INSERT INTO USERS (LOGIN, PASSWORD, NAME) VALUES ('cashier', 123, 'Vasya')");
		defaultUserManagerId = procedeInsertAndReturnId("INSERT INTO USERS (LOGIN, PASSWORD, NAME) VALUES ('manager', 123, 'Petya')");
		defaultShiftId = procedeInsertAndReturnId("INSERT INTO SHIFTS (BEGIN_DATE, USER_ID) SELECT '2010-10-01 20:48:55' , u.ID FROM USERS u WHERE u.LOGIN ='manager'");
		defaultProductId = procedeInsertAndReturnId("INSERT INTO PRODUCTS (NAME, UNITS, PRICE, AMOUNT) VALUES ('productName', 2, 1.2 ,12)");
		defaultChequeId = procedeInsertAndReturnId("INSERT INTO CHEQUES (CREATED_BY, SHIFT_ID) SELECT u.ID, s.ID FROM USERS u JOIN SHIFTS s WHERE u.LOGIN ='cashier' ");
		defaultChequeProductId = procedeInsertAndReturnId("INSERT INTO CHEQUES_PRODUCTS (CHEQUE_ID, PRODUCT_ID, AMOUNT, PRICE) SELECT c.ID, p.ID, 12 , 2.2 FROM CHEQUES c JOIN PRODUCTS p");

		 mockedRequest = Mockito.mock(HttpServletRequest.class);
		 HttpSession sessionMock =(HttpSession)Mockito.mock(HttpSession.class);
		 Mockito.when(mockedRequest.getSession()).thenReturn(sessionMock);
		 User userMock =(User)Mockito.mock(User.class);
		 Mockito.when(sessionMock.getAttribute("user")).thenReturn(userMock);
		 Mockito.when(userMock.getUserId()).thenReturn(defaultUserCashierId);
		 //Mockito.when(mockedRequest.getAttribute("chequeId")).thenReturn(defaultChequeId);
		 mockRoles = new ArrayList<>();
		 Mockito.when(userMock.getRoles()).thenReturn(mockRoles);
		 Mockito.when(mockedRequest.getAttribute("limit")).thenReturn(Integer.MAX_VALUE);
		 Mockito.when(mockedRequest.getAttribute("page")).thenReturn(1);
		 mockedAttributes = new HashMap<String, Object>();   
		 Mockito.doAnswer(new Answer<Void>() {
			    @Override
			    public Void answer(InvocationOnMock invocation) throws Throwable {
			        String key = invocation.getArgumentAt(0, String.class);
			        Object value = invocation.getArgumentAt(1, Object.class);
			        mockedAttributes.put(key, value);
			        return null;
			    }
			}).when(mockedRequest).setAttribute(Mockito.anyString(), Mockito.anyObject());
		 Mockito.doAnswer(new Answer<Object>() {
			    @Override
			    public Object answer(InvocationOnMock invocation) throws Throwable {
			        String key = invocation.getArgumentAt(0, String.class);
			        Object value = mockedAttributes.get(key);
			        return value;
			    }
			}).when(mockedRequest).getAttribute(Mockito.anyString());
		 mockedResponse = Mockito.mock(HttpServletResponse.class);
	}
	
	private Integer procedeInsertAndReturnId(String sql) throws SQLException {
		int id;
		try (Connection con = connectionProvider.getConnection(); 
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (!rs.next()) {
					return null;
				}
				id = rs.getInt(1);
			}
			
		}
		return id;
	}
}
