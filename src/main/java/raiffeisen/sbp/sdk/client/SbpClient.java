package raiffeisen.sbp.sdk.client;

import raiffeisen.sbp.sdk.json.JsonBuilder;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.io.IOException;

public class SbpClient {
    public static String TEST_DOMAIN = "https://test.ecom.raiffeisen.ru";
    public static String PRODUCTION_DOMAIN = "https://e-commerce.raiffeisen.ru";

    private static final String REGISTER_PATH = "/api/sbp/v1/qr/register";
    private static final String QR_INFO_PATH = "/api/sbp/v1/qr/?/info";
    private static final String PAYMENT_INFO_PATH = "/api/sbp/v1/qr/?/payment-info";
    private static final String REFUND_PATH = "/api/sbp/v1/refund";
    private static final String REFUND_INFO_PATH = "/api/sbp/v1/refund/?";

    public static Response registerQR(final String domain, QRInfo qr) throws IOException {
        return PostRequester.request(domain + REGISTER_PATH, JsonBuilder.fromObject(qr), null);
    }

    public static Response getQRInfo(final String domain, QRId qrId, final String secretKey) throws IOException {
        return GetRequester.request(domain + QR_INFO_PATH, qrId, secretKey);
    }
}
