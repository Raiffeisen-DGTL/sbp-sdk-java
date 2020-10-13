package raiffeisen.sbp.sdk.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.model.out.RefundId;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.data.StatusCodes;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.web.ApacheClient;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        QRInfo qrInfo = QRInfo.builder().
                order("123-123-123").
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE).
                qrType(QRType.QRStatic).
                sbpMerchantId(TestData.SBP_MERCHANT_ID).
                build();

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

        QRId qrId = QRId.builder().qrId(TestData.TEST_ID).build();

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

        QRId id = QRId.builder().qrId(TestData.TEST_ID).build();

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

        RefundInfo refundInfo = RefundInfo.builder().
                refundId("12345").
                amount(BigDecimal.TEN).
                order("123-123").
                transactionId(111).
                build();

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

        RefundId refundId = RefundId.builder().refundId(TestData.TEST_ID).build();

        RefundStatus refundStatus = client.getRefundInfo(refundId);

        assertEquals(StatusCodes.SUCCESS.getMessage(), refundStatus.getCode());
        assertEquals(TestData.NULL_BODY, bodyCaptor.getValue());
    }

    @Test
    void fail_throwSbpException() throws Exception {
        Mockito.when(webclient.request(any(),
                any(),
                any(),
                any())).thenReturn(TestData.QR_DYNAMIC_CODE_WITHOUT_AMOUNT_RESPONSE);

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, "secretKey", webclient);

        QRInfo qrInfo = QRInfo.builder().qrType(QRType.QRDynamic).build();

        SbpException thrown = assertThrows(SbpException.class,
                () -> client.registerQR(qrInfo));
        assertEquals(TestData.QR_DYNAMIC_CODE_WITHOUT_AMOUNT_ERROR, thrown.getMessage());
    }
}
