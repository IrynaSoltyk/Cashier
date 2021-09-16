package com.cashier.dao;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cashier.models.User;
import com.cashier.service.ConnectionPool;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Role;

public class UserDao {
	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

	public static User getUser(String inputLogin) throws UnsuccessfulRequestException {
		String sql = "SELECT u.ID, u.NAME, u.LOGIN, u.PASSWORD, r.ROLE FROM USERS AS u "
				+ "LEFT JOIN ROLES as r ON u.ID = r.USER_ID "
				+ "WHERE u.LOGIN = ?";
		ConnectionPool cp = ConnectionPool.getInstance();
		User result = new User();
		List<Role> roles = null;
		try (Connection con = cp.getConnection(); 
				PreparedStatement st = con.prepareStatement(sql)) {
			st.setString(1, inputLogin);
			try (ResultSet rs = st.executeQuery()) {
				
				int i = 0;
				while(rs.next()) {
				if (i++ == 0) {	
					int id = rs.getInt("ID");
					String name = rs.getString("NAME");
					String login = rs.getString("LOGIN");
					String password = rs.getString("PASSWORD");
					
					result.setUserId(id);
					result.setName(name);
					result.setLogin(login);
					result.setPassword(password);
					System.out.println(result.getPassword()+"||");
					roles = new ArrayList<>();
				}
				Role role = Role.values()[(rs.getInt("ROLE"))-1];
				roles.add(role);
				}
				result.setRoles(roles);
			}
			
			
		} catch (SQLException e) {
			logger.error("Failed to get user", e);
			throw new UnsuccessfulRequestException("Unsuccessfull 'Get user' request", e.getCause());
		}

		return result;
	}
	
}
