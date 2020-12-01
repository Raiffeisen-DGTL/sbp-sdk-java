package raiffeisen.sbp.sdk.client;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRStatic;
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

@Tag("unit")
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

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        QRStatic qrStatic = new QRStatic("123-123-123");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE);
        qrStatic.setSbpMerchantId(TestData.TEST_SBP_MERCHANT_ID);

        QRUrl response = client.registerQR(qrStatic);

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

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        QRId qrId = new QRId(TestData.TEST_QR_ID);

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

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        QRId qrId = new QRId(TestData.TEST_QR_ID);

        PaymentInfo response = client.getPaymentInfo(qrId);

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

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TestData.TEST_SBP_MERCHANT_ID,"secretKey", webclient);

        RefundInfo refundInfo = new RefundInfo(BigDecimal.TEN,"123-123","12345");
        refundInfo.setTransactionId(111);

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

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        RefundId refundId = new RefundId(TestData.TEST_REFUND_ID);

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

        SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TestData.TEST_SBP_MERCHANT_ID,"secretKey", webclient);

        QRDynamic qrDynamic = new QRDynamic("", BigDecimal.ONE);

        SbpException thrown = assertThrows(SbpException.class,
                () -> client.registerQR(qrDynamic));
        assertEquals(TestData.QR_DYNAMIC_CODE_WITHOUT_AMOUNT_ERROR, thrown.getMessage());
    }
}
