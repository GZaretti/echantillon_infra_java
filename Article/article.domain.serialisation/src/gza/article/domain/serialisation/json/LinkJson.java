package gza.article.domain.serialisation.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import gza.article.domain.serialisation.Link;
import java.net.URI;
import java.util.Objects;

public class LinkJson implements Link{
    
    private String rel;

    private URI href;

    @JsonCreator
    public LinkJson(@JsonProperty("rel") String rel, 
            @JsonProperty("href") URI href) {
        this.rel = rel;
        this.href = href;
    }
    
    public LinkJson(URI href) {
        this(Link.SELF,href);
    }
    

    @JsonProperty("rel")
    @Override
    public String getRel() {
        return rel;
    }

    @Override
    public void setRel(String rel) {
        this.rel = rel;
    }
    
    @JsonProperty("href")
    @Override
    public URI getHref() {
        return href;
    }

    @Override
    public void setHref(URI href) {
        this.href = href;
    }


    @Override
    public String toString() {
        return "LinkJson{" + "rel=" + rel + ", href=" + href + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.rel);
        hash = 97 * hash + Objects.hashCode(this.href);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LinkJson other = (LinkJson) obj;
        if (!Objects.equals(this.rel, other.rel)) {
            return false;
        }
        if (!Objects.equals(this.href, other.href)) {
            return false;
        }
        return true;
    }
    
    

}
