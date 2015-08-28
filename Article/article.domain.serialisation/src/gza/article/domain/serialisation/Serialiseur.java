package gza.article.domain.serialisation;

import java.io.InputStream;
import java.io.OutputStream;

public interface Serialiseur {

    @Deprecated
    void serialiser(Object objet) throws SerialisationException;

    void serialiser(OutputStream os, Object objet) throws SerialisationException;

    @Deprecated
    Object deserialiser(Class objectClazz) throws SerialisationException;

    Object deserialiser(InputStream is, Class objectClazz) throws SerialisationException;

    @Deprecated
    Object deserialiser(Class objectClazz, Class listClazz) throws SerialisationException;

    Object deserialiser(InputStream is, Class objectClazz, Class listClazz) throws SerialisationException;
}
