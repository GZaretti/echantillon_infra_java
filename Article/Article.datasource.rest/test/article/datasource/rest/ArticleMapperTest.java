package article.datasource.rest;

import gza.article.domain.Article;
import gza.article.domain.ArticleBase;
import gza.article.datasource.ArticleMapper;
import gza.article.datasource.MapperCommand;
import gza.article.datasource.MapperManager;
import gza.article.datasource.PersistenceException;
import gza.article.datasource.rest.ArticleMapperImpl;
import gza.article.datasource.rest.RestMapperManager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ArticleMapperTest {

    private final SimpleDateFormat format;

    public ArticleMapperTest() {
        format = new SimpleDateFormat();
        format.applyPattern("yyyy-mm-dd");
    }

    @BeforeClass
    public static void beforeClass() throws PersistenceException {
        RestMapperManager mapperManager = TestMapperFactory.createMapperManager();

        mapperManager.transactionAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                ((RestMapperManager) mapperManager).getDatabaseSetup().drop();
                return null;
            }
        });

        mapperManager.transactionAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                ((RestMapperManager) mapperManager).getDatabaseSetup().create();
                return null;
            }
        });
    }

    public static void afterClass() throws PersistenceException {
        RestMapperManager mapperManager = TestMapperFactory.createMapperManager();

        mapperManager.transactionAndClose(new MapperCommand() {

            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                ((RestMapperManager) mapperManager).getDatabaseSetup().drop();
                return null;
            }
        });
    }

    @Test
    public void testGetInstance() throws PersistenceException {
        MapperManager mapperManager = TestMapperFactory.createMapperManager();

        ArticleMapper mapper1
                = mapperManager.getArticleMapper();
        ArticleMapper mapper2
                = mapperManager.getArticleMapper();

        Assert.assertEquals(ArticleMapperImpl.class.getCanonicalName(),
                mapper1.getClass().getCanonicalName());

        Assert.assertSame(mapper1, mapper2);
    }

    @Test
    public void testCreate() throws PersistenceException {
        MapperManager mapperManager = TestMapperFactory.createMapperManager();
        
        Article nouvelleEntite = (Article) mapperManager.transactionAndClose(new MapperCommand() {

            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().create();
            }
        });

        Assert.assertNotNull(nouvelleEntite);
        Assert.assertNotNull(nouvelleEntite.getId());
    }

    @Test
    public void testCreateEntite() throws PersistenceException {
        MapperManager mapperManager = TestMapperFactory.createMapperManager();

        final Article entite = ArticleBase.builder()
                .nom("Nouvelle article")
                .poids(12.3)
                .couleur("nouvelle couleur")
                .qteStock(45)
                .prixAchat(new BigDecimal("67.80"))
                .prixVente(new BigDecimal("9.01")).build();


        Article nouvelleEntite = (Article) mapperManager.transactionAndClose(new MapperCommand() {

            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().create(entite);
            }
        });

        Assert.assertNotNull(nouvelleEntite);
        Assert.assertNotNull(nouvelleEntite.getId());

        Assert.assertEquals(entite.getNom(), nouvelleEntite.getNom());
        Assert.assertEquals(entite.getCouleur(), nouvelleEntite.getCouleur());
        Assert.assertEquals(entite.getPoids(), nouvelleEntite.getPoids());
        Assert.assertEquals(entite.getQteStock(), nouvelleEntite.getQteStock());
        Assert.assertEquals(entite.getPrixAchat(), nouvelleEntite.getPrixAchat());
        Assert.assertEquals(entite.getPrixVente(), nouvelleEntite.getPrixVente());
    }

    @Test
    public void testRetreave() throws PersistenceException {
        MapperManager mapperManager = TestMapperFactory.createMapperManager();

        final Article entite = ArticleBase.builder()
                .nom("article create test")
                .poids(12.3)
                .couleur("couleur test")
                .qteStock(45)
                .prixAchat(new BigDecimal("67.80"))
                .prixVente(new BigDecimal("9.01"))
                .build();

        final Article nouvelleEntite = (Article) mapperManager.transaction(new MapperCommand() {

            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().create(entite);
            }
        });

        List<Article> entiteTrouveeList = (List<Article>) mapperManager.transactionAndClose(new MapperCommand() {

            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().retreave(nouvelleEntite.getNom());
            }
        });

        Assert.assertTrue(entiteTrouveeList.size() > 0);
        Assert.assertTrue(entiteTrouveeList.contains(nouvelleEntite));
    }

    @Test
    public void testRetreaveById() throws PersistenceException {
        MapperManager mapperManager = TestMapperFactory.createMapperManager();

        final Article entite = ArticleBase.builder()
                .nom("article retreave test")
                .poids(12.3)
                .couleur("couleur test")
                .qteStock(45)
                .prixAchat(new BigDecimal("67.80"))
                .prixVente(new BigDecimal("9.01"))
                .build();

        final Article nouvelleEntite = (Article) mapperManager.transaction(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().create(entite);
            }
        });

        Article entiteTrouvee = (Article) mapperManager.transactionAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().retreave(nouvelleEntite.getId());
            }
        });

        Assert.assertEquals(nouvelleEntite, entiteTrouvee);
    }

    @Test
    public void testRetreaveByNullId() throws PersistenceException {
        final Long id = null;

        MapperManager mapperManager = TestMapperFactory.createMapperManager();

        Article entiteTrouvee = (Article) mapperManager.transactionAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().retreave(id);
            }
        });

        Assert.assertNull(entiteTrouvee);
    }

    @Test
    public void testUpdate() throws PersistenceException {
        MapperManager mapperManager = TestMapperFactory.createMapperManager();

        final Article entite = ArticleBase.builder()
                .nom("article update test")
                .poids(12.3)
                .couleur("couleur test")
                .qteStock(45)
                .prixAchat(new BigDecimal("67.80"))
                .prixVente(new BigDecimal("9.01"))
                .build();

        final Article nouvelleEntite = (Article) mapperManager.transaction(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().create(entite);
            }
        });

        Article entiteTrouvee = (Article) mapperManager.transaction(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {

                Article entiteTrouvee = mapperManager.getArticleMapper().retreave(nouvelleEntite.getId());
                entiteTrouvee.setNom(entiteTrouvee.getNom() + "++++");
                entiteTrouvee.setCouleur(entiteTrouvee.getCouleur() + "++++");
                entiteTrouvee.setPoids(123.4);
                entiteTrouvee.setPrixAchat(new BigDecimal("678.90"));
                entiteTrouvee.setPrixVente(new BigDecimal("678.90"));
                entiteTrouvee.setQteStock(450);

                mapperManager.getArticleMapper().update(entiteTrouvee);

                return entiteTrouvee;
            }
        });

        Article articleModifie = (Article) mapperManager.transactionAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().retreave(nouvelleEntite.getId());
            }
        });

        Assert.assertEquals(entiteTrouvee, articleModifie);
    }

    @Test
    public void testDelete() throws PersistenceException {
        MapperManager mapperManager = TestMapperFactory.createMapperManager();

        final Article nouvelleEntite = (Article) mapperManager.transaction(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().create();
            }
        });

        final Article entiteTrouvee = (Article) mapperManager.transaction(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().retreave(nouvelleEntite.getId());
            }
        });

        mapperManager.transaction(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                mapperManager.getArticleMapper().delete(entiteTrouvee);
                return null;
            }
        });

        Article entiteTrouvee2 = (Article) mapperManager.transactionAndClose(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                return mapperManager.getArticleMapper().retreave(entiteTrouvee.getId());

            }
        });

        Assert.assertNull(entiteTrouvee2);
    }

    @Test
    public void testDeleteUnknownArticle() throws PersistenceException {
        MapperManager mapperManager = TestMapperFactory.createMapperManager();

        final Article entite = ArticleBase.builder()
                .id(-111L)
                .nom("article update test")
                .poids(12.3)
                .couleur("couleur test")
                .qteStock(45)
                .prixAchat(new BigDecimal("67.80"))
                .prixVente(new BigDecimal("9.01"))
                .build();

        mapperManager.transaction(new MapperCommand() {
            @Override
            public Object execute(MapperManager mapperManager) throws PersistenceException {
                mapperManager.getArticleMapper().delete(entite);
                return null;
            }
        });

    }

}
