package raiffeisen.sbp.sdk;

import raiffeisen.sbp.sdk.model.Response;

import java.util.HashMap;
import java.util.Map;

public class TestData {
    public static final String DOMAIN = "https://test.ecom.raiffeisen.ru";

    public static final String REGISTER_PATH = "/api/sbp/v1/qr/register";
    public static final String QR_INFO_PATH = "/api/sbp/v1/qr/123/info";
    public static final String PAYMENT_INFO_PATH = "/api/sbp/v1/qr/321/payment-info";
    public static final String REFUND_PATH = "/api/sbp/v1/refund";
    public static final String REFUND_INFO_PATH = "/api/sbp/v1/refund/?";

    public static final Map<String, String> headers = new HashMap<String, String>();


    public static final Response successQRUrl = new Response(200,
            "{\"code\": \"SUCCESS\"," +
                    "\"qrId\": \"qrId\"," +
                    "\"payload\": \"payloadUrl\"," +
                    "\"qrUrl\": \"qrUrl\" }");

    public static final Response successPaymentInfo = new Response(200,
            "{\"additionalInfo\": \"addInfo\"," +
                    "\"amount\": 111," +
                    "\"code\": \"SUCCESS\"," +
                    "\"createDate\": \"2020-01-31T09:14:38.107227+03:00\"," +
                    "\"currency\": \"RUB\"," +
                    "\"merchantId\": 456," +
                    "\"order\": \"282a60f8-dd75-4286-bde0-af321dd081b3\"," +
                    "\"paymentStatus\": \"NO_INFO\"," +
                    "\"qrId\": \"AD100051KNSNR64I98CRUJUASC9M72QT\"," +
                    "\"sbpMerchantId\": \"MA0000000553\"," +
                    "\"transactionDate\": \"2019-07-11T17:45:13.109227+03:00\"," +
                    "\"transactionId\": 23 }");

    static {
        headers.put("content-type", "application/json");
        headers.put("charset", "UTF-8");
        headers.put("Authorization", "Bearer secretKey");
    }

}
