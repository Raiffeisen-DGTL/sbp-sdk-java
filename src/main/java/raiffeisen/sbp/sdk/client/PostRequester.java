package raiffeisen.sbp.sdk.client;


import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class PostRequester {
    public static CloseableHttpResponse request(String url, String body, final String secretKey) throws IOException {

        HttpPost poster = new HttpPost(url);
        poster.addHeader("content-type", "application/json");
        poster.addHeader("charset", "UTF-8");
        if(secretKey != null) {
            poster.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey);
        }

        poster.setEntity(new StringEntity(body));

        String result = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(poster)) {
            return response;
        }
    }
}
