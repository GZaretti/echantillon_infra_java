package gza.article.datasource.rest;

import gza.article.domain.Article;
import gza.article.domain.ArticleBase;
import gza.article.datasource.ArticleMapper;
import gza.article.datasource.EntiteUtiliseePersistenceException;
import gza.article.datasource.PersistenceException;
import gza.article.domain.serialisation.SerialisationException;
import gza.article.domain.serialisation.json.ArticleJson;
import gza.article.domain.serialisation.json.JsonSerialiseur;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;


public class ArticleMapperImpl implements ArticleMapper {

    private final RestMapperManager mapperManager;
    static final Logger logger = Logger.getLogger(ArticleMapperImpl.class.getCanonicalName());

    public static String REST_ENTITES = "%s/articles.json";
    public static String REST_ENTITE = "%s/articles/%d.json";
    public static final String MESSAGE_BUNDLE = "gza/article/persistence/rest/MessagesBundle";

    public static final String RECHERCHE_PARAMETRE = "recherche";

    private final ResourceBundle messages;

    public ArticleMapperImpl(RestMapperManager mapperManager) {
        this.mapperManager = mapperManager;
        this.messages = ResourceBundle.getBundle(MESSAGE_BUNDLE);
    }

    @Override
    public Article create(Article e) throws PersistenceException {
        String targetURL = String.format(REST_ENTITES, mapperManager.getContext());
        Article entite = null;
        try {
            JsonSerialiseur serialiseur = new JsonSerialiseur();

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            serialiseur.serialiser(os, new ArticleJson(null, e));

            HttpPost httpPost = new HttpPost(targetURL);
            ByteArrayEntity requestEntity = new ByteArrayEntity(os.toByteArray(),
                    ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestEntity);

            try (CloseableHttpResponse response = mapperManager.getHttpclient().execute(httpPost)) {

                switch (response.getStatusLine().getStatusCode()) {
                    case HttpStatus.SC_CREATED:
                        HttpEntity responseEntity = response.getEntity();

                        if (!responseEntity.getContentType().getValue()
                                .contains(ContentType.APPLICATION_JSON.getMimeType())) {
                            String msg = String.format(messages.getString("ERREUR_VALEUR_NON_ATTENDU"),
                                    responseEntity.getContentType().getName(),
                                    responseEntity.getContentType().getValue());
                            logger.info(msg);
                            throw new PersistenceException(msg);
                        }

                        InputStream is = responseEntity.getContent();
                        entite = (Article) serialiseur.deserialiser(is, ArticleJson.class);
                        break;

                    default:
                        String msg = String.format(messages.getString("ERREUR_STATUS_NON_ATTENDU"),
                                response.getStatusLine().getStatusCode(),
                                response.getStatusLine().getReasonPhrase());
                        logger.info(msg);
                        throw new PersistenceException(msg);

                }

            }
        } catch (IOException | SerialisationException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);

        }

