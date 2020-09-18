package raiffeisen.sbp.sdk;

import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.util.HashMap;
import java.util.Map;

public class TestData {
    public static final String DOMAIN = "https://test.ecom.raiffeisen.ru";

    public static final String REGISTER_PATH = "/api/sbp/v1/qr/register";
    public static final String QR_INFO_PATH = "/api/sbp/v1/qr/123/info";
    public static final String PAYMENT_INFO_PATH = "/api/sbp/v1/qr/123/payment-info";
    public static final String REFUND_PATH = "/api/sbp/v1/refund";
    public static final String REFUND_INFO_PATH = "/api/sbp/v1/refund/123";

    public static final String SBP_MERCHANT_ID = "MA0000000552";

    public static final Map<String, String> HEADERS_AUTH = new HashMap<String, String>();

    public static final Map<String, String> HEADERS = new HashMap<String, String>();

    public static final Response QR_URL = new Response(200,
            "{\"code\": \"SUCCESS\"," +
                    "\"qrId\": \"qrId\"," +
                    "\"payload\": \"payloadUrl\"," +
                    "\"qrUrl\": \"qrUrl\" }");

    public static final Response PAYMENT_INFO = new Response(200,
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

    public static final Response REFUND_STATUS = new Response(200,
            "{ \"code\": \"SUCCESS\"," +
            "\"amount\": 150," +
            "\"refundStatus\": \"IN_PROGRESS\"}");

    public static final String QR_INFO_BODY = "{\"createDate\":\"timestamp\"," +
            "\"order\":\"123-123-123\"," +
            "\"qrType\":\"QRStatic\"," +
            "\"sbpMerchantId\":\"MA0000000552\"}";

    public static final String NULL_BODY = null;

    public static final String REFUND_PAYMENT = "{\"amount\":10," +
            "\"order\":\"123-123\"," +
            "\"refundId\":\"12345\"," +
            "\"transactionId\":111}";

    static {
        HEADERS_AUTH.put("content-type", "application/json");
        HEADERS_AUTH.put("charset", "UTF-8");
        HEADERS_AUTH.put("Authorization", "Bearer secretKey");

        HEADERS.put("content-type", "application/json");
        HEADERS.put("charset", "UTF-8");
    }

}
