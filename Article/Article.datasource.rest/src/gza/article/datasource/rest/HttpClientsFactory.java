package gza.article.datasource.rest;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientsFactory {

    public static CloseableHttpClient getHttpClient() {
        
        CloseableHttpClient httpClient = HttpClients.createDefault();

        return httpClient;
    }

}
