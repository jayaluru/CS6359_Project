package db.services.impl;

import db.dao.impl.CraftDaoImpl;
import db.services.CraftPersistenceService;
import domain.product.Craft;

public class CraftPersistenceServiceImpl extends AbstractProductCategoryPersistenceService<Craft> implements CraftPersistenceService {
	
	public CraftPersistenceServiceImpl() {
		super(new CraftDaoImpl());
	}
		
}
