package com.cashier.service;

import com.cashier.dao.ChequeProductDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.ChequeProduct;

public class ChequeProductService {

	public void insertIntoCheque(ChequeProduct product) throws UnsuccessfulRequestException{
		ChequeProductDao.insertProduct(product);
	}
	
	public void updateProductInCheque(ChequeProduct product) throws UnsuccessfulRequestException{
		ChequeProductDao.updateProduct(product);
	}
	
	public void deleteProductFromCheque(int cpId) throws UnsuccessfulRequestException{
		ChequeProductDao.deleteProduct(cpId);
	}
}
