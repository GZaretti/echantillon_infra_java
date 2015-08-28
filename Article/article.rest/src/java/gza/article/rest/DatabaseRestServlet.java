package gza.article.rest;

import gza.article.datasource.MapperCommand;
import gza.article.datasource.MapperManager;
import gza.article.datasource.PersistenceException;
import gza.article.datasource.db.DbMapperManager;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DatabaseRestServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(DatabaseRestServlet.class.getCanonicalName());
    private final ResourceBundle messages;

    public static final String MESSAGE_BUNDLE = "gza/article/rest/MessagesBundle";

    public DatabaseRestServlet() {
        this.messages = ResourceBundle.getBundle(MESSAGE_BUNDLE);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            DbMapperManager mapperManager = (DbMapperManager) MapperManagerFactory.createMapperManager();

            mapperManager.executeAndClose(new MapperCommand() {

                @Override
                public Object execute(MapperManager mapperManager) throws PersistenceException {
                    ((DbMapperManager) mapperManager).getDatabaseSetup().createTables();
                    ((DbMapperManager) mapperManager).getDatabaseSetup().insertDatas();
                    return null;
                }
            });
        } catch (PersistenceException ex) {
            String msg = messages.getString("ERREUR_DATABASE_CREATE");
            logger.log(Level.SEVERE, msg, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    msg);
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            DbMapperManager mapperManager = (DbMapperManager) MapperManagerFactory.createMapperManager();
            mapperManager.executeAndClose(new MapperCommand() {

                @Override
                public Object execute(MapperManager mapperManager) throws PersistenceException {
                    ((DbMapperManager) mapperManager).getDatabaseSetup().dropTables();
                    return null;
                }
            });
        } catch (PersistenceException ex) {
            String msg = messages.getString("ERREUR_DATABASE_DELETE");

            logger.log(Level.SEVERE, msg, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        }
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
