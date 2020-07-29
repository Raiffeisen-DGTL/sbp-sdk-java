package raiffeisen.sbp.sdk;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import org.junit.jupiter.api.Test;

import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.model.out.RefundInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RefundInfoTest {

    private static final String SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJNQTAwMDAwMDA1NTIiLCJqdGkiOiI0ZDFmZWIwNy0xZDExLTRjOWEtYmViNi" +
            "1kZjUwY2Y2Mzc5YTUifQ.pxU8KYfqbVlxvQV7wfbGpsu4AX1QoY26FqBiuNuyT-s";

    private static SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, SECRET_KEY);

    private static String PAYMENT_URL = "https://test.ecom.raiffeisen.ru/sbp/v1/transaction/*/status?status=SUCCESS";

    private static String STATIC_REFUND_ID;

    private static String STATIC_ORDER_INFO;

    private static long STATIC_TRANSACTION_ID;

    private final String TEST_SBP_MERCHANT_ID = "MA0000000552";

    private String getOrderInfo() {
        return UUID.randomUUID().toString();
    }

    private String getRefundId() {
        return UUID.randomUUID().toString();
    }

    private String getCreateDate() {
        String timestamp = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString();
        return timestamp.substring(0,timestamp.indexOf("["));
    }

    private boolean payQR(QRId id) throws IOException {
        HttpGet getter = new HttpGet(PAYMENT_URL.replace("*", id.getQrId()));

        try(CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(getter)) {
            int code = response.getStatusLine().getStatusCode();
            return code == 200;
        }
    }

    private static String BAD_REFUND_ID = "BadRefundId";

    BigDecimal moneyAmount;

    private void initStaticQR() {
        STATIC_ORDER_INFO = getOrderInfo();
        QRInfo qrStatic = QRInfo.creator().
                createDate(getCreateDate()).
                order(STATIC_ORDER_INFO).
                qrType(QRType.QRStatic).
                sbpMerchantId(TEST_SBP_MERCHANT_ID).
                create();

        try {
            QRUrl qr = client.registerQR(qrStatic);
            QRId id = QRId.creator().qrId(qr.getQrId()).create();

            assert payQR(id);

            PaymentInfo info = client.getPaymentInfo(id);
            STATIC_TRANSACTION_ID = info.getTransactionId();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    public void refundPayment() {
        STATIC_REFUND_ID = getRefundId();
        moneyAmount = new BigDecimal(100);
        RefundInfo refundInfo = RefundInfo.creator().
                amount(moneyAmount).
                order(STATIC_ORDER_INFO).
                refundId(STATIC_REFUND_ID).
                transactionId(STATIC_TRANSACTION_ID).
                create();

        try {
            client.refundPayment(refundInfo);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    public void refundInfoTest() {
        initStaticQR();
        refundPayment();

        try {
            RefundStatus response = client.getRefundInfo(STATIC_REFUND_ID);
            assertEquals("SUCCESS", response.getCode());
            assertEquals(moneyAmount, response.getAmount());
            assertTrue(response.getRefundStatus().equals("IN_PROGRESS") || response.getRefundStatus().equals("COMPLETED"));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }

    }

    @Test
    public void refundInfoExceptionTest() {
        try {
            client.getRefundInfo(BAD_REFUND_ID);
            assert false;
        }
        catch (Exception e) {
            assert (e instanceof SbpException);
        }
    }
}
