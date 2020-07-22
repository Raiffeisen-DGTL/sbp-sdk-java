package raiffeisen.sbp.sdk.client;

import raiffeisen.sbp.sdk.json.JsonBuilder;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.io.IOException;

public class SbpClient {
    public static final String URL_REGISTER_TEST = "https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/register";
    public static final String URL_QR_INFO_TEST = "https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/?/info";
    public static final String URL_PAYMENT_INFO_TEST = "https://test.ecom.raiffeisen.ru/api/sbp/v1/qr/?/payment-info";
    public static final String URL_REFUND_TEST = "https://test.ecom.raiffeisen.ru/api/sbp/v1/refund";
    public static final String URL_REFUND_INFO_TEST = "https://test.ecom.raiffeisen.ru/api/sbp/v1/refund/?";

    public static final String URL_REGISTER = "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/register";
    public static final String URL_QR_INFO = "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/?/info";
    public static final String URL_PAYMENT_INFO = "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/?/payment-info";
    public static final String URL_REFUND = "https://e-commerce.raiffeisen.ru/api/sbp/v1/refund";
    public static final String URL_REFUND_INFO = "https://e-commerce.raiffeisen.ru/api/sbp/v1/refund/?";
    public static Response registerQR(final String url, QRInfo qr) throws IOException {
        return PostRequester.request(url, JsonBuilder.fromObject(qr), null);
    }

}
