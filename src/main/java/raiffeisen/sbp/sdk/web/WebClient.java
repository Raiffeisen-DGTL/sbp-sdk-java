package raiffeisen.sbp.sdk.web;

import raiffeisen.sbp.sdk.model.Response;

import java.io.IOException;
import java.util.Map;

public interface WebClient {

    Response request(String method, String url, Map<String, String> headers, String entity) throws IOException;
}
