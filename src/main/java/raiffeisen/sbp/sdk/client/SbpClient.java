package raiffeisen.sbp.sdk.client;

import raiffeisen.sbp.sdk.json.JsonBuilder;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.model.out.RefundInfo;

import java.io.IOException;

public class SbpClient {
    public static String TEST_DOMAIN = "https://test.ecom.raiffeisen.ru";
    public static String PRODUCTION_DOMAIN = "https://e-commerce.raiffeisen.ru";

    private static final String REGISTER_PATH = "/api/sbp/v1/qr/register";
    private static final String QR_INFO_PATH = "/api/sbp/v1/qr/?/info";
    private static final String PAYMENT_INFO_PATH = "/api/sbp/v1/qr/?/payment-info";
    private static final String REFUND_PATH = "/api/sbp/v1/refund";
    private static final String REFUND_INFO_PATH = "/api/sbp/v1/refund/?";

    private final String domain;

    private final String secretKey;

    public SbpClient(String domain, String secretKey) {
        this.domain = domain;
        this.secretKey = secretKey;
    }

    public Response registerQR(final QRInfo qr) throws IOException {
        return PostRequester.request(domain + REGISTER_PATH, JsonBuilder.fromObject(qr), null);
    }

    public Response refundPayment(final RefundInfo refund) throws IOException {
        return PostRequester.request(domain + REFUND_PATH, JsonBuilder.fromObject(refund), secretKey);
    }

    public Response getQRInfo(final QRId qrId) throws IOException {
        return GetRequester.request(domain + QR_INFO_PATH, qrId, secretKey);
    }

    public Response getPaymentInfo(final QRId qrId) throws IOException {
        return GetRequester.request(domain + PAYMENT_INFO_PATH, qrId, secretKey);
    }

    public Response getRefundInfo(final String refundId) throws IOException {
        return GetRequester.request(domain + REFUND_INFO_PATH, refundId, secretKey);
    }

}
