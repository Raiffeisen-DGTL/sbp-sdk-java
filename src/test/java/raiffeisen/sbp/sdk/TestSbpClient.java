package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.web.ApacheClient;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class TestSbpClient {

    @Mock private ApacheClient webclient;

    @Captor
    ArgumentCaptor<Map> headersCaptor = ArgumentCaptor.forClass(Map.class);

    @Captor
    ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

    @BeforeEach
    public void init() {
        webclient = Mockito.mock(ApacheClient.class);
    }

    @Test
    public void success_registerQR() throws Exception {
        // arrange
        Mockito.when(webclient.request(eq("POST"),
                eq( TestData.DOMAIN + TestData.REGISTER_PATH),
                headersCaptor.capture(),
                bodyCaptor.capture())).
                thenReturn(TestData.QR_URL);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"secretKey", webclient);

        QRInfo qrInfo = QRInfo.creator().
                createDate("timestamp").
                order("123-123-123").
                qrType(QRType.QRStatic).
                sbpMerchantId(TestData.SBP_MERCHANT_ID).
                create();

        // act
        QRUrl response = client.registerQR(qrInfo);

        // assert
        assertEquals("SUCCESS", response.getCode(),"Code is not SUCCESS");
        assertEquals(headersCaptor.getValue(), TestData.HEADERS, "Headers are not equal");
        assertEquals(bodyCaptor.getValue(), TestData.QR_INFO_BODY, "Bodies of request are not equal");
    }

    @Test
    public void success_getQRInfo() throws Exception {
        // arrange
        Mockito.when(webclient.request(eq("GET"),
                eq(TestData.DOMAIN + TestData.QR_INFO_PATH),
                headersCaptor.capture(),
                bodyCaptor.capture())).
                thenReturn(TestData.QR_URL);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"secretKey", webclient);

        QRId qrId = QRId.creator().qrId("123").create();

        // act
        QRUrl response = client.getQRInfo(qrId);

        // assert
        assertEquals("SUCCESS", response.getCode(), "Code is not SUCCESS");
        assertEquals(headersCaptor.getValue(), TestData.HEADERS_AUTH, "Headers are not equal");
        assertEquals(bodyCaptor.getValue(), TestData.NULL_BODY, "Bodies of request are not equal");
    }

    @Test
    public void success_getPaymentInfo() throws Exception {
        // arrange
        Mockito.when(webclient.request(eq("GET"),
                eq(TestData.DOMAIN + TestData.PAYMENT_INFO_PATH),
                headersCaptor.capture(),
                bodyCaptor.capture())).
                thenReturn(TestData.PAYMENT_INFO);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"secretKey", webclient);

        QRId id = QRId.creator().qrId("123").create();

        // act
        PaymentInfo response = client.getPaymentInfo(id);

        // assert
        assertEquals("SUCCESS", response.getCode(), "Response code is not correct");
        assertEquals(headersCaptor.getValue(), TestData.HEADERS_AUTH, "Headers are not equal");
        assertEquals(bodyCaptor.getValue(), TestData.NULL_BODY, "Bodies of request are not equal");
    }

    @Test
    public void success_refundPayment() throws Exception {
        // arrange
        Mockito.when(webclient.request(eq("POST"),
                eq(TestData.DOMAIN + TestData.REFUND_PATH),
                headersCaptor.capture(),
                bodyCaptor.capture())).
                thenReturn(TestData.REFUND_STATUS);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"secretKey", webclient);

        RefundInfo refundInfo = RefundInfo.creator().
                refundId("12345").
                amount(BigDecimal.TEN).
                order("123-123").
                transactionId(111).
                create();

        // act
        RefundStatus refundStatus = client.refundPayment(refundInfo);

        // assert
        assertEquals("SUCCESS", refundStatus.getCode(), "Response code is not correct");
        assertEquals(headersCaptor.getValue(), TestData.HEADERS_AUTH, "Headers are not equal");
        assertEquals(bodyCaptor.getValue(), TestData.REFUND_PAYMENT, "Bodies of request are not equal");
    }

    @Test
    public void success_getRefundInfo() throws Exception {
        // arrange
        Mockito.when(webclient.request(eq("GET"),
                eq(TestData.DOMAIN + TestData.REFUND_INFO_PATH),
                headersCaptor.capture(),
                bodyCaptor.capture())).
                thenReturn(TestData.REFUND_STATUS);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"secretKey", webclient);

        // act
        RefundStatus refundStatus = client.getRefundInfo("123");

        assertEquals("SUCCESS", refundStatus.getCode(), "Response code is not correct");
        assertEquals(headersCaptor.getValue(), TestData.HEADERS_AUTH, "Headers are not equal");
        assertEquals(bodyCaptor.getValue(), TestData.NULL_BODY, "Bodies of request are not equal");
    }
}
