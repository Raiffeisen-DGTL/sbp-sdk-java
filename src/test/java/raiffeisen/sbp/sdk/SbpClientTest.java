package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import raiffeisen.sbp.sdk.utils.StatusCodes;
import raiffeisen.sbp.sdk.utils.TestData;
import raiffeisen.sbp.sdk.web.ApacheClient;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class SbpClientTest {

    @Mock
    private ApacheClient webclient;

    @Test
    void success_registerQR() throws Exception {
        ArgumentCaptor<Map> headersCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(webclient.request(eq("POST"),
                eq(TestData.SANDBOX + TestData.REGISTER_PATH),
                headersCaptor.capture(),
                bodyCaptor.capture())).
                thenReturn(TestData.QR_URL);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, "secretKey", webclient);

        QRInfo qrInfo = QRInfo.creator().
                order("123-123-123").
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE).
                qrType(QRType.QRStatic).
                sbpMerchantId(TestData.SBP_MERCHANT_ID).
                create();

        QRUrl response = client.registerQR(qrInfo);

        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
        assertEquals(TestData.HEADERS, headersCaptor.getValue());
        assertEquals(TestData.QR_INFO_BODY, bodyCaptor.getValue());
    }

    @Test
    void success_getQRInfo() throws Exception {
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(webclient.request(eq("GET"),
                eq(TestData.SANDBOX + TestData.QR_INFO_PATH),
                any(),
                bodyCaptor.capture())).
                thenReturn(TestData.QR_URL);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, "secretKey", webclient);

        QRId qrId = QRId.creator().qrId("123").create();

        QRUrl response = client.getQRInfo(qrId);

        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
        assertEquals(TestData.NULL_BODY, bodyCaptor.getValue());
    }

    @Test
    void success_getPaymentInfo() throws Exception {
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(webclient.request(eq("GET"),
                eq(TestData.SANDBOX + TestData.PAYMENT_INFO_PATH),
                any(),
                bodyCaptor.capture())).
                thenReturn(TestData.PAYMENT_INFO);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, "secretKey", webclient);

        QRId id = QRId.creator().qrId("123").create();

        PaymentInfo response = client.getPaymentInfo(id);

        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
        assertEquals(TestData.NULL_BODY, bodyCaptor.getValue());
    }

    @Test
    void success_refundPayment() throws Exception {
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(webclient.request(eq("POST"),
                eq(TestData.SANDBOX + TestData.REFUND_PATH),
                any(),
                bodyCaptor.capture())).
                thenReturn(TestData.REFUND_STATUS);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, "secretKey", webclient);

        RefundInfo refundInfo = RefundInfo.creator().
                refundId("12345").
                amount(BigDecimal.TEN).
                order("123-123").
                transactionId(111).
                create();

        RefundStatus refundStatus = client.refundPayment(refundInfo);

        assertEquals(StatusCodes.SUCCESS.getMessage(), refundStatus.getCode());
        assertEquals(TestData.REFUND_PAYMENT, bodyCaptor.getValue());
    }

    @Test
    void success_getRefundInfo() throws Exception {
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(webclient.request(eq("GET"),
                eq(TestData.SANDBOX + TestData.REFUND_INFO_PATH),
                any(),
                bodyCaptor.capture())).
                thenReturn(TestData.REFUND_STATUS);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, "secretKey", webclient);

        RefundStatus refundStatus = client.getRefundInfo("123");

        assertEquals(StatusCodes.SUCCESS.getMessage(), refundStatus.getCode());
        assertEquals(TestData.NULL_BODY, bodyCaptor.getValue());
    }
}
