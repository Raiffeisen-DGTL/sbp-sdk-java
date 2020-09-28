package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import raiffeisen.sbp.sdk.utils.SbpUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class TestSbpUtils {
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
        assertTrue(SbpUtils.checkNotificationSignature(TestData.NOTIFICATION_AMOUNT, TestData.NOTIFICATION_SBP_MERCHANT_ID,
                TestData.NOTIFICATION_ORDER, TestData.NOTIFICATION_PAYMENT_STATUS, TestData.NOTIFICATION_TRANSACTION_DATE,
                TestData.API_SIGNATURE, TestData.NOTIFICATION_TEST_SECRET_KEY));
    }
}
