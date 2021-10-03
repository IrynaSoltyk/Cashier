package com.cashier.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.cashier.CashierTestBase;
import com.cashier.controllers.Controller;
import com.cashier.controllers.ControllerResponse;
import com.cashier.dao.ProductDao;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Product;
import com.cashier.models.Role;
import com.cashier.models.Units;

public class TestProductsGetAllController extends CashierTestBase {
	@Test
	public void productsGetAllControllerDoesntGetProductsMarkedDeletedTest() throws UnsuccessfulRequestException, SQLException{
		mockRoles.add(Role.COMMODITY_EXPERT);
		ProductDao dao = new ProductDao(connectionProvider);
		Product product = new Product();
		product.setAmount(22);
		product.setName("neewname");
		product.setUnits(Units.PIECE);
		product.setPrice(BigDecimal.TEN);
		dao.create(product);	
		Product productToDelete = new Product();
		productToDelete.setId(defaultProductId);
		dao.delete(productToDelete);
		Controller controller = new  ProductsGetAllController(connectionProvider);
		
		ControllerResponse controllerResponse = controller.process(mockedRequest, mockedResponse);	

		assertNotNull(controllerResponse);
		assertTrue(mockedAttributes.containsKey("products"));
		List<Product> products = (ArrayList<Product>)mockedAttributes.get("products");
		assertEquals(1, products.size());			
					
	}
}
