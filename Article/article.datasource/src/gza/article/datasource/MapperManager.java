package gza.article.datasource;

public interface MapperManager {

    public ArticleMapper getArticleMapper() throws PersistenceException;

    public Object executeAndClose(MapperCommand command) throws PersistenceException;

    public Object transaction(MapperCommand command) throws PersistenceException;

    public Object transactionAndClose(final MapperCommand command) throws PersistenceException;
}
