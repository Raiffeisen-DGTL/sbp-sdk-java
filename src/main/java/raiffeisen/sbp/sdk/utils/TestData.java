package raiffeisen.sbp.sdk.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import raiffeisen.sbp.sdk.model.Response;

import java.math.BigDecimal;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class TestData {
    public static final String SANDBOX = "https://test.ecom.raiffeisen.ru";
    public static final String PAYMENT_URL = "https://test.ecom.raiffeisen.ru/sbp/v1/transaction/*/status?status=SUCCESS";

    private static final String BASE_PATH = "/api/sbp/v1/"; //TODO move urls to config for the whole project
    public static final String REGISTER_PATH = BASE_PATH + "qr/register";
    public static final String QR_INFO_PATH = BASE_PATH + "qr/123/info";
    public static final String PAYMENT_INFO_PATH = BASE_PATH + "qr/123/payment-info";
    public static final String REFUND_PATH = BASE_PATH + "refund";
    public static final String REFUND_INFO_PATH = BASE_PATH + "refund/123";

    public static final String SBP_MERCHANT_ID = "MA0000000552";
    public static final String SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJNQTAwMDAwMDA1NTIiLCJqdGkiOiI0ZDFmZWIwNy0xZDExLTRjOWEtYmViNi" +
            "1kZjUwY2Y2Mzc5YTUifQ.pxU8KYfqbVlxvQV7wfbGpsu4AX1QoY26FqBiuNuyT-s";

    public static final Map<String, String> HEADERS_AUTH =
            Map.of("content-type", "application/json",
                    "charset", "UTF-8",
                    "Authorization", "Bearer secretKey");

    public static final Map<String, String> HEADERS =
            Map.of("content-type", "application/json",
                    "charset", "UTF-8");

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

    public static final String QR_INFO_BODY = "{\"createDate\":\"" + TestData.DATE_CREATE_DATE + "\","+
            "\"order\":\"123-123-123\"," +
            "\"qrType\":\"QRStatic\"," +
            "\"sbpMerchantId\":\"MA0000000552\"," +
            "\"qrExpirationDate\":\"" + TestData.DATE_QR_EXPIRATION_DATE + "\"" +
            "}";

    public static final String NULL_BODY = null;

    public static final String REFUND_PAYMENT = "{\"amount\":10," +
            "\"order\":\"123-123\"," +
            "\"refundId\":\"12345\"," +
            "\"transactionId\":111}";

    public static final String NOTIFICATION = "{\"transactionId\":17998," +
            "\"qrId\":\"AS1000408BSPMRDI8IHBGO4DFQAISU9O\"," +
            "\"sbpMerchantId\":\"MA0000000552\"," +
            "\"merchantId\":123," +
            "\"amount\":101.01," +
            "\"currency\":\"RUB\"," +
            "\"transactionDate\":\"2020-07-24T17:20:00.999232+03:00\"," +
            "\"paymentStatus\":\"SUCCESS\"," +
            "\"additionalInfo\":null," +
            "\"order\":\"dfe0ff08-4796-46bb-a9fb-93fcd99ce748\"," +
            "\"createDate\":\"2020-07-24T17:19:58+03:00\"}";

    public static final String API_SIGNATURE =
            "1eca6a084ff8a5b4f5740e0eeab2a72d8ff981bce5b5dea75a53bf44944a8c8d";

    public static final BigDecimal NOTIFICATION_AMOUNT = BigDecimal.valueOf(101.01);
    public static final String NOTIFICATION_ORDER = "dfe0ff08-4796-46bb-a9fb-93fcd99ce748";
    public static final String NOTIFICATION_PAYMENT_STATUS = "SUCCESS";
    public static final String NOTIFICATION_TRANSACTION_DATE = "2020-07-24T17:20:00.999232+03:00";
    public static final String NOTIFICATION_TEST_SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTAwMDAwMDA1NTIiLCJqdGkiOiIwZTRhODI2ZC0zMTk3LTQ5YWUtYjRlYS0zZDllOGRkODIyOGEifQ.Q-AVaDBzvfkz6I8ZCVgvGIWpWTUgotRDmTcs4ysR0Qc";
    public static final String DATE_CREATE_DATE = "2019-07-22T09:14:38.107227+03:00";
    public static final String DATE_CREATE_DATE_PLUS_DAY = "2019-07-23T09:14:38.107227+03:00";
    public static final String DATE_CREATE_DATE_PLUS_MONTH = "2019-08-22T09:14:38.107227+03:00";
    public static final String DATE_QR_EXPIRATION_DATE = "2020-07-22T09:14:38.107227+03:00";
}
