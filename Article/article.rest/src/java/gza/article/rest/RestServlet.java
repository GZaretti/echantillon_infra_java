package gza.article.rest;

import static gza.article.rest.ArticleRestServlet.MESSAGE_BUNDLE;
import gza.article.domain.serialisation.json.LinkJson;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class RestServlet extends HttpServlet {

    protected static final Logger logger = Logger.getLogger(RestServlet.class.getCanonicalName());

    public static final String MESSAGE_BUNDLE = "gza/article/rest/MessagesBundle";

    public static final String ENCODAGE_UTF8 = "UTF-8";
    public static final String HEADER_ATTRIBUT_LOCATION = "Location";
    public static final String MEDIA_APPLICATION_JSON = "application/json";

    public static final String RECHERCHE_ATTRIBUT = "recherche";

    protected Pattern entitesPattern;
    protected Pattern entitePattern;
    protected String entiteTemplate;

    protected final ResourceBundle messages;

    public RestServlet() {
        this.messages = ResourceBundle.getBundle(MESSAGE_BUNDLE);

    }

    protected boolean etreUrlEntites(String requestUrl) {
        return this.entitesPattern.matcher(requestUrl).matches();
    }

    protected boolean etreUrlEntite(String requestUrl) {
        return this.entitePattern.matcher(requestUrl).matches();
    }

    protected Long getId(String requestURL) {
        return getId(this.entitePattern, requestURL);
    }

    protected Long getId(Pattern pattern, String requestURL) {
        Matcher matcher = pattern.matcher(requestURL);
        Long id = null;
        if (matcher.matches()) {
            id = Long.parseLong(matcher.group(2));
        }
        return id;
    }

    protected String getContext(String requestURL) {
        return this.getContext(entitePattern, requestURL);
    }

    protected String getContext(Pattern pattern, String requestURL) {
        Matcher matcher = pattern.matcher(requestURL);
        String context = null;
        if (matcher.matches()) {
            context = matcher.group(1);
        }

        if (context == null) {
            matcher = this.entitesPattern.matcher(requestURL);
            if (matcher.matches()) {
                context = matcher.group(1);
            }
        }
        return context;
    }

    protected LinkJson getLink(HttpServletRequest request, Long id) throws URISyntaxException {
        return this.getLink(this.entiteTemplate, request, id);
    }

    protected LinkJson getLink(String template, HttpServletRequest request, Long id) throws URISyntaxException {
        return this.getLink(entitePattern, template, request, id);
    }

    protected LinkJson getLink(Pattern pattern, String template, HttpServletRequest request, Long id) throws URISyntaxException {
        return new LinkJson(
                new URI(String.format(template,
                                this.getContext(pattern, request.getRequestURL().toString()), id)));

    }

    public String getRechercheParametre(HttpServletRequest request) {
        String recherche = request.getParameter(RECHERCHE_ATTRIBUT);
        return recherche != null ? recherche : "";
    }

}
