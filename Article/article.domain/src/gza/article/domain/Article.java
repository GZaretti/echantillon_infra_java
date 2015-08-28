package gza.article.domain;

import java.math.BigDecimal;

public interface Article extends Entite<Article> {

    String getNom();

    void setNom(String nom);

    Double getPoids();

    void setPoids(Double poids);

    String getCouleur();

    void setCouleur(String couleur);

    Integer getQteStock();

    void setQteStock(Integer qteStock);

    BigDecimal getPrixAchat();

    void setPrixAchat(BigDecimal prixAchat);

    BigDecimal getPrixVente();

    void setPrixVente(BigDecimal prixVente);

}
