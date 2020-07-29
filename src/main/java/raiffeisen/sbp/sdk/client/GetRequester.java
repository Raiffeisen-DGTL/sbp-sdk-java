package raiffeisen.sbp.sdk.client;

import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.web.WebClient;

import java.io.IOException;
import java.util.HashMap;

public class GetRequester {

    private WebClient webClient;

    public GetRequester(WebClient client) {
        webClient = client;
    }

    public void setWebClient(WebClient client) {
        webClient = client;
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public Response request(String url, QRId qrId, final String secretKey) throws IOException {
        return request(url, qrId.getQrId(), secretKey);
    }

    public Response request(String url, final String pathParameter, final String secretKey) throws IOException {
        url = url.replace("?", pathParameter);

        HashMap<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("charset", "UTF-8");
        headers.put("Authorization", "Bearer " + secretKey);

        return webClient.request("GET", url, headers, null);
    }
}
