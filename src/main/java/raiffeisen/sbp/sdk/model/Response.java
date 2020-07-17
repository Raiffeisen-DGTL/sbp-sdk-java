package raiffeisen.sbp.sdk.model;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Response {
    private final int code;
    private final String body;

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public Response(final CloseableHttpResponse httpResponse) throws IOException {
        this.code = httpResponse.getStatusLine().getStatusCode();
        this.body = EntityUtils.toString(httpResponse.getEntity());
    }
}
