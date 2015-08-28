package gza.article.datasource.db;

import gza.article.datasource.Mapper;
import gza.article.datasource.PersistenceException;
import gza.article.domain.Entite;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * @param <E>
 */
public abstract class EntiteMapperImpl<E extends Entite> implements Mapper<E> {

    protected static final Logger logger = Logger.getLogger(EntiteMapperImpl.class.getCanonicalName());
    public static final String MESSAGE_BUNDLE = "gza/article/datasource/MessagesBundle";

    protected final ResourceBundle messages;
    protected final DbMapperManager mapperManager;
    protected final Class entityClazz;

    protected String sqlSelectMaxStr;
    protected String sqlSelectByIdStr;
    protected String sqlSelectByIdForUpdateStr;
    protected String sqlDeleteStr;

    public EntiteMapperImpl(Class entityClazz, DbMapperManager manager) {
        this.messages = ResourceBundle.getBundle(MESSAGE_BUNDLE);
        this.mapperManager = manager;
        this.entityClazz = entityClazz;
    }

    protected Long getNewId() throws PersistenceException {
        Long id;
        try {
            Statement statement = this.mapperManager.getConnection().createStatement();
            try (final ResultSet result = statement.executeQuery(sqlSelectMaxStr)) {
                result.next();
                id = result.getLong(1) + 1;
            }
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }
        return id;
    }

    @Override
    public E retreave(Long id) throws PersistenceException {
        E entite = retreaveEntite(sqlSelectByIdStr, id);
        return entite;
    }

    protected E retreaveForUpdate(Long id) throws PersistenceException {
        E entite = retreaveEntite(sqlSelectByIdForUpdateStr, id);
        return entite;
    }

    protected E retreaveEntite(String query, Long id) throws PersistenceException {
        logger.info(String.format("id = %d", id));

        E entite = null;
        try {
            if (id != null) {
                PreparedStatement statement = this.mapperManager.getConnection()
                        .prepareStatement(query);
                statement.setLong(1, id);

                try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        entite = createEntite(result);
                    }
                }
            }
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }

        return entite;
    }

    protected abstract E createEntite(ResultSet result) throws PersistenceException;

    @Override
    public void delete(E e) throws PersistenceException {
        delete(e.getId());
    }

    @Override
    public void delete(Long id) throws PersistenceException {
        logger.info(id.toString());

        E entite = this.retreaveForUpdate(id);
        if (entite != null) {
            verifierEntiteUtilisee(id);

            try {
                PreparedStatement statement = this.mapperManager.getConnection().prepareStatement(sqlDeleteStr);
                statement.setLong(1, entite.getId());
                statement.executeUpdate();
            } catch (SQLException ex) {
                logger.severe(ex.getMessage());
                throw new PersistenceException(ex);
            }
        }
    }

    protected abstract void verifierEntiteUtilisee(Long id) throws PersistenceException;

}
