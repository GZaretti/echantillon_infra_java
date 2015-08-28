package gza.article.datasource.db;

import gza.article.domain.ArticleBase;
import gza.article.datasource.PersistenceException;
import gza.article.domain.Article;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class DatabaseSetup {

    static final Logger logger = Logger.getLogger(DatabaseSetup.class.getCanonicalName());

    private final DbMapperManager mapperManager;

    public DatabaseSetup(DbMapperManager mapperManager) {
        this.mapperManager = mapperManager;
    }

    public void createTables() throws PersistenceException {

        try {
            Statement statement = mapperManager.getConnection().createStatement();
            statement.addBatch(ArticleMapperImpl.CREATE_TABLE);
            statement.addBatch(ArticleMapperImpl.CREATE_SEQUENCE);
            
            int[] status = statement.executeBatch();
            for (int sts : status) {
                logger.info(String.format("status : %d", sts));
            }

        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }

    }

    public void dropTables() throws PersistenceException {
        try {
            Statement statement = mapperManager.getConnection().createStatement();
            statement.addBatch(ArticleMapperImpl.DROP_TABLE);

            statement.addBatch(ArticleMapperImpl.DROP_SEQUENCE);

            int[] status = statement.executeBatch();
            for (int sts : status) {
                logger.info(String.format("status : %d", sts));
            }

        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }

    }

    public void insertDatas() throws PersistenceException {
        setupArticles();
        
    }

    public void setupArticles() throws PersistenceException {
        logger.info("");
        try {
            PreparedStatement statement = mapperManager.getConnection()
                    .prepareStatement(ArticleMapperImpl.SQL_INSERT);
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(1L)
                    .nom("AGRAFEUSE")
                    .poids(150.0)
                    .couleur("ROUGE")
                    .qteStock(10)
                    .prixAchat(new BigDecimal("20.00"))
                    .prixVente(new BigDecimal("29.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(2L)
                    .nom("CALCULATRICE")
                    .poids(150.0)
                    .couleur("NOIR")
                    .qteStock(5)
                    .prixAchat(new BigDecimal("200.00"))
                    .prixVente(new BigDecimal("235.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(3L)
                    .nom("CACHET DATEUR")
                    .poids(100.0)
                    .couleur("BLANC")
                    .qteStock(3)
                    .prixAchat(new BigDecimal("21.00"))
                    .prixVente(new BigDecimal("30.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(4L)
                    .nom("LAMPE")
                    .poids(550.0)
                    .couleur("ROUGE")
                    .qteStock(3)
                    .prixAchat(new BigDecimal("105.00"))
                    .prixVente(new BigDecimal("149.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(5L)
                    .nom("LAMPE")
                    .poids(550.0)
                    .couleur("BLANC")
                    .qteStock(3)
                    .prixAchat(new BigDecimal("100.00"))
                    .prixVente(new BigDecimal("149.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(6L)
                    .nom("LAMPE")
                    .poids(550.0)
                    .couleur("BLEU")
                    .qteStock(3)
                    .prixAchat(new BigDecimal("105.00"))
                    .prixVente(new BigDecimal("149.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(7L)
                    .nom("LAMPE")
                    .poids(550.0)
                    .couleur("VERT")
                    .qteStock(3)
                    .prixAchat(new BigDecimal("105.00"))
                    .prixVente(new BigDecimal("149.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(8L)
                    .nom("PESE-LETTRE1-500")
                    .qteStock(2)
                    .prixAchat(new BigDecimal("120.00"))
                    .prixVente(new BigDecimal("200.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(9L)
                    .nom("PESE-LETTRE1-1000")
                    .qteStock(2)
                    .prixAchat(new BigDecimal("150.00"))
                    .prixVente(new BigDecimal("250.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(10L)
                    .nom("PESE-LETTRE1-500")
                    .poids(0.0)
                    .qteStock(2).prixAchat(new BigDecimal("120.00"))
                    .prixVente(new BigDecimal("200.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(11L)
                    .nom("PESE-LETTRE1-1000")
                    .poids(0.0)
                    .qteStock(2)
                    .prixAchat(new BigDecimal("150.00"))
                    .prixVente(new BigDecimal("250.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(12L)
                    .nom("CRAYON")
                    .poids(20.0)
                    .couleur("ROUGE")
                    .qteStock(210)
                    .prixAchat(new BigDecimal("1.00"))
                    .prixVente(new BigDecimal("2.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(13L)
                    .nom("CRAYON")
                    .poids(30.0)
                    .couleur("BLEU")
                    .qteStock(190)
                    .prixAchat(new BigDecimal("1.00"))
                    .prixVente(new BigDecimal("2.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(14L)
                    .nom("CRAYON LUXE")
                    .poids(20.0)
                    .couleur("ROUGE")
                    .qteStock(95)
                    .prixAchat(new BigDecimal("3.00"))
                    .prixVente(new BigDecimal("5.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(15L)
                    .nom("CRAYON LUXE")
                    .poids(20.0)
                    .couleur("VERT")
                    .qteStock(90)
                    .prixAchat(new BigDecimal("3.00"))
                    .prixVente(new BigDecimal("5.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(16L)
                    .nom("CRAYON LUXE")
                    .poids(20.0)
                    .couleur("BLEU").qteStock(80)
                    .prixAchat(new BigDecimal("3.00"))
                    .prixVente(new BigDecimal("5.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(17L)
                    .nom("CRAYON LUXE")
                    .poids(20.0)
                    .couleur("NOIR")
                    .qteStock(450)
                    .prixAchat(new BigDecimal("3.00"))
                    .prixVente(new BigDecimal("5.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(18L)
                    .nom("COFFRET SIMPLE")
                    .poids(300.0)
                    .couleur("NOIR")
                    .qteStock(20)
                    .prixAchat(new BigDecimal("100.00"))
                    .prixVente(new BigDecimal("120.00"))
                    .build());
            addBatchArticle(statement,
                    ArticleBase.builder()
                    .id(19L)
                    .nom("COFFRET LUXE")
                    .poids(350.0)
                    .couleur("BLEU")
                    .qteStock(15)
                    .prixAchat(new BigDecimal("150.00"))
                    .prixVente(new BigDecimal("180.00"))
                    .build());
            int[] status = statement.executeBatch();
            for (int sts : status) {
                EntiteMapperImpl.logger.info(String.format("status : %d", sts));
            }
        } catch (SQLException ex) {
            EntiteMapperImpl.logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }
    }

    private void addBatchArticle(PreparedStatement statement,
            Article article) throws SQLException {
        logger.info(article.toString());
        statement.setLong(1, article.getId());
        statement.setString(2, article.getNom());
        if (article.getPoids() != null) {
            statement.setDouble(3, article.getPoids());
        } else {
            statement.setNull(3, Types.NUMERIC);
        }
        statement.setString(4, article.getCouleur());
        if (article.getQteStock() != null) {
            statement.setInt(5, article.getQteStock());
        } else {
            statement.setNull(5, Types.NUMERIC);
        }
        if (article.getPrixAchat() != null) {
            statement.setBigDecimal(6, article.getPrixAchat());
        } else {
            statement.setNull(6, Types.NUMERIC);
        }
        if (article.getPrixVente() != null) {
            statement.setBigDecimal(7, article.getPrixVente());
        } else {
            statement.setNull(7, Types.NUMERIC);
        }
        statement.addBatch();
    }
}
