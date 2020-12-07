package raiffeisen.sbp.sdk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.StatusCodes;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.model.PaymentNotification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static raiffeisen.sbp.sdk.data.TestData.TEST_SBP_MERCHANT_ID;

@Tag("unit")
class SbpUtilsTest {
    private static final String TEST_API_SIGNATURE = "1eca6a084ff8a5b4f5740e0eeab2a72d8ff981bce5b5dea75a53bf44944a8c8d";
    private static final String BODY = "{\"transactionId\":17998,\"qrId\":\"AS1000408BSPMRDI8IHBGO4DFQAISU9O\",\"sbpMerchantId\":\"MA0000000552\",\"merchantId\":123,\"amount\":101.01,\"currency\":\"RUB\",\"transactionDate\":\"2020-07-24T17:20:00.999232+03:00\",\"paymentStatus\":\"SUCCESS\",\"additionalInfo\":null,\"order\":\"dfe0ff08-4796-46bb-a9fb-93fcd99ce748\",\"createDate\":\"2020-07-24T17:19:58+03:00\"}";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(101.01);
    private static final String ORDER = "dfe0ff08-4796-46bb-a9fb-93fcd99ce748";
    private static final String TRANSACTION_DATE = "2020-07-24T17:20:00.999232+03:00";
    private static final String TEST_SECRET_KEY = TestData.NOTIFICATION_TEST_SECRET_KEY;

    private static PaymentNotification notification;

    @BeforeEach
    public void PaymentNotificationTest() throws JsonProcessingException {
        notification = SbpUtils.parseNotification(TestData.NOTIFICATION);
        assertEquals(AMOUNT, notification.getAmount());
        assertEquals(TEST_SBP_MERCHANT_ID, notification.getSbpMerchantId());
        assertEquals(ORDER, notification.getOrder());
        assertEquals(StatusCodes.SUCCESS.getMessage(), notification.getPaymentStatus());
        assertEquals(TRANSACTION_DATE, notification.getTransactionDate());
    }

    @Test
    void checkSignatureFromJson() {
        assertTrue(SbpUtils.checkNotificationSignature(BODY, TEST_API_SIGNATURE, TEST_SECRET_KEY));
    }

    @Test
    void checkSignatureFromPaymentNotification() {
        assertTrue(SbpUtils.checkNotificationSignature(notification, TEST_API_SIGNATURE, TEST_SECRET_KEY));
    }

    @Test
    void checkSignatureFromFields() {
        assertTrue(SbpUtils.checkNotificationSignature(AMOUNT, TEST_SBP_MERCHANT_ID, ORDER, StatusCodes.SUCCESS.getMessage(), TRANSACTION_DATE,
                TEST_API_SIGNATURE, TEST_SECRET_KEY));
    }

    @Test
    void wrongSignature() {
        assertFalse(SbpUtils.checkNotificationSignature(notification, TEST_API_SIGNATURE.substring(5), TEST_SECRET_KEY));
    }

    @Test
    void badJsonBody() {
        assertFalse(SbpUtils.checkNotificationSignature("bad json", TEST_API_SIGNATURE, TEST_SECRET_KEY));
    }
}
