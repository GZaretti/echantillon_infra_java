package gza.article.domain.serialisation.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gza.article.domain.Article;
import gza.article.domain.ArticleBase;
import gza.article.domain.serialisation.Link;
import gza.article.domain.serialisation.SerialisationException;
import gza.article.domain.serialisation.Serialiseur;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArticleJsonTest {

    private static final Logger logger = Logger.getLogger(ArticleJsonTest.class.getCanonicalName());

    private Article entite1;
    private Article entite2;
    private Article entite11;
    private Article entite1Modifie;
    private Article entiteIdNull;
    private List<ArticleJson> jsonEntiteList;
    private ArticleJson jsonEntite1;
    private ArticleJson jsonEntite2;

    public ArticleJsonTest() {
    }

    @Before
    public void setup() throws URISyntaxException {
        entite1 = ArticleBase.builder()
                .id(1L)
                .nom("AGRAFEUSE")
                .poids(150.0)
                .couleur("ROUGE")
                .qteStock(10)
                .prixAchat(new BigDecimal(20))
                .prixVente(new BigDecimal(29))
                .build();

        jsonEntite1 = new ArticleJson(new LinkJson(LinkJson.SELF,
                new URI(String.format(Link.ARTICLE_URL_TEMPLATE,
                                "http://host/context", entite1.getId()))), entite1);

        entite2 = ArticleBase.builder()
                .id(2L)
                .nom("CALCULATRICE")
                .poids(150.0)
                .couleur("NOIR")
                .qteStock(5)
                .prixAchat(new BigDecimal(200))
                .prixVente(new BigDecimal(235))
                .build();

        jsonEntite2 = new ArticleJson(new LinkJson(LinkJson.SELF,
                new URI(String.format(Link.ARTICLE_URL_TEMPLATE,
                                "http://host/context", entite2.getId()))), entite2);

        jsonEntiteList = new ArrayList<>();
        jsonEntiteList.add(jsonEntite1);
        jsonEntiteList.add(jsonEntite2);

    }

    @Test
    public void testEqualsJsonTO() {
        Assert.assertTrue(entite1.equals(jsonEntite1));
        Assert.assertTrue(jsonEntite1.equals(entite1));
        Assert.assertEquals(entite1, jsonEntite1);
    }

    @Test
    public void testEqualsJsonTOList() {
        List<Article> articles = new ArrayList<>();
        articles.add(entite1);
        articles.add(entite2);
        Assert.assertEquals(jsonEntiteList, articles);
    }

    @Test
    public void testSerialiserDeserialiser() {
        //Serialiser
        StringWriter entiteListWriter = new StringWriter();
        StringWriter entiteWriter = new StringWriter();

        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.writeValue(entiteListWriter, jsonEntiteList);
            mapper.writeValue(entiteWriter, jsonEntite1);

        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            throw new RuntimeException(ex);
        }

        logger.info(entiteListWriter.toString());
        logger.info(entiteWriter.toString());

        //deserialiser
        StringReader entiteListReader = new StringReader(entiteListWriter.toString());
        StringReader entiteReader = new StringReader(entiteWriter.toString());
        List<ArticleJson> entitesLues;
        ArticleJson entiteLue;
        try {
            ObjectMapper mapper = new ObjectMapper();
            entitesLues = mapper.readValue(entiteListReader,
                    new TypeReference<List<ArticleJson>>() {
                    });
            entiteLue = mapper.readValue(entiteReader, ArticleJson.class);

        } catch (IOException ex) {
            logger.severe(ex.getMessage());
            throw new RuntimeException(ex);

        }

        logger.info(entitesLues.toString());
        logger.info(entiteLue.toString());

        Assert.assertArrayEquals(jsonEntiteList.toArray(), entitesLues.toArray());
        Assert.assertEquals(jsonEntite1, entiteLue);

    }

    @Test
    public void testSerialiserDeserialiserListV2() throws SerialisationException {
        String filename = "testJsonArticleList.json";

        try (OutputStream os = new FileOutputStream(filename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            serialiseur.serialiser(os, jsonEntiteList);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }

        List<ArticleJson> entitesLues;
        try (InputStream is = new FileInputStream(filename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            entitesLues = (List<ArticleJson>) serialiseur.deserialiser(is, ArticleJson.class, List.class);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }

        logger.info(jsonEntiteList.toString());
        logger.info(entitesLues.toString());

        Assert.assertEquals(jsonEntiteList, entitesLues);
    }

    @Test
    public void testSerialiserDeserialiserV2() throws SerialisationException {
        String filename = "testJsonArticle.json";

        try (OutputStream os = new FileOutputStream(filename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            serialiseur.serialiser(os, this.jsonEntite1);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }
        ArticleJson entiteLue;
        try (InputStream is = new FileInputStream(filename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            entiteLue = (ArticleJson) serialiseur.deserialiser(is, ArticleJson.class);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }
        logger.info(entiteLue.toString());

        Assert.assertEquals(this.jsonEntite1, entiteLue);
    }

}
