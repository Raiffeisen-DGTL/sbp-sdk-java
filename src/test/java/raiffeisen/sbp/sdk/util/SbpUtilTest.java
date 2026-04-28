/*
 * Copyright 2020-present AO Raiffeisenbank
 *
 * This file is part of SBP SDK Java.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
import static raiffeisen.sbp.sdk.data.TestData.TEST_NOTIFICATION_SBP_MERCHANT_ID;

@Tag("unit")
class SbpUtilTest {
    private static final String TEST_API_SIGNATURE = "1eca6a084ff8a5b4f5740e0eeab2a72d8ff981bce5b5dea75a53bf44944a8c8d";
    private static final String BODY = "{\"transactionId\":17998,\"qrId\":\"AS1000408BSPMRDI8IHBGO4DFQAISU9O\",\"sbpMerchantId\":\"MA0000000552\",\"merchantId\":123,\"amount\":101.01,\"currency\":\"RUB\",\"transactionDate\":\"2020-07-24T17:20:00.999232+03:00\",\"paymentStatus\":\"SUCCESS\",\"additionalInfo\":null,\"order\":\"dfe0ff08-4796-46bb-a9fb-93fcd99ce748\",\"createDate\":\"2020-07-24T17:19:58+03:00\"}";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(101.01);
    private static final String ORDER = "dfe0ff08-4796-46bb-a9fb-93fcd99ce748";
    private static final String TRANSACTION_DATE = "2020-07-24T17:20:00.999232+03:00";
    private static final String TEST_SECRET_KEY = TestData.NOTIFICATION_TEST_SECRET_KEY;

    private static PaymentNotification notification;

    @BeforeEach
    public void PaymentNotificationTest() throws JsonProcessingException {
        notification = SbpUtil.parseNotification(TestData.NOTIFICATION);
        assertEquals(AMOUNT, notification.getAmount());
        assertEquals(TEST_NOTIFICATION_SBP_MERCHANT_ID, notification.getSbpMerchantId());
        assertEquals(ORDER, notification.getOrder());
        assertEquals(StatusCodes.SUCCESS.getMessage(), notification.getPaymentStatus());
        assertEquals(TRANSACTION_DATE, notification.getTransactionDate());
    }

    @Test
    void checkSignatureFromJson() {
        assertTrue(SbpUtil.checkNotificationSignature(BODY, TEST_API_SIGNATURE, TEST_SECRET_KEY));
    }

    @Test
    void checkSignatureFromPaymentNotification() {
        assertTrue(SbpUtil.checkNotificationSignature(notification, TEST_API_SIGNATURE, TEST_SECRET_KEY));
    }

    @Test
    void checkSignatureFromFields() {
        assertTrue(SbpUtil.checkNotificationSignature(AMOUNT, TEST_NOTIFICATION_SBP_MERCHANT_ID, ORDER, StatusCodes.SUCCESS.getMessage(), TRANSACTION_DATE,
                TEST_API_SIGNATURE, TEST_SECRET_KEY));
    }

    @Test
    void wrongSignature() {
        assertFalse(SbpUtil.checkNotificationSignature(notification, TEST_API_SIGNATURE.substring(5), TEST_SECRET_KEY));
    }

    @Test
    void badJsonBody() {
        assertFalse(SbpUtil.checkNotificationSignature("bad json", TEST_API_SIGNATURE, TEST_SECRET_KEY));
    }
}
