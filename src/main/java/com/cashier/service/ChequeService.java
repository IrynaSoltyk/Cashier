package com.cashier.service;

import com.cashier.dao.ChequeDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Cheque;
import com.cashier.models.RequestEntity;

public class ChequeService {
	
	public RequestEntity getAllCheques(int page, int limit) throws UnsuccessfulRequestException{
		return ChequeDao.getAll(limit, page * limit);
	}
	
	public RequestEntity getAllinShift(int shiftId) throws UnsuccessfulRequestException{
		return ChequeDao.getAllInShift(shiftId);
	}
	
	public RequestEntity getAllInShiftForUser(int shiftId, int userId, int page, int limit) throws UnsuccessfulRequestException{
		return ChequeDao.getAllInShiftForUser(shiftId, userId, limit, page * limit);
	}
	
	public Cheque get(int checkId) throws UnsuccessfulRequestException{
		return ChequeDao.get(checkId);
	}
		
	public Cheque create(Cheque cheque) throws UnsuccessfulRequestException{
			return ChequeDao.create(cheque);
	}
	
	public void deleteCheque(int chequeId) throws UnsuccessfulRequestException{
		ChequeDao.deleteCheque(chequeId);
	}
	
	public void cancelCheque(Cheque cheque) throws UnsuccessfulRequestException{
		ChequeDao.cancelCheque(cheque);
	}
	
	public void closeCheque(int chequeId) throws UnsuccessfulRequestException{
		ChequeDao.closeCheque(chequeId);
	}
}