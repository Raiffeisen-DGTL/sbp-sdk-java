package raiffeisen.sbp.sdk.client;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRId;

import java.io.IOException;

public class GetRequester {
    public static Response request(String url, QRId qrId, final String secretKey) throws IOException, SbpException {
        return request(url, qrId.getQrId(), secretKey);
    }

    public static Response request(String url, final String pathParameter, final String secretKey) throws IOException, SbpException {
        url = url.replace("?", pathParameter);
        HttpGet getter = new HttpGet(url);

        getter.addHeader("content-type", "application/json");
        getter.addHeader("charset", "UTF-8");

        getter.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(getter)) {

            int code = response.getStatusLine().getStatusCode();
            if(code != 200) {
                throw new SbpException("Bad HTTP code: " + code);
            }
            else {
                return new Response(code, EntityUtils.toString(response.getEntity()));
            }
        }
    }
}
