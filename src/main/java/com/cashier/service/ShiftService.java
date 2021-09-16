package com.cashier.service;

import com.cashier.dao.ShiftDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.RequestEntity;
import com.cashier.models.Shift;

public class ShiftService {
	
	public void createShift(Shift shift)  throws UnsuccessfulRequestException {
		ShiftDao.create(shift);
	}
	
	public void closeShift(int id)  throws UnsuccessfulRequestException {
		ShiftDao.closeShift(id);
	}
	
	public Shift get(int id) throws UnsuccessfulRequestException {
		return ShiftDao.getShift(id);
	}
	
	public int getCurrentShiftId() throws UnsuccessfulRequestException {
		return ShiftDao.getCurrentShiftId();
	}
	
	public RequestEntity getAllShifts(int page, int limit) throws UnsuccessfulRequestException {
		return ShiftDao.getAllShifts(limit, page * limit);
	}
}
