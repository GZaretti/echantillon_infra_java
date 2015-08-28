package article.datasource.rest;

import gza.article.datasource.MapperCommand;
import gza.article.datasource.MapperManager;
import gza.article.datasource.PersistenceException;
import gza.article.datasource.rest.RestMapperManager;
import org.junit.Test;

public class DatabaseSetupTest {
    private final RestMapperManager mapperManager;
    
    public DatabaseSetupTest() {
                mapperManager = TestMapperFactory.createMapperManager();

    }

    @Test
    public void testCreateDrop() throws PersistenceException {
        mapperManager.transactionAndClose(new MapperCommand(){
            @Override
            public Object execute(MapperManager daoManager) throws PersistenceException {
                ((RestMapperManager)daoManager).getDatabaseSetup().drop();
                return null;
            }
        });

        mapperManager.transactionAndClose(new MapperCommand(){
            @Override
            public Object execute(MapperManager daoManager) throws PersistenceException {
                ((RestMapperManager)daoManager).getDatabaseSetup().create();
                return null;
            }
        });
        
        
    }
    
}
