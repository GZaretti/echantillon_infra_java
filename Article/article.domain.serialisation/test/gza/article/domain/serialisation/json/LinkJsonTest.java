package gza.article.domain.serialisation.json;

import gza.article.domain.serialisation.SerialisationException;
import gza.article.domain.serialisation.Serialiseur;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LinkJsonTest {

    private LinkJson jsonEntite1;
    private LinkJson jsonEntite2;
    private ArrayList<LinkJson> jsonEntiteList;

    private static final Logger logger = Logger.getLogger(LinkJsonTest.class.getCanonicalName());

    public LinkJsonTest() {
    }

    @Before
    public void setup() throws URISyntaxException {
        jsonEntite1 = new LinkJson(LinkJson.SELF, new URI("http://localhost/article/test.json"));
        jsonEntite2 = new LinkJson("next", new URI("http://localhost/article/test2.json"));

        jsonEntiteList = new ArrayList<>();
        jsonEntiteList.add(jsonEntite1);
        jsonEntiteList.add(jsonEntite2);

    }

    @Test
    public void testSerialiserDeserialiserList() throws SerialisationException {
        String jsonFilename = "testJsonLinkList.json";

        try (OutputStream os = new FileOutputStream(jsonFilename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            serialiseur.serialiser(os, jsonEntiteList);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }

        List<LinkJson> entitesLues;
        try (InputStream is = new FileInputStream(jsonFilename);) {
            Serialiseur serialiseur = new JsonSerialiseur();
            entitesLues = (List<LinkJson>) serialiseur.deserialiser(is, LinkJson.class, List.class);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }
        logger.info(entitesLues.toString());

        Assert.assertArrayEquals(jsonEntiteList.toArray(), entitesLues.toArray());
    }

    @Test
    public void testSerialiserDeserialiser() throws SerialisationException {
        String jsonFilename = "testJsonLink.json";

        try (OutputStream os = new FileOutputStream(jsonFilename)) {
            Serialiseur serialiseur = new JsonSerialiseur();
            serialiseur.serialiser(os, this.jsonEntite1);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }

        LinkJson entiteLue;
        try (InputStream is = new FileInputStream(jsonFilename);) {
            Serialiseur serialiseur = new JsonSerialiseur();
            entiteLue = (LinkJson) serialiseur.deserialiser(is, LinkJson.class);
        } catch (IOException ex) {
            throw new SerialisationException(ex);
        }

        logger.info(entiteLue.toString());

        Assert.assertEquals(this.jsonEntite1, entiteLue);
    }

}
