package raiffeisen.sbp.sdk.client;


import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import raiffeisen.sbp.sdk.model.Response;

import java.io.IOException;

public class PostRequester {
    public static Response request(String url, String body, final String secretKey) throws IOException {

        HttpPost poster = new HttpPost(url);
        poster.addHeader("content-type", "application/json");
        poster.addHeader("charset", "UTF-8");
        if(secretKey != null) {
            poster.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey);
        }

        poster.setEntity(new StringEntity(body));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(poster)) {

            return new Response(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
        }
    }
}
