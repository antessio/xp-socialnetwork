package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.dao.db.DatabseUtils;

public abstract class AbstractDAO {
    protected DatabseUtils connectionConfigFactory;

    public AbstractDAO(){
        connectionConfigFactory = new DatabseUtils();
    }
}
