package raiffeisen.sbp.sdk.client;

import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.web.WebClient;

import java.io.IOException;
import java.util.Map;

public class GetRequester extends Requester {

    public GetRequester(WebClient client) {
        super(client);
    }

    public Response request(String url, QRId qrId, final String secretKey) throws SbpException, IOException {
        return request(url, qrId.getQrId(), secretKey);
    }

    public Response request(String url, final String pathParameter, final String secretKey) throws SbpException, IOException {
        url = url.replace("?", pathParameter);

        Map<String, String> headers = prepareHeaders(secretKey);

        Response response = webClient.request(WebClient.GET_METHOD, url, headers, null);
        return responseOrThrow(response);
    }
}
