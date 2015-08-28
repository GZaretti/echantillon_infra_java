package article.datasource.rest;

import gza.article.datasource.rest.RestMapperManager;

public class TestMapperFactory {
    public static RestMapperManager createMapperManager(){
        return new RestMapperManager("http://localhost:8080/article.rest/v1/resources");
    }
}
