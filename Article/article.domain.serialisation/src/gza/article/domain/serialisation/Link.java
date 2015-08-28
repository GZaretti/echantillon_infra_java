package gza.article.domain.serialisation;

import java.net.URI;


public interface Link {
    public static final String SELF = "self";
    public static final String ARTICLE_URL_TEMPLATE = "%s/articles/%d.html";
    
    public String getRel();
    public void setRel(String rel);
    public URI getHref();
    public void setHref(URI link);
}
