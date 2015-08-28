package gza.article.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ArticleBase extends EntiteBase<Article> implements Article, Serializable {

    protected String nom;
    protected Double poids;
    protected String couleur;
    protected Integer qteStock;
    protected BigDecimal prixAchat;
    protected BigDecimal prixVente;

    private ArticleBase(Long id, String nom, Double poids, String couleur, Integer qteStock,
            BigDecimal prixAchat, BigDecimal prixVente, Long version) {
        super(id, version);
        this.nom = nom;
        this.poids = poids;
        this.couleur = couleur;
        this.qteStock = qteStock;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;

    }

    private ArticleBase(Article a) {
        this(a.getId(), a.getNom(),
                a.getPoids(), a.getCouleur(),
                a.getQteStock(), a.getPrixAchat(),
                a.getPrixVente(), a.getVersion());
    }

    private ArticleBase(ArticleBase.Builder a) {
        this(a.id,
                a.nom,
                a.poids,
                a.couleur,
                a.qteStock,
                a.prixAchat,
                a.prixVente,
                a.version);
    }

    @Override
    public String getNom() {
        return this.nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public Double getPoids() {
        return this.poids;
    }

    @Override
    public void setPoids(Double poids) {
        this.poids = poids;
    }

    @Override
    public String getCouleur() {
        return this.couleur;
    }

    @Override
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    @Override
    public Integer getQteStock() {
        return this.qteStock;
    }

    @Override
    public void setQteStock(Integer qteStock) {
        this.qteStock = qteStock;
    }

    @Override
    public BigDecimal getPrixAchat() {
        return this.prixAchat;
    }

    @Override
    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    @Override
    public BigDecimal getPrixVente() {
        return this.prixVente;
    }

    @Override
    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    @Override
    public String toString() {
        return String.format("ArticleBase : { %s, nom : %s, "
                + "poids : %f, couleur : %s, qteStock : %d, "
                + "prixAchat : %f, prixVente : %f }", super.toString(),
                this.getNom(), this.getPoids(), this.getCouleur(),
                this.getQteStock(), this.getPrixAchat(), this.getPrixVente());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + super.hashCode();
        if (this.nom != null) {
            hash = 47 * hash + Objects.hashCode(this.nom);
        }
        if (this.poids != null) {
            hash = 47 * hash + Objects.hashCode(this.poids);
        }
        if (this.couleur != null) {
            hash = 47 * hash + Objects.hashCode(this.couleur);
        }
        if (this.qteStock != null) {
            hash = 47 * hash + Objects.hashCode(this.qteStock);
        }
        if (this.prixAchat != null) {
            hash = 47 * hash + Objects.hashCode(this.prixAchat);
        }
        if (this.prixVente != null) {
            hash = 47 * hash + Objects.hashCode(this.prixVente);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof Article)) {
            return false;
        }

        final Article other = (Article) obj;
        if (!Objects.equals(this.nom, other.getNom())) {
            return false;
        }
        if (!Objects.equals(this.poids, other.getPoids())) {
            return false;
        }
        if (!Objects.equals(this.couleur, other.getCouleur())) {
            return false;
        }
        if (!Objects.equals(this.qteStock, other.getQteStock())) {
            return false;
        }
        if (!Objects.equals(this.prixAchat, other.getPrixAchat())) {
            return false;
        }
        if (!Objects.equals(this.prixVente, other.getPrixVente())) {
            return false;
        }
        return true;
    }

    @Override
    public String getDescription() {
        return String.format("%s %s (CHF%.2f)",
                this.nom,
                this.couleur != null ? this.couleur : "",
                this.prixVente);
    }

    @Override
    public void update(Article entite) {
        this.nom = entite.getNom();
        this.couleur = entite.getCouleur();
        this.poids = entite.getPoids();
        this.prixAchat = entite.getPrixAchat();
        this.prixVente = entite.getPrixVente();
        this.qteStock = entite.getQteStock();
    }
    
    public static Builder builder(){
        return new Builder();
    }

    public static class Builder extends EntiteBase.Builder<Article> {

        private String nom;
        private Double poids;
        private String couleur;
        private Integer qteStock;
        private BigDecimal prixAchat;
        private BigDecimal prixVente;

        private Builder() {
            super();
        }
        
        @Override
        public Builder id(Long id){
            this.id = id;
            return this;
        }
        
        @Override
        public Builder version(Long version){
            this.version = version;
            return this;
        }
        
        public Builder article(Article art) {
            this.id = art.getId();
            this.nom = art.getNom();
            this.poids = art.getPoids();
            this.couleur = art.getCouleur();
            this.prixAchat = art.getPrixAchat();
            this.prixVente = art.getPrixVente();
            this.qteStock = art.getQteStock();
            this.version = art.getVersion();
            return this;
        }

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder poids(Double poids) {
            this.poids = poids;
            return this;
        }

        public Builder couleur(String couleur) {
            this.couleur = couleur;
            return this;
        }

        public Builder qteStock(Integer qteStock) {
            this.qteStock = qteStock;
            return this;
        }

        public Builder prixAchat(BigDecimal prixAchat) {
            this.prixAchat = prixAchat;
            return this;
        }

        public Builder prixVente(BigDecimal prixVente) {
            this.prixVente = prixVente;
            return this;
        }
        
        @Override
        public Article build() {
            return new ArticleBase(this);
        }

    }

}
