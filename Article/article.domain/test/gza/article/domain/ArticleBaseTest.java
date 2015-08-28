package gza.article.domain;

import java.math.BigDecimal;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;

public class ArticleBaseTest {

    private Article entite1;
    private Article entite11;
    private Article entite1Modifie;
    private Article entiteIdNull;
    private Article entite2;

    private static final Logger LOGGER = Logger.getLogger(ArticleBaseTest.class.getCanonicalName());

    public ArticleBaseTest() {
    }

    @Before
    public void setup() {
        entite1 = ArticleBase.builder()
                .id(1L)
                .nom("AGRAFEUSE")
                .poids(150.0)
                .couleur("ROUGE")
                .qteStock(10)
                .prixAchat(new BigDecimal(20))
                .prixVente(new BigDecimal(29))
                .build();

        LOGGER.finest(entite1.toString());

        entite2 = ArticleBase.builder()
                .id(2L)
                .nom("CALCULATRICE")
                .poids(150.0)
                .couleur("NOIR")
                .qteStock(5)
                .prixAchat(new BigDecimal(200))
                .prixVente(new BigDecimal(235))
                .build();
        LOGGER.finest(entite2.toString());

        entite11 = ArticleBase.builder()
                .id(1L)
                .nom("AGRAFEUSE")
                .poids(150.0)
                .couleur("ROUGE")
                .qteStock(10)
                .prixAchat(new BigDecimal(20))
                .prixVente(new BigDecimal(29))
                .build();
        LOGGER.finest(entite11.toString());

        entite1Modifie = ArticleBase.builder()
                .article(entite1).build();
        entite1Modifie.setNom(entite1Modifie.getCouleur() + "------");
        LOGGER.finest(entite1Modifie.toString());

        entiteIdNull = ArticleBase.builder().build();
        LOGGER.finest(entiteIdNull.toString());

    }

    @Test
    public void testVersion() {
        Assert.assertEquals(new Long(0), entite1.getVersion());

    }

    @Test
    public void testEquals() {
        Assert.assertNotSame(entite1, entite11);
        Assert.assertEquals(entite1, entite11);

    }

    @Test
    public void testUpdate() {
        Article article = ArticleBase.builder()
                .id(1L)
                .nom("***AGRAFEUSE***")
                .couleur("***ROUGE***")
                .poids(150.0)
                .qteStock(10)
                .prixAchat(new BigDecimal(20))
                .prixVente(new BigDecimal(29))
                .build();

        entite1.update(article);

        Assert.assertNotSame(entite1, article);
        Assert.assertEquals(entite1, article);
    }

    @Test
    public void testNotEquals() {
        Assert.assertEquals(entite1.getId(), entite1Modifie.getId());
        Assert.assertFalse(entite1.equals(entite1Modifie));

        Assert.assertFalse(entite1.equals(entiteIdNull));
        Assert.assertFalse(entite1.equals(null));
        Assert.assertFalse(entite1.equals(new Object()));
        Assert.assertFalse(entite1.equals(entite2));
    }

    @Test
    public void testEqualsHashCode() {
        Assert.assertNotSame(entite1, entite11);
        Assert.assertEquals(entite1.hashCode(), entite11.hashCode());
    }

    @Test
    public void testNotEqualsHashCode() {
        Assert.assertEquals(entite1.getId(), entite1Modifie.getId());
        Assert.assertFalse(entite1.hashCode() == entite1Modifie.hashCode());

        Assert.assertFalse(entite1.hashCode() == entiteIdNull.hashCode());
        Assert.assertFalse(entite1.hashCode() == entite2.hashCode());
    }

    @Ignore
    @Test
    public void testSerializable() {
        Assert.fail("Méthode pas encore implémentée!");
    }

    @Test
    public void testBuilder() {
        Article article = ArticleBase.builder()
                .id(1L)
                .nom("AGRAFEUSE")
                .couleur("ROUGE")
                .poids(150.0)
                .qteStock(10)
                .prixAchat(new BigDecimal(20))
                .prixVente(new BigDecimal(29))
                .build();

        Assert.assertEquals(entite1, article);
    }

    @Test
    public void testBuilderNullObject() {
        Article article = ArticleBase.builder()
                .build();

        Assert.assertEquals(entiteIdNull, article);
    }
}
