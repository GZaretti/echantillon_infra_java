package gza.article.datasource.rest;

import gza.article.datasource.ArticleMapper;
import gza.article.datasource.MapperCommand;
import gza.article.datasource.MapperManager;
import gza.article.datasource.PersistenceException;
import java.io.IOException;
import java.util.logging.Logger;
import org.apache.http.impl.client.CloseableHttpClient;

public class RestMapperManager implements MapperManager {

    private ArticleMapperImpl articleMapper;
    private DatabaseSetup databaseSetup;
    private final String context;
    private CloseableHttpClient httpClient;

    static final Logger logger = Logger.getLogger(RestMapperManager.class.getCanonicalName());
   

    public RestMapperManager(String applicationContext) {
        this.context = applicationContext;
    }

    @Override
    public ArticleMapper getArticleMapper() throws PersistenceException {
        if (this.articleMapper == null) {
            this.articleMapper = new ArticleMapperImpl(this);
        }
        return this.articleMapper;
    }

    @Override
    public Object executeAndClose(MapperCommand command) throws PersistenceException {
        Object result = null;
        try {
            result = command.execute(this);
        } finally {
            closeHttpClient();
        }
        return result;
    }

    @Override
    public Object transaction(MapperCommand command) throws PersistenceException {
        return command.execute(this);
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

    public DatabaseSetup getDatabaseSetup() {
        if (this.databaseSetup == null) {
            this.databaseSetup = new DatabaseSetup(this);
        }
        return this.databaseSetup;
    }

    public String getContext() {
        return this.context;
    }

    public CloseableHttpClient getHttpclient() {
        if (this.httpClient == null) {
            this.httpClient = HttpClientsFactory.getHttpClient();
        }
        return this.httpClient;
    }

    private void closeHttpClient() throws PersistenceException {
        try {
            this.getHttpclient().close();
            this.httpClient = null;
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }
    }

}
