package gza.article.datasource.db;

public class TestMapperFactory {

    public static DbMapperManager createMapperManager() {
        return new gza.article.datasource.db.DbMapperManager(
                DataSourceFactory.getPostgreSQLDataSource("db.local",
                        5432, "testDB", "test", "testpass"));
    }
}
