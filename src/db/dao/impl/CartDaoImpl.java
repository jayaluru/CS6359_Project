package db.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.dao.CartDao;
import db.dao.DaoException;
import domain.product.Product;
import domain.user.Cart;

public class CartDaoImpl implements CartDao {

	private static final String createQuery = 
			"INSERT INTO "
			+ "CART (USERID) "
			+ "VALUES (?) ";
	
	private static final String retrieveQuery = 
			"SELECT "
			+ "CARTID, USERID "
			+ "FROM CART "
			+ "WHERE USERID = ?";
	
	private static final String deleteProductsQuery = 
			"DELETE FROM CARTPRODUCT WHERE CARTID = ? ";
				
	private static final String addProductQuery = 
			"INSERT INTO "
			+ "CARTPRODUCT (CARTID, PRODID) "
			+ "VALUES (?, ?) ";

	private static final String removeProductQuery = 
			"DELETE FROM CARTPRODUCT WHERE CARTID = ? AND PRODID = ? ";
	@Override
	public void create(Connection connection, Cart cart, Integer userId) throws SQLException, DaoException {
		if (cart.getCartId() != null) {
			throw new DaoException("CartId must be null!");
		}
		if (userId == null) {
			throw new DaoException("UserId cannot be null!");
		}
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.prepareStatement(createQuery, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, userId);
			statement.executeUpdate();
			rs = statement.getGeneratedKeys();
			rs.next();
			cart.setCartId(rs.getInt(1));
		} finally {
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
	}

	@Override
	public Cart retrieveByUser(Connection connection, Integer userId) throws SQLException, DaoException {
		if (userId == null) {
			throw new DaoException("UserId cannot be null!");
		}
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			statement = connection.prepareStatement(retrieveQuery);
			statement.setInt(1, userId);
			rs = statement.executeQuery();
			boolean found = rs.next();
			if (!found) {
				return null;
			}
			Cart cart = new Cart();
			cart.setCartId(rs.getInt(1));
			return cart;
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
		}
	}

	public int update(Connection connection, Cart cart) throws SQLException, DaoException {
		if (cart.getCartId() == null) {
			throw new DaoException("CartId cannot be null!");
		}

		PreparedStatement statement = null;
		
		try {
			statement = connection.prepareStatement(deleteProductsQuery);
			statement.setInt(1, cart.getCartId());
			statement.executeUpdate();
		} finally {
			if (statement != null && !statement.isClosed()) {
				statement.close();
			}
		}
		int count = 0;
		for (Product prod : cart.getProducts()) {
			statement = null;
			try {
				statement = connection.prepareStatement(addProductQuery);
				statement.setInt(1, cart.getCartId());
				statement.setInt(2, prod.getProdId());
				statement.executeUpdate();
				count += 1;
			} finally {
				if (statement != null && !statement.isClosed()) {
					statement.close();
				}
			}
		}
		return count;

	}

}
