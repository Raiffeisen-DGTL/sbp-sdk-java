package raiffeisen.sbp.sdk.client;

import raiffeisen.sbp.sdk.client.PostRequester;
import raiffeisen.sbp.sdk.json.JsonBuilder;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.CreateQr;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

public class SbpClient {
    private static final String URL_REGISTER = "https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/register";
    private static final String URL_QR_INFO = "https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/?/info";
    private static final String URL_PAYMENT_INFO = "https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/#/payment-info";
    private static final String URL_REFUND = "https://test.ecom.raiffeisen.ru/api/sbp/v1/refund";
    private static final String URL_REFUND_INFO = "https://test.ecom.raiffeisen.ru/api/sbp/v1/refund/?";
    public static Response registerQR(CreateQr qr) throws IOException {
        CloseableHttpResponse response = PostRequester.request(URL_REGISTER, JsonBuilder.fromObject(qr), null);
        return new Response(response);
    }

}
