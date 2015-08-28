package gza.article.rest;

import gza.article.domain.Article;
import gza.article.datasource.MapperCommand;
import gza.article.datasource.MapperManager;
import gza.article.datasource.EntiteUtiliseePersistenceException;
import gza.article.datasource.PersistenceException;
import gza.article.domain.ArticleBase;
import gza.article.domain.serialisation.SerialisationException;
import gza.article.domain.serialisation.json.ArticleJson;
import gza.article.domain.serialisation.json.JsonSerialiseur;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ArticleRestServlet extends RestServlet {

    public static final String ARTICLES_PATTERN = "(.+)/articles(\\.(\\w+))?";
    public static final String ARTICLE_PATTERN = "(.+)/articles/(\\d+)(\\.(\\w+))?";
    public static final String ARTICLE_TEMPLATE = "%s/articles/%d.json";

    public ArticleRestServlet() {
        super();
        this.entitesPattern = Pattern.compile(ARTICLES_PATTERN);
        this.entitePattern = Pattern.compile(ARTICLE_PATTERN);
        this.entiteTemplate = ARTICLE_TEMPLATE;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (etreUrlEntites(request.getRequestURL().toString())) {
            getArticles(request, response);
        } else if (etreUrlEntite(request.getRequestURL().toString())) {
            getArticle(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (etreUrlEntites(request.getRequestURL().toString())) {
            createArticle(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (etreUrlEntite(request.getRequestURL().toString())) {
            deleteArticle(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (etreUrlEntite(request.getRequestURL().toString())) {
            updateArticle(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    private void getArticles(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            MapperManager mm = MapperManagerFactory.createMapperManager();

            final String rechercheFinal = getRechercheParametre(request);

            List<Article> articles = (List<Article>) mm.transactionAndClose(
                    new MapperCommand() {

                        @Override
                        public Object execute(MapperManager mapperManager)
                        throws PersistenceException {
                            return mapperManager.getArticleMapper().retreave(rechercheFinal);
                        }
                    });

            List<ArticleJson> articleJsonList = new ArrayList<>();
            for (Article art : articles) {
                articleJsonList.add(new ArticleJson(getLink(request, art.getId()), art));
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MEDIA_APPLICATION_JSON);
            response.setHeader(HEADER_ATTRIBUT_LOCATION, request.getRequestURL().toString());
            response.setCharacterEncoding(ENCODAGE_UTF8);

            JsonSerialiseur serialiseur = new JsonSerialiseur();
            serialiseur.serialiser(response.getOutputStream(), articleJsonList);

        } catch (PersistenceException | SerialisationException | URISyntaxException ex) {
            String msg = messages.getString("ERREUR_REQUEST");
            logger.log(Level.WARNING, msg, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    msg);
        }
    }

    private void getArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            MapperManager mm = MapperManagerFactory.createMapperManager();

            final Long articleId = getId(request.getRequestURL().toString());
            Article article = (Article) mm.transactionAndClose(
                    new MapperCommand() {

                        @Override
                        public Object execute(MapperManager mapperManager)
                        throws PersistenceException {
                            return mapperManager.getArticleMapper().retreave(articleId);
                        }
                    });

            if (article != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(MEDIA_APPLICATION_JSON);
                response.setHeader(HEADER_ATTRIBUT_LOCATION, request.getRequestURL().toString());
                response.setCharacterEncoding(ENCODAGE_UTF8);

                JsonSerialiseur serialiseur = new JsonSerialiseur();
                serialiseur.serialiser(response.getOutputStream(),
                        new ArticleJson(getLink(request, article.getId()),
                                article));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (PersistenceException | SerialisationException | URISyntaxException ex) {
            String msg = messages.getString("ERREUR_REQUEST");
            logger.log(Level.WARNING, msg, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    msg);
        }
    }

    private void createArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (!request.getContentType().contains(MEDIA_APPLICATION_JSON)) {
            String msg = messages.getString("ERREUR_REQUEST")
                    + "Content-Type: " + request.getContentType();
            logger.log(Level.WARNING, msg);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    msg);
            return;
        }

        try {
            MapperManager mm = MapperManagerFactory.createMapperManager();

            JsonSerialiseur serialiseur = new JsonSerialiseur();
            final ArticleJson articleJson = (ArticleJson) serialiseur.deserialiser(request.getInputStream(),
                    ArticleJson.class);

            Article nouvelleArticle = (Article) mm.transactionAndClose(
                    new MapperCommand() {

                        @Override
                        public Object execute(MapperManager mapperManager) throws PersistenceException {
                            return mapperManager.getArticleMapper().create(articleJson);
                        }
                    });

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType(MEDIA_APPLICATION_JSON);
            response.setHeader(HEADER_ATTRIBUT_LOCATION, String.format(this.entiteTemplate,
                    this.getContext(request.getRequestURL().toString()),
                    nouvelleArticle.getId()));
            response.setCharacterEncoding(ENCODAGE_UTF8);

            serialiseur.serialiser(response.getOutputStream(),new ArticleJson(getLink(request,
                    nouvelleArticle.getId()), nouvelleArticle));

        } catch (PersistenceException | SerialisationException | URISyntaxException ex) {
            String msg = messages.getString("ERREUR_REQUEST");
            logger.log(Level.WARNING, msg, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    msg);
        }
    }

    private void deleteArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            MapperManager mm = MapperManagerFactory.createMapperManager();

            final Long articleId = getId(request.getRequestURL().toString());
            mm.transactionAndClose(
                    new MapperCommand() {

                        @Override
                        public Object execute(MapperManager mapperManager) throws PersistenceException {
                            mapperManager.getArticleMapper().delete(articleId);
                            return null;
                        }
                    });

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (EntiteUtiliseePersistenceException ex) {
            logger.log(Level.WARNING, ex.getMessage());
            response.sendError(HttpServletResponse.SC_CONFLICT,
                    ex.getMessage());
        } catch (PersistenceException ex) {
            String msg = messages.getString("ERREUR_REQUEST");
            logger.log(Level.WARNING, msg, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    msg);
        }
    }

    private void updateArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!request.getContentType().contains(MEDIA_APPLICATION_JSON)) {
            String msg = messages.getString("ERREUR_REQUEST")
                    + "Content-Type: " + request.getContentType();
            logger.log(Level.WARNING, msg);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    msg);
            return;
        }

        try {
            MapperManager mm = MapperManagerFactory.createMapperManager();

            JsonSerialiseur serialiseur = new JsonSerialiseur();

            final Long articleId = getId(request.getRequestURL().toString());

            final ArticleJson articleJson = (ArticleJson) serialiseur.deserialiser(
                    request.getInputStream(),ArticleJson.class);

            final Article article = ArticleBase.builder()
                    .article(articleJson)
                    .id(articleId)
                    .build();

            mm.transactionAndClose(
                    new MapperCommand() {

                        @Override
                        public Object execute(MapperManager mapperManager) throws PersistenceException {
                            mapperManager.getArticleMapper().update(article);
                            return null;
                        }
                    });

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (PersistenceException | SerialisationException ex) {
            String msg = messages.getString("ERREUR_REQUEST");
            logger.log(Level.WARNING, msg, ex);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    msg);
        }
    }

}
