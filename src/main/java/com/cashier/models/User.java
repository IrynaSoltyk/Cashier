package com.cashier.models;

import java.util.List;

public class User {

	private int userId;
	private String name;
	private String login;
	private String password;
	private List<Role> roles;

	public int getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}
	
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setName(String username) {
		this.name = username;
	}


	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
