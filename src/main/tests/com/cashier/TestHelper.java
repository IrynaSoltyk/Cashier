

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class TestHelper {
	    private static final String URL_CONNECTION = "jdbc:mysql:~/cashier_test_database;user=user;password=123;";

	    public static void beforeTest() throws SQLException, ClassNotFoundException {

	        try (Connection con = DriverManager.getConnection(URL_CONNECTION);
		             Statement statement = con.createStatement()) {
		            String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
		                    "  id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
		                    " login VARCHAR(20) NOT NULL, \n" +
		                    " password VARCHAR(20) NOT NULL, \n" +
		                    " name VARCHAR(30) NOT NULL, \n" +
		                    "  PRIMARY KEY (id));";
		            statement.executeUpdate(sql);
		            
		            sql = "CREATE TABLE IF NOT EXISTS roles (\n" +
		                    "  id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
		                    " user_id INTEGER(11) NOT NULL, \n" +
		                    " role INTEGER(11) NOT NULL, \n" +
		                    "  PRIMARY KEY (id));";
		           statement.executeUpdate(sql);
		           
		            sql = "CREATE TABLE IF NOT EXISTS products (\n" +
		                    " id INTEGER NOT NULL AUTO_INCREMENT,\n" +
		                    " name VARCHAR(30) NOT NULL,\n" +
		                    " units INTEGER NOT NULL, \n" +
		                    " price DECIMAL(19,2) NOT NULL, \n" +
		                    " amount INTEGER NOT NULL, \n" +
		                    "  PRIMARY KEY (id));";
		            statement.executeUpdate(sql);
		            
		           sql = "CREATE TABLE IF NOT EXISTS checks_products (\n" +
		                    " id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
		                    " check_id INTEGER(11) NOT NULL, \n" +
		                    " product_id INTEGER(11) NOT NULL, \n" +
		                    " amount DECIMAL(10) NOT NULL, \n" +
		                    "  PRIMARY KEY (id));";
		            statement.executeUpdate(sql);
		            
		            sql = "CREATE TABLE IF NOT EXISTS shifts (\n" +
		                    " id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
		                    " begin_date DATETIME NOT NULL, \n" +
		                    " end_date DATETIME NOT NULL, \n" +
		                    " user_id INTEGER(11) NOT NULL, \n" +
		                    "  PRIMARY KEY (id));";
		            statement.executeUpdate(sql);
		            
		            sql = "CREATE TABLE IF NOT EXISTS checks (\n" +
		                    " id INTEGER(11) NOT NULL AUTO_INCREMENT,\n" +
		                    " date DATETIME NOT NULL, \n" +
		                    " user_id INTEGER(11) NOT NULL, \n" +
		                    " shift_id INTEGER(11) NOT NULL, \n" +
		                    " cost  DECIMAL(15,2) NOT NULL, \n" +
		                    " canselled_date DATETIME, \n" +
		                    " canselled_by INTEGER(11), \n" +
		                    "  PRIMARY KEY (id));";
		            statement.executeUpdate(sql);
		        }
	    }
	}
