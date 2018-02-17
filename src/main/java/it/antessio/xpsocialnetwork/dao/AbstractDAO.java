package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.dao.db.DatabseUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDAO {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractDAO.class);
    protected DatabseUtils connectionConfigFactory;

    public AbstractDAO(){
        connectionConfigFactory = new DatabseUtils();
    }
}
