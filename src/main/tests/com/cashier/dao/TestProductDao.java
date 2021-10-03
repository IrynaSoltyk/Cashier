package com.cashier.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cashier.CashierTestBase;
import com.cashier.MockConnectionProvider;
import com.cashier.TestHelper;
import com.cashier.exeptions.UnsuccessfulRequestException;
import com.cashier.models.Product;

import com.cashier.models.Units;

public class TestProductDao  extends CashierTestBase {

	@Test
	public void createProductShouldCreateUserInDatabase() throws UnsuccessfulRequestException, SQLException {
		ProductDao dao = new ProductDao(connectionProvider);
		Product product = createOneProduct("name", Units.LITRE, BigDecimal.valueOf(1.2), 11);
		dao.create(product);
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM PRODUCTS WHERE ID="+product.getId())) {
			if (rs.next()) {
				assertEquals(product.getName(), rs.getString("NAME"));
				assertEquals(product.getUnits(), Units.values()[rs.getInt("UNITS")-1]);
				assertEquals(product.getPrice(), rs.getBigDecimal("PRICE"));
				assertEquals(product.getAmount(), rs.getInt("AMOUNT"));
			} else {
				fail("No products in database");
			}

		}
	}

	private Product createOneProduct(String name, Units units, BigDecimal price, int amount) {
		Product product = new Product();
		product.setName(name);
		product.setUnits(units);
		product.setPrice(price);
		product.setAmount(amount);
		return product;
	}

	@Test
	public void searchAllProductsShouldSearchByIdAndByName() throws UnsuccessfulRequestException, SQLException {
		ProductDao dao = new ProductDao(connectionProvider);
		List<Product> products = createThreeProducts();
		for (Product p : products) {
			dao.create(p);
		}
		int idToSearch = products.get(0).getId();
		products.get(1).setName("testsearch" + idToSearch);
		dao.update(products.get(1));

		List<Product> actual = (List<Product>) dao.searchAllProducts(String.valueOf(idToSearch), 5, 0).getObjects();
		List<Product> expected = null;
		String searchPattern = String.valueOf(idToSearch);
		String sqlPattern = "%" + idToSearch + "%";
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement st = con.prepareStatement(
						"SELECT ID FROM PRODUCTS WHERE DELETED = FALSE AND NAME LIKE ? OR CAST(ID AS CHAR(20)) LIKE ?")) {
			int k = 0;
			st.setString(++k, sqlPattern);
			st.setString(++k, sqlPattern);
			try (ResultSet rs = st.executeQuery()) {
				expected = new ArrayList<>();
				while (rs.next()) {
					Product p = new Product();
					p.setId(rs.getInt("ID"));
					expected.add(p);
				}
			}
		}
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		assertTrue(actual.stream()
				.filter(p -> p.getName().contains(searchPattern) | String.valueOf(p.getId()).contains(searchPattern))
				.allMatch(p -> p.getName().equals("name") | p.getName().equals("testsearch" + idToSearch)));

	}

	private List<Product> createThreeProducts() {
		List<Product> products = new ArrayList<>();
		Product product = new Product();
		product.setName("name");
		product.setUnits(Units.KG);
		product.setPrice(BigDecimal.valueOf(1.2));
		product.setAmount(11);
		products.add(product);

		Product product1 = new Product();
		product1.setName("2name1");
		product1.setUnits(Units.LITRE);
		product1.setPrice(BigDecimal.valueOf(1.2));
		product1.setAmount(11);
		products.add(product1);

		Product product2 = new Product();
		product2.setName("name55");
		product2.setUnits(Units.LITRE);
		product2.setPrice(BigDecimal.valueOf(1.2));
		product2.setAmount(11);
		products.add(product2);

		return products;
	}

	@Test
	public void getAllProductsShouldGetAllButNotDeleted() throws UnsuccessfulRequestException, SQLException {
		ProductDao dao = new ProductDao(connectionProvider);
		List<Product> products = createThreeProducts();
		for (Product p : products) {
			dao.create(p);
		}

		List<Product> actual = (List<Product>) dao.getAll(Integer.MAX_VALUE, 0).getObjects();
		List<Product> expected = null;
		try (Connection con = connectionProvider.getConnection();
				Statement st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT ID FROM PRODUCTS WHERE DELETED = FALSE")) {
			expected = new ArrayList<>();
			while (rs.next()) {
				Product p = new Product();
				p.setId(rs.getInt("ID"));
				expected.add(p);
			}

		}
		assertNotNull(actual);
		assertEquals(expected.size(), actual.size());
		assertTrue(actual.stream().allMatch(p -> !p.isDeleted()));
	}

	@Test
	public void getShouldGetFromDatabase() throws UnsuccessfulRequestException, SQLException {
		ProductDao dao = new ProductDao(connectionProvider);
		Product inputProduct = createOneProduct("name", Units.LITRE, BigDecimal.valueOf(1.2), 11);
		dao.create(inputProduct);
		Product actual = dao.get(inputProduct.getId());
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement st = con.prepareStatement("SELECT * FROM PRODUCTS WHERE ID = ?")){
				st.setInt(1, inputProduct.getId());
				try (ResultSet rs = st.executeQuery()) {
			if (rs.next()) {
				assertEquals(rs.getString("NAME"), actual.getName());
				assertEquals(Units.values()[rs.getInt("UNITS") - 1], actual.getUnits());
				assertEquals(rs.getBigDecimal("PRICE"), actual.getPrice());
				assertEquals(rs.getInt("AMOUNT"), actual.getAmount());
			} else {
				fail("No products in database");
			}
				}
		}

	}

	@Test
	public void updateShouldUpdate() throws UnsuccessfulRequestException, SQLException {
		ProductDao dao = new ProductDao(connectionProvider);
		Product inputProduct = createOneProduct("name", Units.LITRE, BigDecimal.valueOf(1.2), 11);
		dao.create(inputProduct);
		
		Product updateProduct = createOneProduct("namechanged", Units.LITRE, BigDecimal.valueOf(1.4), 12);
		updateProduct.setId(inputProduct.getId());
		dao.update(updateProduct);
		
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement st = con.prepareStatement("SELECT * FROM PRODUCTS WHERE ID = ?")) {
			st.setInt(1, inputProduct.getId());
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					assertEquals(updateProduct.getName(), rs.getString("NAME"));
					assertEquals(updateProduct.getAmount(), rs.getInt("AMOUNT"));
					assertEquals(updateProduct.getPrice(), rs.getBigDecimal("PRICE"));
				} else {
					fail("No products in database");
				}

			}
		}
	}
	
	@Test
	public void deleteShouldDeleteIfProductIsNotInCheque() throws UnsuccessfulRequestException, SQLException {
		ProductDao dao = new ProductDao(connectionProvider);
		Product inputProduct = createOneProduct("name", Units.LITRE, BigDecimal.valueOf(1.2), 11);
		dao.create(inputProduct);
		
		dao.delete(inputProduct);
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement st = con.prepareStatement("SELECT * FROM PRODUCTS WHERE ID = ?")) {
			st.setInt(1, inputProduct.getId());	
			System.out.println(inputProduct.getId());
			try (ResultSet rs = st.executeQuery()) {
				if(rs.next()) {
					fail("There should be empty result set");
				}
			}
		}
	}
		
	@Test
	public void deleteShouldMarkDeleteIfProductIsInCheque() throws UnsuccessfulRequestException, SQLException {
		ProductDao dao = new ProductDao(connectionProvider);
		Product p = new Product();
		p.setId(defaultProductId);
		dao.delete(p);
		Product actual;
		try (Connection con = connectionProvider.getConnection();
				PreparedStatement st = con.prepareStatement("SELECT * FROM PRODUCTS WHERE ID = ?")) {
			st.setInt(1, defaultProductId);		
			try (ResultSet rs = st.executeQuery()) {
				if(!rs.next()) {
					fail("There should be product, marked 'deleted'");
				}
				actual = new Product();
				actual.setId(rs.getInt("ID"));
				actual.setDeleted(rs.getBoolean("DELETED"));
			}
			
		}
		assertNotNull(actual);
		assertEquals(defaultProductId, actual.getId());
		assertTrue(actual.isDeleted());
		
	}
}
