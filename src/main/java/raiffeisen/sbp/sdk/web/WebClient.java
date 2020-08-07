package raiffeisen.sbp.sdk.web;

import raiffeisen.sbp.sdk.model.Response;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public interface WebClient extends Closeable {
    String GET_METHOD = "GET";
    String POST_METHOD = "POST";

    Response request(String method, String url, Map<String, String> headers, String entity) throws IOException;
}
