package com.cashier.service;

import com.cashier.dao.UserDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.User;

public class UserService {
	
 public User getUser(String login) throws UnsuccessfulRequestException {
	 return UserDao.getUser(login);
 }
}
