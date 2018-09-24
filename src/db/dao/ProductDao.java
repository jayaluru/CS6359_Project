package db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


import domain.product.Product;

public interface ProductDao extends ProductCategoryDao<Product> {
	
	public List<Product> retrieveByTransaction(Connection connection, Integer trxnId) throws SQLException, DaoException;
	
	public List<Product> retrieveBySeller(Connection connection, Integer sellerId) throws SQLException, DaoException;
	
}