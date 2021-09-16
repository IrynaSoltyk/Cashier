package com.cashier.service;

import com.cashier.dao.ProductDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Product;
import com.cashier.models.RequestEntity;

public class ProductService {
	
	public RequestEntity searchAllProducts(String pattern, int page, int limit) throws UnsuccessfulRequestException {
		return ProductDao.searchAllProducts(pattern, limit, page * limit);
	}
	
	public RequestEntity getAllProducts(int page, int limit) throws UnsuccessfulRequestException {
		return ProductDao.getAll(limit, page * limit);
	}
	
	public void updateProduct(Product product) throws UnsuccessfulRequestException {
		ProductDao.update(product);
	}
	
	public Product getProduct(int id)  throws UnsuccessfulRequestException {
		return ProductDao.get(id);
	}

	public void deleteProduct(Product product)  throws UnsuccessfulRequestException {
		ProductDao.delete(product);
	}
	
	public void createProduct(Product product)  throws UnsuccessfulRequestException {
		ProductDao.create(product);
	}
}
