package gza.article.domain.serialisation;


public class SerialisationException extends Exception {

    public SerialisationException() {
    }

    public SerialisationException(String message) {
        super(message);
    }

    public SerialisationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerialisationException(Throwable cause) {
        super(cause);
    }
    
}
