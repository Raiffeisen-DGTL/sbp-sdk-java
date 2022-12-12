package raiffeisen.sbp.sdk.client;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.model.out.QRStatic;
import raiffeisen.sbp.sdk.model.out.RefundId;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.web.SdkHttpClient;

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
    private SdkHttpClient webclient;

    @Test
    void success_registerQR() throws Exception {
        ArgumentCaptor<Map> headersCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(webclient.postRequest(
                        eq(TestData.SANDBOX + TestData.REGISTER_PATH),
                        headersCaptor.capture(),
                        bodyCaptor.capture())).
                thenReturn(TestData.QR_URL);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        QRStatic qrStatic = new QRStatic("123-123-123");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE);

        QRUrl response = client.registerQR(qrStatic);


        assertEquals(TestData.HEADERS, headersCaptor.getValue());
        assertEquals(TestData.QR_INFO_BODY, bodyCaptor.getValue());

        assertEquals(TestData.QR_URL_QR_ID, response.getQrId());
        assertEquals(TestData.QR_URL_PAYLOAD, response.getPayload());
        assertEquals(TestData.QR_URL_URL, response.getQrUrl());
    }

    @Test
    void success_getQRInfo() throws Exception {
        Mockito.when(webclient.getRequest(
                        eq(TestData.SANDBOX + TestData.QR_INFO_PATH),
                        any())).
                thenReturn(TestData.QR_URL);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        String id = TestData.TEST_QR_ID;

        QRUrl response = client.getQRInfo(id);

        assertEquals(TestData.QR_URL_QR_ID, response.getQrId());
        assertEquals(TestData.QR_URL_PAYLOAD, response.getPayload());
        assertEquals(TestData.QR_URL_URL, response.getQrUrl());
    }

    @Test
    void success_getPaymentInfo() throws Exception {
        Mockito.when(webclient.getRequest(
                        eq(TestData.SANDBOX + TestData.PAYMENT_INFO_PATH),
                        any())).
                thenReturn(TestData.PAYMENT_INFO);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        String id = TestData.TEST_QR_ID;

        PaymentInfo response = client.getPaymentInfo(id);

        assertEquals(TestData.PAYMENT_INFO_ADDITIONAL_INFO, response.getAdditionalInfo());
        assertEquals(TestData.PAYMENT_INFO_AMOUNT, response.getAmount().toString());
        assertEquals(TestData.PAYMENT_INFO_CREATE_DATE, response.getCreateDate().toString());
        assertEquals(TestData.PAYMENT_INFO_CURRENCY, response.getCurrency());
        assertEquals(TestData.PAYMENT_INFO_TRANSACTION_DATE, response.getTransactionDate().toString());
        assertEquals(TestData.PAYMENT_INFO_ORDER, response.getOrder());
        assertEquals(TestData.PAYMENT_INFO_PAYMENT_STATUS, response.getPaymentStatus());
    }

    @Test
    void success_refundPayment() throws Exception {
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(webclient.postRequest(
                        eq(TestData.SANDBOX + TestData.REFUND_PATH),
                        any(),
                        bodyCaptor.capture())).
                thenReturn(TestData.REFUND_STATUS);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        RefundInfo refundInfo = new RefundInfo(BigDecimal.TEN, "123-123", "12345");
        refundInfo.setTransactionId(111);

        RefundStatus refundStatus = client.refundPayment(refundInfo);

        assertEquals(TestData.REFUND_PAYMENT, bodyCaptor.getValue());
        assertEquals(TestData.REFUND_STATUS_AMOUNT, refundStatus.getAmount().toString());
        assertEquals(TestData.REFUND_STATUS_STATUS, refundStatus.getRefundStatus());
    }

    @Test
    void success_getRefundInfo() throws Exception {
        Mockito.when(webclient.getRequest(
                        eq(TestData.SANDBOX + TestData.REFUND_INFO_PATH),
                        any())).
                thenReturn(TestData.REFUND_STATUS);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        RefundId refundId = new RefundId(TestData.TEST_REFUND_ID);

        RefundStatus refundStatus = client.getRefundInfo(refundId);

        assertEquals(TestData.REFUND_STATUS_AMOUNT, refundStatus.getAmount().toString());
        assertEquals(TestData.REFUND_STATUS_STATUS, refundStatus.getRefundStatus());
    }

    @Test
    void fail_throwSbpException() throws Exception {
        Mockito.when(webclient.postRequest(any(),
                any(),
                any())).thenReturn(TestData.QR_DYNAMIC_CODE_WITHOUT_AMOUNT_RESPONSE);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        QRDynamic qrDynamic = new QRDynamic("", BigDecimal.ONE);

        SbpException thrown = assertThrows(SbpException.class,
                () -> client.registerQR(qrDynamic));
        assertEquals(TestData.QR_DYNAMIC_CODE_WITHOUT_AMOUNT_ERROR_CODE, thrown.getCode());
        assertEquals(TestData.QR_DYNAMIC_CODE_WITHOUT_AMOUNT_ERROR_MESSAGE, thrown.getMessage());
    }

    @Test
    void fail_throwContractViolationException() throws Exception {
        Mockito.when(webclient.postRequest(any(),
                any(),
                any())).thenReturn(TestData.UNSUPPORTED_RESPONSE1);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        QRDynamic qrDynamic = new QRDynamic("", BigDecimal.ONE);

        ContractViolationException thrown = assertThrows(ContractViolationException.class,
                () -> client.registerQR(qrDynamic));
        assertEquals(TestData.UNSUPPORTED_RESPONSE1_HTTPCODE, thrown.getHttpCode());
        assertEquals(TestData.UNSUPPORTED_RESPONSE1_MESSAGE, thrown.getMessage());
    }

    @Test
    void fail_throwContractViolationExceptionWithCode() throws Exception {
        Mockito.when(webclient.postRequest(any(),
                any(),
                any())).thenReturn(TestData.UNSUPPORTED_RESPONSE2);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        QRDynamic qrDynamic = new QRDynamic("", BigDecimal.ONE);

        ContractViolationException thrown = assertThrows(ContractViolationException.class,
                () -> client.registerQR(qrDynamic));
        assertEquals(TestData.UNSUPPORTED_RESPONSE2_HTTPCODE, thrown.getHttpCode());
        assertEquals(TestData.UNSUPPORTED_RESPONSE2_MESSAGE, thrown.getMessage());
    }

    @Test
    void fail_throwContractViolationExceptionWhenCodeIsNull() throws Exception {
        Mockito.when(webclient.postRequest(any(),
                any(),
                any())).thenReturn(TestData.UNSUPPORTED_RESPONSE3);

        SbpClient client = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, "secretKey", webclient);

        QRDynamic qrDynamic = new QRDynamic("", BigDecimal.ONE);

        ContractViolationException thrown = assertThrows(ContractViolationException.class,
                () -> client.registerQR(qrDynamic));
        assertEquals(TestData.UNSUPPORTED_RESPONSE3_HTTPCODE, thrown.getHttpCode());
        assertEquals(TestData.UNSUPPORTED_RESPONSE3_MESSAGE, thrown.getMessage());
    }

}
