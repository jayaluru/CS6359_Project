package db.services.impl;

import db.dao.impl.CraftDaoImpl;
import db.services.CraftPersistenceService;
import domain.product.Craft;

public class CraftPersistenceServiceImpl extends AbstractProductCategoryPersistenceService<Craft> implements CraftPersistenceService {
	
	public static CraftPersistenceService instance;

	private CraftPersistenceServiceImpl() {
		super(CraftDaoImpl.getInstance());
	}
	
	public static CraftPersistenceService getInstance() {
		if (instance == null) {
			instance = new CraftPersistenceServiceImpl();
		}
		return instance;
	}
}
