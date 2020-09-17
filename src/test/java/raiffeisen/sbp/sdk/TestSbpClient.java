package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.web.ApacheClient;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class TestSbpClient {

    @Mock private ApacheClient webclient;

    private final String TEST_SBP_MERCHANT_ID = "MA0000000552";

    public static final String DOMAIN = "https://test.ecom.raiffeisen.ru";

    private static final String REGISTER_PATH = "/api/sbp/v1/qr/register";
    private static final String QR_INFO_PATH = "/api/sbp/v1/qr/123/info";
    private static final String PAYMENT_INFO_PATH = "/api/sbp/v1/qr/321/payment-info";
    private static final String REFUND_PATH = "/api/sbp/v1/refund";
    private static final String REFUND_INFO_PATH = "/api/sbp/v1/refund/?";

    @BeforeEach
    public void init() {
        webclient = Mockito.mock(ApacheClient.class);
    }

    @Test
    public void positiveRegisterQR() throws Exception {
        // arrange
        prepareMockPositiveRegisterQR();

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"", webclient);

        QRInfo QR = QRInfo.creator().
                createDate(getCreateDate()).
                order(getOrderInfo()).
                qrType(QRType.QRStatic).
                sbpMerchantId(TEST_SBP_MERCHANT_ID).
                create();

        // act
        QRUrl response = client.registerQR(QR);

        // assert
        assertEquals("SUCCESS", response.getCode(),"Code is not SUCCESS");
    }

    @Test
    public void positiveGetQRInfo() throws Exception {
        // arrange
        prepareMockPositiveGetQRInfo();

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"", webclient);

        QRId qrId = QRId.creator().qrId("123").create();

        // act
        QRUrl response = client.getQRInfo(qrId);

        // assert
        assertEquals("SUCCESS", response.getCode(), "Code is not SUCCESS");
    }

    @Test
    public void positiveGetPaymentInfo() throws Exception {
        // arrange
        prepareMockPositiveGetPaymentInfo();

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"secretKey", webclient);

        QRId id = QRId.creator().qrId("321").create();

        // act
        PaymentInfo response = client.getPaymentInfo(id);

        // assert
        assertEquals("SUCCESS", response.getCode(), "Response code is not correct");
        assertEquals(456, response.getMerchantId(), "MerchantID is not correct");

    }

    private String getOrderInfo() {
        return UUID.randomUUID().toString();
    }

    private String getCreateDate() {
        String timestamp = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString();
        return timestamp.substring(0,timestamp.indexOf("["));
    }

    private void prepareMockPositiveRegisterQR() throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("charset", "UTF-8");
        headers.put("Authorization", "Bearer ");

        Response successRegisterQR = new Response(200,
                "{\"code\": \"SUCCESS\"," +
                        "\"qrId\": \"qrId\"," +
                        "\"payload\": \"payloadUrl\"," +
                        "\"qrUrl\": \"qrUrl\" }");

        Mockito.when(webclient.request(eq("POST"),
                eq(DOMAIN + REGISTER_PATH),
                any(), // this is the place for eq(headers)
                any())).
                thenReturn(successRegisterQR);
    }

    private void prepareMockPositiveGetQRInfo() throws IOException {
        Response response = new Response(200,
                "{\"code\": \"SUCCESS\"," +
                        "\"qrId\": \"qrId\"," +
                        "\"payload\": \"payloadUrl\"," +
                        "\"qrUrl\": \"qrUrl\" }");

        Mockito.when(webclient.request(eq("GET"),
                eq(DOMAIN + QR_INFO_PATH),
                any(),
                any())).
                thenReturn(response);

    }

    private void prepareMockPositiveGetPaymentInfo() throws IOException {
        Response response = new Response(200,
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

        Response badResponse = new Response(400,"");

        Mockito.when(webclient.request(eq("GET"),
                any(),
                any(),
                any())).
                thenReturn(response);
    }
}
