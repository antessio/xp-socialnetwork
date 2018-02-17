package it.antessio.xpsocialnetwork.dao;

import it.antessio.xpsocialnetwork.dao.db.DatabaseUtils;

import it.antessio.xpsocialnetwork.dao.db.DatabaseUtilsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractDAO {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractDAO.class);
    protected DatabaseUtils connectionConfigFactory;

    public AbstractDAO(){
        connectionConfigFactory = DatabaseUtilsFactory.getInstance();
    }
}
