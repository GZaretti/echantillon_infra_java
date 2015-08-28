package gza.article.domain.serialisation.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gza.article.domain.Article;
import gza.article.domain.ArticleBase;
import java.math.BigDecimal;

public class ArticleJson implements Article {

    private final Article article;
    private LinkJson link;

    public ArticleJson(LinkJson link, Article article) {
        this.article = article;
        this.link = link;
    }

    @JsonCreator
    public ArticleJson(
            @JsonProperty("link") LinkJson link,
            @JsonProperty("id") Long id,
            @JsonProperty("nom") String nom,
            @JsonProperty("poids") Double poids,
            @JsonProperty("couleur") String couleur,
            @JsonProperty("qteStock") Integer qteStock,
            @JsonProperty("prixAchat") BigDecimal prixAchat,
            @JsonProperty("prixVente") BigDecimal prixVente,
            @JsonProperty("version") Long version) {
        this(link, ArticleBase.builder()
                .id(id)
                .nom(nom)
                .poids(poids)
                .couleur(couleur)
                .qteStock(qteStock)
                .prixAchat(prixAchat)
                .prixVente(prixVente)
                .version(version)
                .build());
    }

    @JsonProperty("id")
    @Override
    public Long getId() {
        return article.getId();
    }

    @JsonProperty("nom")
    @Override
    public String getNom() {
        return article.getNom();
    }

    @Override
    public void setNom(String nom) {
        article.setNom(nom);
    }

    @JsonProperty("poids")
    @Override
    public Double getPoids() {
        return article.getPoids();
    }

    @Override
    public void setPoids(Double poids) {
        article.setPoids(poids);
    }

    @JsonProperty("couleur")
    @Override
    public String getCouleur() {
        return article.getCouleur();
    }

    @Override
    public void setCouleur(String couleur) {
        article.setCouleur(couleur);
    }

    @JsonProperty("qteStock")
    @Override
    public Integer getQteStock() {
        return article.getQteStock();
    }

    @Override
    public void setQteStock(Integer qteStock) {
        article.setQteStock(qteStock);
    }

    @JsonProperty("prixAchat")
    @Override
    public void setPrixAchat(BigDecimal prixAchat) {
        article.setPrixAchat(prixAchat);
    }

    @JsonProperty("prixAchat")
    @Override
    public BigDecimal getPrixAchat() {
        return article.getPrixAchat();
    }

    @JsonProperty("prixVente")
    @Override
    public BigDecimal getPrixVente() {
        return article.getPrixVente();
    }

    @Override
    public void setPrixVente(BigDecimal prixVente) {
        article.setPrixVente(prixVente);
    }

    @JsonProperty("link")
    public LinkJson getLink() {
        return link;
    }

    @JsonIgnore
    @Override
    public String getDescription() {
        return article.getDescription();
    }

    @Override
    public String toString() {
        return "ArticleJson{" + "article=" + article + ", link=" + link + '}';
    }

    @Override
    public int hashCode() {
        return this.article.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Article)) {
            return false;
        }

        return this.article.equals(obj);
    }

    @Override
    public void update(Article entite) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getVersion() {
        return this.article.getVersion();
    }

}
