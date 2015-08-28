package gza.article.datasource;

public class EntiteInconnuePersistenceException extends PersistenceException {

    public EntiteInconnuePersistenceException() {
    }

    public EntiteInconnuePersistenceException(String message) {
        super(message);
    }

    public EntiteInconnuePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntiteInconnuePersistenceException(Throwable cause) {
        super(cause);
    }

    public EntiteInconnuePersistenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
