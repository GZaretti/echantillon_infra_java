package gza.article.domain.serialisation.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import gza.article.domain.serialisation.SerialisationException;
import gza.article.domain.serialisation.Serialiseur;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public class JsonSerialiseur implements Serialiseur {

    private InputStream inputStream;
    private OutputStream outputStream;
    private static final Logger logger
            = Logger.getLogger(JsonSerialiseur.class.getCanonicalName());
    private final ObjectMapper mapper;

    public JsonSerialiseur() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Deprecated
    public JsonSerialiseur(InputStream inputStream, OutputStream outputStream) {
        this();
        this.inputStream = inputStream;
        this.outputStream = outputStream;

    }

    @Override
    @Deprecated
    public void serialiser(Object objet) throws SerialisationException {
        this.serialiser(outputStream, objet);
    }

    @Override
    public void serialiser(OutputStream os, Object object) throws SerialisationException {
        if (os == null) {
            throw new SerialisationException("Erreur: outputStream est null!");
        }
        try {
            this.mapper.writeValue(os, object);

        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            throw new SerialisationException(ex);
        }
    }

    @Override
    @Deprecated
    public Object deserialiser(Class objectClazz) throws SerialisationException {
        return deserialiser(this.inputStream, objectClazz);
    }

    @Override
    @Deprecated
    public Object deserialiser(Class objectClazz, Class listClazz) throws SerialisationException {
        return deserialiser(this.inputStream, objectClazz, listClazz);
    }

    @Override
    public Object deserialiser(InputStream is, Class objectClazz, Class listClazz)
            throws SerialisationException {
        Object object;
        if (is == null) {
            throw new SerialisationException("Erreur: inputStream est null!");
        }
        try {
            TypeFactory t = mapper.getTypeFactory();
            if (listClazz != null) {
                object = mapper.readValue(is, t.constructCollectionType(listClazz, objectClazz));
            } else {
                object = mapper.readValue(is, t.constructType(objectClazz));
            }
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            throw new SerialisationException(ex);
        }
        return object;
    }

    @Override
    public Object deserialiser(InputStream is, Class objectClazz) throws SerialisationException {
        return deserialiser(is, objectClazz, null);
    }
}
