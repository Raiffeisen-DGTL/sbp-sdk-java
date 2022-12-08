package raiffeisen.sbp.sdk.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import raiffeisen.sbp.sdk.client.PropertiesLoader;
import raiffeisen.sbp.sdk.model.Response;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestData {
    public static final String SANDBOX = "https://pay-test.raif.ru";
    public static final String PAYMENT_URL = "https://pay-test.raif.ru/api/nspc-mock/v01/rfuture/payment/init";
    public static final String TEST_SBP_MERCHANT_ID = "MA341037";

    public static final String TEST_NOTIFICATION_SBP_MERCHANT_ID = "MA0000000552";

    public static final String SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTM0MTAzNyIsImp0aSI6IjllMjY2YmIwLTA4OTgtNGMwMC04YzIwLTk2NDNmMmQxMjNkMiJ9.1NbE__vzECn9UZBJIincgnoOP0h1llZtGxikZJOqkpc";
    public static final Map<String, String> HEADERS =
            Map.of("content-type", "application/json",
                    "charset", "UTF-8");
    public static final String QR_URL_QR_ID = "qrId";
    public static final String QR_URL_PAYLOAD = "payloadUrl";
    public static final String QR_URL_URL = "qrUrl";
    public static final Response QR_URL = new Response(200,
            "{\"code\": \"SUCCESS\"," +
                    "\"qrId\": \"" + QR_URL_QR_ID + "\"," +
                    "\"payload\": \"" + QR_URL_PAYLOAD + "\"," +
                    "\"qrUrl\": \"" + QR_URL_URL + "\" }");
    public static final String PAYMENT_INFO_ADDITIONAL_INFO = "addInfo";
    public static final String PAYMENT_INFO_AMOUNT = "111";
    public static final String PAYMENT_INFO_CREATE_DATE = "2020-01-31T09:14:38.107227+03:00";
    public static final String PAYMENT_INFO_CURRENCY = "RUB";
    public static final String PAYMENT_INFO_MERCHANT_ID = "456";
    public static final String PAYMENT_INFO_ORDER = "282a60f8-dd75-4286-bde0-af321dd081b3";
    public static final String PAYMENT_INFO_PAYMENT_STATUS = "NO_INFO";
    public static final String PAYMENT_INFO_QR_ID = "AD100051KNSNR64I98CRUJUASC9M72QT";
    public static final String PAYMENT_INFO_TRANSACTION_DATE = "2019-07-11T17:45:13.109227+03:00";
    public static final Response PAYMENT_INFO = new Response(200,
            "{\"additionalInfo\": \"" + PAYMENT_INFO_ADDITIONAL_INFO + "\"," +
                    "\"amount\": " + PAYMENT_INFO_AMOUNT + "," +
                    "\"code\": \"SUCCESS\"," +
                    "\"createDate\": \"" + PAYMENT_INFO_CREATE_DATE + "\"," +
                    "\"currency\": \"" + PAYMENT_INFO_CURRENCY + "\"," +
                    "\"merchantId\": " + PAYMENT_INFO_MERCHANT_ID + "," +
                    "\"order\": \"" + PAYMENT_INFO_ORDER + "\"," +
                    "\"paymentStatus\": \"" + PAYMENT_INFO_PAYMENT_STATUS + "\"," +
                    "\"qrId\": \"" + PAYMENT_INFO_QR_ID + "\"," +
                    "\"sbpMerchantId\": \"MA341037\"," +
                    "\"transactionDate\": \"" + PAYMENT_INFO_TRANSACTION_DATE + "\"," +
                    "\"transactionId\": 23 }");
    public static final String REFUND_STATUS_AMOUNT = "150";
    public static final String REFUND_STATUS_STATUS = "IN_PROGRESS";
    public static final Response REFUND_STATUS = new Response(200,
            "{ \"code\": \"SUCCESS\"," +
                    "\"amount\": " + REFUND_STATUS_AMOUNT + "," +
                    "\"refundStatus\": \"" + REFUND_STATUS_STATUS + "\"}");
    public static final Response QR_DYNAMIC_CODE_WITHOUT_AMOUNT_RESPONSE = new Response(200,
            "{\"code\":\"ERROR.DYNAMIC_QR_WITHOUT_AMOUNT\",\"message\":\"Не передана сумма для динамического QR-кода\"}");
    public static final int UNSUPPORTED_RESPONSE1_HTTPCODE = 400;
    public static final String UNSUPPORTED_RESPONSE1_MESSAGE = "Error execution request";
    public static final Response UNSUPPORTED_RESPONSE1 =
            new Response(UNSUPPORTED_RESPONSE1_HTTPCODE, UNSUPPORTED_RESPONSE1_MESSAGE);
    public static final int UNSUPPORTED_RESPONSE2_HTTPCODE = 200;
    public static final String UNSUPPORTED_RESPONSE2_MESSAGE = "{\"code\":\"BAD_MESSAGE\"}";
    public static final Response UNSUPPORTED_RESPONSE2 = new Response(UNSUPPORTED_RESPONSE2_HTTPCODE, UNSUPPORTED_RESPONSE2_MESSAGE);
    public static final int UNSUPPORTED_RESPONSE3_HTTPCODE = 404;
    public static final String UNSUPPORTED_RESPONSE3_MESSAGE =
            "{\"timestamp\":\"2020-12-03T17:56:36.546+0000\"," +
                    "\"status\":404,\"error\":\"Not Found\"," +
                    "\"message\":\"No message available\"," +
                    "\"path\":\"/sbp/v1/qr/register1\"}";
    public static final Response UNSUPPORTED_RESPONSE3 = new Response(UNSUPPORTED_RESPONSE3_HTTPCODE, UNSUPPORTED_RESPONSE3_MESSAGE);
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
    public static final String NOTIFICATION_TEST_SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTAwMDAwMDA1NTIiLCJqdGkiOiIwZTRhODI2ZC0zMTk3LTQ5YWUtYjRlYS0zZDllOGRkODIyOGEifQ.Q-AVaDBzvfkz6I8ZCVgvGIWpWTUgotRDmTcs4ysR0Qc";
    public static final String DATE_CREATE_DATE = "2019-07-22T09:14:38.107227+03:00";
    public static final String DATE_CREATE_DATE_PLUS_DAY = "2019-07-23T09:14:38.107227+03:00";
    public static final String DATE_CREATE_DATE_PLUS_MONTH = "2019-08-22T09:14:38.107227+03:00";
    public static final String DATE_QR_EXPIRATION_DATE = "2020-07-22T09:14:38.107227+03:00";
    public static final String QR_INFO_BODY = "{" +
            "\"order\":\"123-123-123\"," +
            "\"qrType\":\"QRStatic\"," +
            "\"createDate\":\"" + TestData.DATE_CREATE_DATE + "\"," +
            "\"qrExpirationDate\":\"" + TestData.DATE_QR_EXPIRATION_DATE + "\"," +
            "\"sbpMerchantId\":\"MA341037\"" +
            "}";
    public static final String MISSING_REFUND_ID_ERROR_CODE = "ERROR.INVALID_REQUEST";
    public static final String MISSING_REFUND_ID_ERROR_MESSAGE = "Не передан обязательный параметр";
    public static final String QR_CODE_NOT_FOUND_ERROR_CODE = "ERROR.NOT_FOUND";
    public static final String QR_CODE_NOT_FOUND_ERROR_MESSAGE = "QR-код не найден у данного партнера";
    public static final String QR_DYNAMIC_CODE_WITHOUT_AMOUNT_ERROR_CODE = "ERROR.DYNAMIC_QR_WITHOUT_AMOUNT";
    public static final String QR_DYNAMIC_CODE_WITHOUT_AMOUNT_ERROR_MESSAGE = "Не передана сумма для динамического QR-кода";
    public static final String TEST_REFUND_ID = "123";
    public static final String TEST_QR_ID = "123";
    public static final String REGISTER_PATH = PropertiesLoader.REGISTER_PATH;
    public static final String QR_INFO_PATH = PropertiesLoader.QR_INFO_PATH.replace("?", TEST_QR_ID);
    public static final String PAYMENT_INFO_PATH = PropertiesLoader.PAYMENT_INFO_PATH.replace("?", TEST_QR_ID);
    public static final String REFUND_PATH = PropertiesLoader.REFUND_PATH;
    public static final String REFUND_INFO_PATH = PropertiesLoader.REFUND_INFO_PATH.replace("?", TEST_REFUND_ID);
    public static final Map<String, String> MAP_HEADERS = Map.of("content-type", "application/json",
            "charset", "UTF-8");
    public static final String RESPONSE_BODY = "{\"code\",\"SUCCESS\"}";

    public static String getNotFoundRefundError(String refundId) {
        String st = "Возврат с refundId %s не найден";
        return String.format(st, refundId);
    }

}
