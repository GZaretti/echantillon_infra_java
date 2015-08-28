package gza.article.datasource.db;

import javax.sql.DataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.postgresql.ds.PGPoolingDataSource;

public class DataSourceFactory {

    public static DataSource getHsqldbDataSource(String url,
            String user, String password) {
        
        JDBCDataSource ds = new JDBCDataSource();
        ds.setUrl(url);
        ds.setUser(user);
        ds.setPassword(password);
        
        return ds;
    }

    public static DataSource getPostgreSQLDataSource(String serverName,
            int portNumber, String databaseName, String user, String password) {

        PGPoolingDataSource ds = new PGPoolingDataSource();
        ds.setServerName(serverName);
        ds.setPortNumber(portNumber);
        ds.setDatabaseName(databaseName);
        ds.setUser(user);
        ds.setPassword(password);
        ds.setMaxConnections(100);
        
        return ds;
    }
}
