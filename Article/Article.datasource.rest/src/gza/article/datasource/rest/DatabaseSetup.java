package gza.article.datasource.rest;

import gza.article.datasource.PersistenceException;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;

public class DatabaseSetup {

    private final RestMapperManager daoManager;
    static final Logger logger = Logger.getLogger(DatabaseSetup.class.getCanonicalName());

    public static String REST_DATABASE = "%s/database";
    public static final String MESSAGE_BUNDLE = "gza/article/persistence/rest/MessagesBundle";
    private final ResourceBundle messages;

    public DatabaseSetup(RestMapperManager daoManager) {
        this.daoManager = daoManager;
        this.messages = ResourceBundle.getBundle(MESSAGE_BUNDLE);
    }

    public void create() throws PersistenceException {
        String targetURL = String.format(REST_DATABASE, daoManager.getContext());

        HttpPost httppost = new HttpPost(targetURL);
        try (CloseableHttpResponse response = daoManager.getHttpclient().execute(httppost)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
                logger.info(String.format("status - code: %1$d, message: %2$s",
                        response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()));
                throw new PersistenceException(String.format(messages.getString("ERREUR_CREATION_DATABASE"), 
                        response.getStatusLine().getStatusCode()));
            }
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);

        }
    }

    public void drop() throws PersistenceException {
        String targetURL = String.format(REST_DATABASE, daoManager.getContext());

        HttpDelete httpget = new HttpDelete(targetURL);
        try (CloseableHttpResponse response = daoManager.getHttpclient().execute(httpget)) {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.info(String.format("status - code: %1$d, message: %2$s",
                        response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()));
                throw new PersistenceException(String.format(messages.getString("ERREUR_SUPPRESSION_DATABASE"), 
                        response.getStatusLine().getStatusCode()));
            }
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);

        }
    }

}
