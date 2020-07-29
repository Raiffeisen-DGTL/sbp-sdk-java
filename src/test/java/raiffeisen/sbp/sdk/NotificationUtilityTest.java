package raiffeisen.sbp.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import raiffeisen.sbp.sdk.model.PaymentNotification;
import raiffeisen.sbp.sdk.utils.SbpUtils;

import java.math.BigDecimal;

public class NotificationUtilityTest {
    private final String API_SIGNATURE =
            "1eca6a084ff8a5b4f5740e0eeab2a72d8ff981bce5b5dea75a53bf44944a8c8d";

    private static final String BODY = "{\"transactionId\":17998,\"qrId\":\"AS1000408BSPMRDI8IHBGO4DFQAISU9O\",\"sbpMerchantId\":\"MA0000000552\",\"merchantId\":123,\"amount\":101.01,\"currency\":\"RUB\",\"transactionDate\":\"2020-07-24T17:20:00.999232+03:00\",\"paymentStatus\":\"SUCCESS\",\"additionalInfo\":null,\"order\":\"dfe0ff08-4796-46bb-a9fb-93fcd99ce748\",\"createDate\":\"2020-07-24T17:19:58+03:00\"}";

    private static final BigDecimal AMOUNT = BigDecimal.valueOf(101.01);

    private static final String SBP_MERCHANT_ID = "MA0000000552";

    private static final String ORDER = "dfe0ff08-4796-46bb-a9fb-93fcd99ce748";

    private static final String PAYMENT_STATUS = "SUCCESS";

    private static final String TRANSACTION_DATE = "2020-07-24T17:20:00.999232+03:00";

    private static final String TEST_SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTAwMDAwMDA1NTIiLCJqdGkiOiIwZTRhODI2ZC0zMTk3LTQ5YWUtYjRlYS0zZDllOGRkODIyOGEifQ.Q-AVaDBzvfkz6I8ZCVgvGIWpWTUgotRDmTcs4ysR0Qc";

    private static PaymentNotification notification;

    @BeforeEach
    public void PaymentNotificationTest() {
        try {
            notification = PaymentNotification.fromJson(BODY);
            assertEquals(AMOUNT, notification.getAmount());
            assertEquals(SBP_MERCHANT_ID, notification.getSbpMerchantId());
            assertEquals(ORDER, notification.getOrder());
            assertEquals(PAYMENT_STATUS, notification.getPaymentStatus());
            assertEquals(TRANSACTION_DATE, notification.getTransactionDate());
        }
        catch (JsonProcessingException e) {
            assert false;
        }
    }

    @Test
    public void checkSignatureFromJson() {
        assertTrue(SbpUtils.checkNotificationSignature(BODY, API_SIGNATURE, TEST_SECRET_KEY));
    }

    @Test
    public void checkSignatureFromPaymentNotification() {
        assertTrue(SbpUtils.checkNotificationSignature(notification, API_SIGNATURE, TEST_SECRET_KEY));
    }

    @Test
    public void checkSignatureFromFields() {
        assertTrue(SbpUtils.checkNotificationSignature(AMOUNT, SBP_MERCHANT_ID, ORDER, PAYMENT_STATUS, TRANSACTION_DATE,
                API_SIGNATURE, TEST_SECRET_KEY));
    }
}