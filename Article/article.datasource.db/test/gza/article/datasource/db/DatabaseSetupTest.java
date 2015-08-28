package gza.article.datasource.db;

import gza.article.domain.Article;
import gza.article.datasource.MapperCommand;
import gza.article.datasource.MapperManager;
import gza.article.datasource.PersistenceException;
import java.util.List;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabaseSetupTest {

    private final DbMapperManager mapperManager;

    public static final Logger logger = Logger.getLogger(DatabaseSetupTest.class.getCanonicalName());

    public DatabaseSetupTest() {
        mapperManager = TestMapperFactory.createMapperManager();
    }

    @Before
    public void setup() throws Exception {
        mapperManager.executeAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                try {
                    ((DbMapperManager) mapperManager).getDatabaseSetup().dropTables();
                } catch (Exception ex) {
                    //ne fait rien
                }
                ((DbMapperManager) mapperManager).getDatabaseSetup().createTables();
                return null;
            }
        });

    }

    //@After
    public void tearDown() throws Exception {
        mapperManager.executeAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                ((DbMapperManager) mapperManager).getDatabaseSetup().dropTables();
                return null;
            }
        });

    }

    @Test
    public void testInsertDatas() throws Exception {
        mapperManager.transactionAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                ((DbMapperManager) mapperManager).getDatabaseSetup().insertDatas();
                return null;
            }
        });

        List<Article> articles = (List<Article>) mapperManager.transaction(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return ((DbMapperManager) mapperManager).getArticleMapper().retreave("");
            }
        });

        Assert.assertTrue(articles.size() > 0);
        logger.info(articles.toString());
    }
}
