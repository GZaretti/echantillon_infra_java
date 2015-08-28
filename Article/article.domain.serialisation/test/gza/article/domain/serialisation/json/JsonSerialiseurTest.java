package gza.article.domain.serialisation.json;

import gza.article.domain.Article;
import gza.article.domain.ArticleBase;
import gza.article.domain.serialisation.SerialisationException;
import gza.article.domain.serialisation.Serialiseur;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class JsonSerialiseurTest {

    private static final Logger logger = Logger.getLogger(ArticleJsonTest.class.getCanonicalName());

    private Article entite1;
    private Article entite2;
    private List<ArticleJson> jsonEntiteList;
    private ArticleJson jsonEntite1;
    private ArticleJson jsonEntite2;

    public JsonSerialiseurTest() {
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
                .prixVente(new BigDecimal(29)).build();

        jsonEntite1 = new ArticleJson(new LinkJson(LinkJson.SELF,
                new URI(String.format("http://localhost/article/articles/%d.json", entite1.getId()))), entite1);

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
                new URI(String.format("http://localhost/article/articles/%d.json", entite2.getId()))), entite2);

        jsonEntiteList = new ArrayList<>();
        jsonEntiteList.add(jsonEntite1);
        jsonEntiteList.add(jsonEntite2);

    }

    @Test
    public void testSerialiserDeserialiserObjet() throws SerialisationException, IOException {
        String filename = "test.json";

        try (OutputStream os = new FileOutputStream(filename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            serialiseur.serialiser(os, jsonEntite2);
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

        Assert.assertEquals(jsonEntite2, entiteLue);
    }

    @Test
    public void testSerialiserDeserialiserObjetV2() throws SerialisationException, IOException {
        String filename = "testObjet.json";

        try (OutputStream os = new FileOutputStream(filename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            serialiseur.serialiser(os, jsonEntite2);
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

        Assert.assertEquals(jsonEntite2, entiteLue);
    }

    @Test
    public void testSerialiserDeserialiserObjetList() throws SerialisationException, IOException {
        String filename = "testList.json";

        try (OutputStream os = new FileOutputStream(filename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            serialiseur.serialiser(os, jsonEntiteList);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }

        List<ArticleJson> entiteLue;
        try (InputStream is = new FileInputStream(filename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            entiteLue = (List<ArticleJson>) serialiseur.deserialiser(is, ArticleJson.class, List.class);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }
        logger.info(entiteLue.toString());

        Assert.assertArrayEquals(jsonEntiteList.toArray(), entiteLue.toArray());
    }
}
