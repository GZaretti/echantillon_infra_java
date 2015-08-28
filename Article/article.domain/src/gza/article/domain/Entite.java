package gza.article.domain;

/**
 * @param <T>
 */
public interface Entite<T> {

    Long getId();

    String getDescription();

    Long getVersion();

    void update(T entite);
}
