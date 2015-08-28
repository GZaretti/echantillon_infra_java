package gza.article.datasource.db;

import gza.article.datasource.ArticleMapper;
import gza.article.datasource.MapperCommand;
import gza.article.datasource.MapperManager;
import gza.article.datasource.PersistenceException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class DbMapperManager implements MapperManager {

    static final Logger logger = Logger.getLogger(DbMapperManager.class.getCanonicalName());

    private Connection connection;
    private ArticleMapper articleMapper;
    private DatabaseSetup databaseSetup;
    private final DataSource dataSource;

    public DbMapperManager(DataSource dataSource) {
        this.dataSource = dataSource;

    }

    @Override
    public ArticleMapper getArticleMapper() throws PersistenceException {
        if (this.articleMapper == null) {
            this.articleMapper = new ArticleMapperImpl(this);
        }
        return this.articleMapper;
    }
    
    public DatabaseSetup getDatabaseSetup() throws PersistenceException {
        if (this.databaseSetup == null) {
            this.databaseSetup = new DatabaseSetup(this);
        }
        return this.databaseSetup;

    }

    @Override
    public Object executeAndClose(MapperCommand command) throws PersistenceException {
        Object result = null;
        try {
            result = command.execute(this);
        } finally {
            closeConnection();

        }
        return result;
    }

    @Override
    public Object transaction(MapperCommand command) throws PersistenceException {
        Object returnValue = null;
        try {
            if (this.getConnection().getAutoCommit()) {
                this.getConnection().setAutoCommit(false);
            }

            returnValue = command.execute(this);
            this.getConnection().commit();
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            try {
                this.getConnection().rollback();
            } catch (SQLException e) {
                logger.severe(e.getMessage());
            }
            throw new PersistenceException(ex);
        } finally {
            try {
                this.getConnection().setAutoCommit(true);
            } catch (SQLException ex) {
                logger.severe(ex.getMessage());
            }
        }
        return returnValue;
    }

    @Override
    public Object transactionAndClose(final MapperCommand command) throws PersistenceException {
        return executeAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager manager) throws PersistenceException {
                return manager.transaction(command);
            }
        });

    }

    public Connection getConnection() throws PersistenceException {
        try {
            if (this.connection == null) {
                this.connection = dataSource.getConnection();
            }
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }
        return this.connection;
    }

    protected void closeConnection() throws PersistenceException {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
            this.connection = null;
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }

    }


}
