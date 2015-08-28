package gza.article.datasource;

public interface MapperCommand {

    Object execute(MapperManager mapperManager) throws PersistenceException;
}
