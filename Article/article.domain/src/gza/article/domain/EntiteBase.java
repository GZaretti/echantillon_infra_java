package gza.article.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * @param <T>
 */
public abstract class EntiteBase<T> implements Entite<T>, Serializable {

    protected Long id;
    protected Long version;

    protected EntiteBase() {
        this.version = 0L;
    }

    protected EntiteBase(Long id) {
        this();
        this.id = id;
    }
    
    protected EntiteBase(Long id, Long version) {
        this();
        this.id = id;
        this.version = version != null ? version : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getDescription() {
        return String.format("id : %d", this.id);
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return String.format("EntiteBase : { id : %d, version : %d } ", this.id, this.version);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Entite)) {
            return false;
        }

        final Entite other = (Entite) obj;
        if (!Objects.equals(this.id, other.getId())) {
            return false;
        }

        if (!Objects.equals(this.version, other.getVersion())) {
            return false;
        }
        return true;
    }
    
    public static abstract class Builder<T> {

        protected Long id;
        protected Long version;

        protected Builder() {
        }

        public Builder<T> id(Long id) {
            this.id = id;
            return this;
        }

        public Builder<T> version(Long version) {
            this.version = version;
            return this;
        }
        
        public abstract T build();

    }    

}
