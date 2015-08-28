package gza.article.datasource;

import gza.article.domain.Entite;
import java.util.List;

/**
 * @param <E>
 */
public interface Mapper<E extends Entite> {
    E create(E e) throws PersistenceException;

    E create() throws PersistenceException;

    List<E> retreave(String s) throws PersistenceException;

    E retreave(Long id) throws PersistenceException;

    void update(E e) throws PersistenceException;

    void delete(E e) throws PersistenceException;
    void delete(Long id) throws PersistenceException;
    
}
