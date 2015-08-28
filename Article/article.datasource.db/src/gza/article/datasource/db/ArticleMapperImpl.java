package gza.article.datasource.db;

import gza.article.domain.Article;
import gza.article.domain.ArticleBase;
import gza.article.datasource.ArticleMapper;
import gza.article.datasource.EntiteInconnuePersistenceException;
import gza.article.datasource.EntiteUtiliseePersistenceException;
import gza.article.datasource.PersistenceException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ArticleMapperImpl extends EntiteMapperImpl<Article> implements ArticleMapper {

    public static String CREATE_TABLE = "CREATE TABLE ARTICLES ("
            + "  NUMERO           INTEGER,"
            + "  NOM              TEXT,"
            + "  POIDS            NUMERIC(12,2),"
            + "  COULEUR          TEXT,"
            + "  QTE_STOCK        INTEGER,"
            + "  PRIX_ACHAT       NUMERIC(12,2),"
            + "  PRIX_VENTE       NUMERIC(12,2),"
            + "  CONSTRAINT PK_ARTICLES"
            + "    PRIMARY KEY (NUMERO)"
            + ")";


    public static String DROP_TABLE = "DROP TABLE ARTICLES";

    public static String CREATE_SEQUENCE = "CREATE SEQUENCE LAST_ARTICLE_ID START 1000";
    public static String DROP_SEQUENCE = "DROP SEQUENCE LAST_ARTICLE_ID";

    public static final String SQL_INSERT = "INSERT INTO ARTICLES "
            + "(NUMERO,NOM,POIDS,COULEUR,QTE_STOCK,PRIX_ACHAT,PRIX_VENTE)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String SQL_UPDATE = "UPDATE ARTICLES "
            + "SET NOM = ?, POIDS =  ?, COULEUR = ?, QTE_STOCK = ?, PRIX_ACHAT = ?, PRIX_VENTE= ? "
            + "WHERE NUMERO = ?";

    public static final String SQL_DELETE = "DELETE FROM ARTICLES "
            + "WHERE NUMERO = ?";

    public static final String SQL_SELECT_ALL = "SELECT NUMERO,NOM,POIDS,"
            + "COULEUR,QTE_STOCK,PRIX_ACHAT,PRIX_VENTE "
            + "FROM ARTICLES ";

    public static final String SQL_SELECT_BY_ID = SQL_SELECT_ALL
            + "WHERE NUMERO = ?";

    public static final String SQL_SELECT_BY_ID_FOR_UPDATE = SQL_SELECT_BY_ID
            + " FOR UPDATE";

    public static final String SQL_SELECT = SQL_SELECT_ALL
            + "WHERE NUMERO = ? OR UPPER(NOM) LIKE UPPER(?) OR UPPER(COULEUR) LIKE UPPER(?) "
            + "ORDER BY NUMERO";

//    public static final String SQL_SELECT_MAX = "SELECT MAX(NUMERO) FROM ARTICLES";
    public static final String SQL_SELECT_MAX = "SELECT nextval('LAST_ARTICLE_ID')";
    
    public static final String TABLE_ATTRIBUT_NUMERO = "NUMERO";
    public static final String TABLE_ATTRIBUT_NOM = "NOM";
    public static final String TABLE_ATTRIBUT_POIDS = "POIDS";
    public static final String TABLE_ATTRIBUT_COULEUR = "COULEUR";
    public static final String TABLE_ATTRIBUT_QTE_STOCK = "QTE_STOCK";
    public static final String TABLE_ATTRIBUT_PRIX_ACHAT = "PRIX_ACHAT";
    public static final String TABLE_ATTRIBUT_PRIX_VENTE = "PRIX_VENTE";

    public ArticleMapperImpl(DbMapperManager mapperManager) {
        super(Article.class, mapperManager);

        this.sqlSelectMaxStr = SQL_SELECT_MAX;
        this.sqlSelectByIdStr = SQL_SELECT_BY_ID;
        this.sqlSelectByIdForUpdateStr = SQL_SELECT_BY_ID_FOR_UPDATE;
        this.sqlDeleteStr = SQL_DELETE;

    }

    @Override
    public Article create(Article e) throws PersistenceException {
        Article entite;
        logger.info(e.toString());
        try {
            Long id = getNewId();
            entite = ArticleBase.builder().id(id)
                    .nom(e.getNom())
                    .couleur(e.getCouleur())
                    .qteStock(e.getQteStock())
                    .poids(e.getPoids())
                    .prixAchat(e.getPrixAchat())
                    .prixVente(e.getPrixVente())
                    .build();

            PreparedStatement statement = this.mapperManager.getConnection().prepareStatement(SQL_INSERT);
            statement.setLong(1, entite.getId());
            statement.setString(2, entite.getNom());
            if (e.getPoids() != null) {
                statement.setDouble(3, entite.getPoids());
            } else {
                statement.setNull(3, java.sql.Types.NUMERIC);
            }
            statement.setString(4, entite.getCouleur());
            if (e.getQteStock() != null) {
                statement.setInt(5, entite.getQteStock());
            } else {
                statement.setNull(5, java.sql.Types.NUMERIC);
            }

            if (e.getPrixAchat() != null) {
                statement.setBigDecimal(6, entite.getPrixAchat());
            } else {
                statement.setNull(6, java.sql.Types.NUMERIC);
            }
            if (e.getPrixVente() != null) {
                statement.setBigDecimal(7, entite.getPrixVente());
            } else {
                statement.setNull(7, java.sql.Types.NUMERIC);
            }

            statement.executeUpdate();

        } catch (SQLException ex) {
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
    protected Article createEntite(ResultSet result) throws PersistenceException {
        Article article;
        try {
            Long id = result.getLong(TABLE_ATTRIBUT_NUMERO);
            String nom = result.getString(TABLE_ATTRIBUT_NOM);
            Double poids = result.getDouble(TABLE_ATTRIBUT_POIDS);
            String couleur = result.getString(TABLE_ATTRIBUT_COULEUR);
            Integer qteStock = result.getInt(TABLE_ATTRIBUT_QTE_STOCK);
            BigDecimal prixAchat = result.getBigDecimal(TABLE_ATTRIBUT_PRIX_ACHAT);
            BigDecimal prixVente = result.getBigDecimal(TABLE_ATTRIBUT_PRIX_VENTE);

            article = ArticleBase.builder().id(id)
                    .nom(nom)
                    .couleur(couleur)
                    .qteStock(qteStock)
                    .poids(poids)
                    .prixAchat(prixAchat)
                    .prixVente(prixVente)
                    .build();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
        return article;
    }

    @Override
    public List<Article> retreave(String recherche) throws PersistenceException {
        logger.info(String.format("recherche = %s", recherche));

        List<Article> list = new ArrayList<>();
        try {
            PreparedStatement statement = mapperManager.getConnection().prepareStatement(SQL_SELECT);

            try {
                statement.setLong(1, new Long(recherche));
            } catch (NumberFormatException ex) {
                //ne fait rien
                statement.setNull(1, Types.NUMERIC);
            }
            statement.setString(2, "%" + recherche + "%");
            statement.setString(3, "%" + recherche + "%");

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Article entite = this.createEntite(result);
                    list.add(entite);
                }
            }
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }

        return list;
    }

    @Override
    public void update(Article e) throws PersistenceException {
        logger.info(e.toString());
        Article a = this.retreaveForUpdate(e.getId());
        if (a == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format(messages.getString("ERREUR_ARTICLE_INCONNU"), e));
        }

        try {
            PreparedStatement statement = mapperManager.getConnection().prepareStatement(SQL_UPDATE);
            statement.setString(1, e.getNom());

            if (e.getPoids() != null) {
                statement.setDouble(2, e.getPoids());
            } else {
                statement.setNull(2, java.sql.Types.DOUBLE);
            }
            statement.setString(3, e.getCouleur());
            if (e.getQteStock() != null) {
                statement.setInt(4, e.getQteStock());
            } else {
                statement.setNull(4, java.sql.Types.INTEGER);
            }

            if (e.getPrixAchat() != null) {
                statement.setBigDecimal(5, e.getPrixAchat());
            } else {
                statement.setNull(5, java.sql.Types.NUMERIC);
            }
            if (e.getPrixVente() != null) {
                statement.setBigDecimal(6, e.getPrixVente());
            } else {
                statement.setNull(6, java.sql.Types.NUMERIC);
            }

            statement.setLong(7, e.getId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.severe(ex.getMessage());
            throw new PersistenceException(ex);
        }
    }

    @Override
    protected void verifierEntiteUtilisee(Long id) throws PersistenceException {
        /**List<Commande> list = this.mapperManager.getCommandeMapper().retreaveByArticle(id);
        if (list.size() > 0) {
            throw new EntiteUtiliseePersistenceException(
                    String.format(messages.getString("ERREUR_ARTICLE_UTILISE"), id));
        }**/
    }

}
