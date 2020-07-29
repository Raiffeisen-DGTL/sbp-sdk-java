package raiffeisen.sbp.sdk.client;


import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.web.WebClient;

import java.io.IOException;
import java.util.HashMap;

public class PostRequester {

    private WebClient webClient;

    public PostRequester(WebClient client) {
        webClient = client;
    }

    public void setWebClient(WebClient client) {
        webClient = client;
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public Response request(String url, String body, final String secretKey) throws IOException, SbpException {

        HashMap<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("charset", "UTF-8");
        if(secretKey != null) {
            headers.put("Authorization", "Bearer " + secretKey);
        }

        return webClient.request("POST", url, headers, body);
    }
}
