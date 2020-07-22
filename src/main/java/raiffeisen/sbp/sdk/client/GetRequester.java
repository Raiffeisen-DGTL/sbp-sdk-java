package raiffeisen.sbp.sdk.client;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRId;

import java.io.IOException;

public class GetRequester {
    public static Response request(String url, QRId qrId, final String secretKey) throws IOException {

        url = url.replace("?", qrId.getQrId());
        HttpGet getter = new HttpGet(url);

        getter.addHeader("content-type", "application/json");
        getter.addHeader("charset", "UTF-8");

        getter.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(getter)) {
            return new Response(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
        }
    }

    public static Response request(String url, final String pathParameter, final String secretKey) throws IOException {
        url = url.replace("?", pathParameter);
        HttpGet getter = new HttpGet(url);

        getter.addHeader("content-type", "application/json");
        getter.addHeader("charset", "UTF-8");

        getter.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(getter)) {
            return new Response(response.getStatusLine().getStatusCode(), EntityUtils.toString(response.getEntity()));
        }
    }
}
