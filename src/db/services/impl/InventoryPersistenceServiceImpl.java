package db.services.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import db.DbManager;
import db.dao.CategoryDao;
import db.dao.DaoException;
import db.dao.InventoryDao;
import db.dao.ProductDao;
import db.dao.impl.CategoryDaoImpl;
import db.dao.impl.InventoryDaoImpl;
import db.dao.impl.ProductDaoImpl;
import db.services.InventoryPersistenceService;
import domain.product.Category;
import domain.product.Product;
import domain.user.Inventory;

public class InventoryPersistenceServiceImpl implements InventoryPersistenceService {

	private DbManager db = new DbManager();
	private InventoryDao inventoryDao = new InventoryDaoImpl();
	private ProductDao prodDao = new ProductDaoImpl();
	private CategoryDao catDao = new CategoryDaoImpl();

	@Override
	public Inventory retrieve(Integer userId) throws SQLException, DaoException {
		Connection connection = db.getConnection();

		try {
			connection.setAutoCommit(false);
			Inventory invn = inventoryDao.retrieveByUser(connection, userId);
			List<Product> prods = prodDao.retrieveByInventory(connection, invn.getInvnId());
			for (Product prod : prods) {
				Category prodCat = catDao.retrieveByProduct(connection, prod.getProdId());
				prod.setCategory(prodCat);
			}
			invn.setProducts(prods);
			connection.commit();
			return invn;
		} catch (Exception ex) {
			connection.rollback();
			throw ex;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
				if (!connection.isClosed()) {
					connection.close();
				}
			}
		}
	}

}
