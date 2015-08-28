package gza.article.rest;

import gza.article.datasource.MapperManager;
import gza.article.datasource.PersistenceException;
import gza.article.datasource.db.DbMapperManager;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MapperManagerFactory {

    private static final Logger logger = Logger.getLogger(MapperManagerFactory.class.getCanonicalName());

    public static MapperManager createMapperManager() throws PersistenceException {
        MapperManager mapperManager;
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context
                    .lookup("java:/comp/env/jdbc/db");

            mapperManager = new DbMapperManager(dataSource);
        } catch (NamingException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }
        return mapperManager;
    }
}
