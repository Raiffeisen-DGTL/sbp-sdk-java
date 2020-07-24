package raiffeisen.sbp.sdk;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import raiffeisen.sbp.sdk.model.PaymentNotification;

public class NotificationUtilityTest {
    private final String API_SIGNATURE =
            "1eca6a084ff8a5b4f5740e0eeab2a72d8ff981bce5b5dea75a53bf44944a8c8d";

    private final String BODY = "{\"transactionId\":17998,\"qrId\":\"AS1000408BSPMRDI8IHBGO4DFQAISU9O\",\"sbpMerchantId\":\"MA0000000552\",\"merchantId\":123,\"amount\":101.01,\"currency\":\"RUB\",\"transactionDate\":\"2020-07-24T17:20:00.999232+03:00\",\"paymentStatus\":\"SUCCESS\",\"additionalInfo\":null,\"order\":\"dfe0ff08-4796-46bb-a9fb-93fcd99ce748\",\"createDate\":\"2020-07-24T17:19:58+03:00\"}";

    private static final String SECRET_KEY = "";

    @Test
    public void PaymentNotificationTest() {

        String fields = PaymentNotification.joinFields(BODY);

        System.out.println(fields);

        String check = PaymentNotification.encrypt(fields, SECRET_KEY);

        System.out.println(check);

        // assertTrue(PaymentNotification.checkNotificationSignature(BODY, API_SIGNATURE, SECRET_KEY));
    }
}