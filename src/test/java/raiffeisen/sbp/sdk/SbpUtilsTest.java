package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.utils.SbpUtils;
import raiffeisen.sbp.sdk.utils.TestData;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SbpUtilsTest {
    @Test
    void checkSignatureFromJson() {
        assertTrue(SbpUtils.checkNotificationSignature(TestData.NOTIFICATION, TestData.API_SIGNATURE, TestData.NOTIFICATION_TEST_SECRET_KEY));
    }

    @Test
    void checkSignatureFromPaymentNotification() {
        assertTrue(SbpUtils.checkNotificationSignature(TestData.NOTIFICATION, TestData.API_SIGNATURE, TestData.NOTIFICATION_TEST_SECRET_KEY));
    }

    @Test
    void checkSignatureFromFields() {
        assertTrue(SbpUtils.checkNotificationSignature(TestData.NOTIFICATION_AMOUNT, TestData.SBP_MERCHANT_ID,
                TestData.NOTIFICATION_ORDER, TestData.NOTIFICATION_PAYMENT_STATUS, TestData.NOTIFICATION_TRANSACTION_DATE,
                TestData.API_SIGNATURE, TestData.NOTIFICATION_TEST_SECRET_KEY));
    }
}