        return entite;
    }

    @Override
    public Article create() throws PersistenceException {
        return this.create(ArticleBase.builder().build());
    }

    @Override
    public List<Article> retreave(String s) throws PersistenceException {
        List<Article> entites = null;
        String targetURL = String.format(REST_ENTITES, mapperManager.getContext());
        try {
            URI uri = new URIBuilder(targetURL)
                    .setParameter(RECHERCHE_PARAMETRE, s)
                    .build();

            HttpGet httpGet = new HttpGet(uri);

            try (CloseableHttpResponse response = mapperManager.getHttpclient().execute(httpGet)) {

                switch (response.getStatusLine().getStatusCode()) {
                    case HttpStatus.SC_OK:
                        HttpEntity responseEntity = response.getEntity();

                        if (!responseEntity.getContentType().getValue()
                                .contains(ContentType.APPLICATION_JSON.getMimeType())) {
                            String msg = String.format(messages.getString("ERREUR_VALEUR_NON_ATTENDU"),
                                    responseEntity.getContentType().getName(),
                                    responseEntity.getContentType().getValue());
                            logger.info(msg);
                            throw new PersistenceException(msg);
                        }

                        InputStream is = responseEntity.getContent();

                        JsonSerialiseur serialiseur = new JsonSerialiseur();
                        entites = (List<Article>) serialiseur.deserialiser(is,
                                ArticleJson.class, List.class);
                        break;

                    case HttpStatus.SC_NOT_FOUND:
                        entites = null;
                        break;

                    default:
                        String msg = String.format(messages.getString("ERREUR_STATUS_NON_ATTENDU"),
                                response.getStatusLine().getStatusCode(),
                                response.getStatusLine().getReasonPhrase());
                        logger.info(msg);
                        throw new PersistenceException(msg);
                }
            }
        } catch (IOException | SerialisationException | URISyntaxException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }
        return entites;

    }

    @Override
    public Article retreave(Long id) throws PersistenceException {
        Article entite = null;
        if (id != null) {
            String targetURL = String.format(REST_ENTITE, mapperManager.getContext(), id);
            try {

                HttpGet httpGet = new HttpGet(targetURL);

                try (CloseableHttpResponse response = mapperManager.getHttpclient().execute(httpGet)) {
                    switch (response.getStatusLine().getStatusCode()) {
                        case HttpStatus.SC_OK:
                            HttpEntity responseEntity = response.getEntity();

                            if (!responseEntity.getContentType().getValue()
                                    .contains(ContentType.APPLICATION_JSON.getMimeType())) {
                                String msg = String.format(messages.getString("ERREUR_VALEUR_NON_ATTENDU"),
                                        responseEntity.getContentType().getName(),
                                        responseEntity.getContentType().getValue());
                                logger.info(msg);
                                throw new PersistenceException(msg);
                            }

                            InputStream is = responseEntity.getContent();

                            JsonSerialiseur serialiseur = new JsonSerialiseur();
                            entite = (Article) serialiseur.deserialiser(is, ArticleJson.class);
                            break;

                        case HttpStatus.SC_NOT_FOUND:
                            entite = null;
                            break;

                        default:
                            String msg = String.format(messages.getString("ERREUR_STATUS_NON_ATTENDU"),
                                    response.getStatusLine().getStatusCode(),
                                    response.getStatusLine().getReasonPhrase());
                            logger.info(msg);
                            throw new PersistenceException(msg);
                    }
                }
            } catch (IOException | SerialisationException ex) {
                logger.severe(ex.getMessage());
                throw new PersistenceException(ex);
            }
        }
        return entite;
    }

    @Override
    public void update(Article e) throws PersistenceException {
        if (e != null) {
            String targetURL = String.format(REST_ENTITE, mapperManager.getContext(), e.getId());
            try {
                JsonSerialiseur serialiseur = new JsonSerialiseur();

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                serialiseur.serialiser(os, new ArticleJson(null, e));

                HttpPut httpPut = new HttpPut(targetURL);
                ByteArrayEntity requestEntity = new ByteArrayEntity(os.toByteArray(),
                        ContentType.APPLICATION_JSON);

                httpPut.setEntity(requestEntity);

                try (CloseableHttpResponse response = mapperManager.getHttpclient().execute(httpPut)) {

                    switch (response.getStatusLine().getStatusCode()) {
                        case HttpStatus.SC_NO_CONTENT:
                            break;

                        default:
                            String msg = String.format(messages.getString("ERREUR_STATUS_NON_ATTENDU"),
                                    response.getStatusLine().getStatusCode(),
                                    response.getStatusLine().getReasonPhrase());
                            logger.info(msg);
                            throw new PersistenceException(msg);

                    }

                }
            } catch (IOException | SerialisationException ex) {
                logger.severe(ex.getMessage());
                throw new PersistenceException(ex);

            }
        }
    }

    @Override
    public void delete(Article e) throws PersistenceException {
        if (e != null) {
            this.delete(e.getId());
        }
    }

    @Override
    public void delete(Long id) throws PersistenceException {
        if (id != null) {
            String targetURL = String.format(REST_ENTITE, mapperManager.getContext(), id);
            try {

                HttpDelete httpDelete = new HttpDelete(targetURL);

                try (CloseableHttpResponse response = mapperManager.getHttpclient().execute(httpDelete)) {
                    switch (response.getStatusLine().getStatusCode()) {

                        case HttpStatus.SC_OK:
                            break;

                        case HttpStatus.SC_CONFLICT:
                            String msgConflit = String.format(messages.getString("ERREUR_CONFLIT"),
                                    response.getStatusLine().getStatusCode(),
                                    response.getStatusLine().getReasonPhrase());
                            logger.info(msgConflit);
                            throw new EntiteUtiliseePersistenceException(msgConflit);

                        default:
                            String msg = String.format(messages.getString("ERREUR_STATUS_NON_ATTENDU"),
                                    response.getStatusLine().getStatusCode(),
                                    response.getStatusLine().getReasonPhrase());
                            logger.warning(msg);
                    }
                }
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
                throw new PersistenceException(ex);
            }
        }
    }

}
