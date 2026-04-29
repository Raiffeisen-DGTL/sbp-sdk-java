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

package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import raiffeisen.sbp.sdk.data.StatusCodes;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.OrderRefund;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class OrderRefundTest {
    private static final BigDecimal AMOUNT = new BigDecimal(100);
    private static OrderInfo orderInfo;

    @BeforeAll
    @Timeout(15)
    static void setup() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        orderInfo = TestUtils.initOrderWithQrVariable();
    }

    @Test
    void OrderRefundSuccessTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        String refundId = TestUtils.getRandomUUID();
        OrderRefund orderRefund = new OrderRefund(orderInfo.getId(), refundId, AMOUNT);

        RefundStatus response = TestUtils.CLIENT.orderRefund(orderRefund);

        assertEquals(AMOUNT, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundInfoExceptionTest() {
        String refundId = TestUtils.getRandomUUID();
        String orderId = TestUtils.getRandomUUID();
        OrderRefund orderRefund = new OrderRefund(orderId, refundId, AMOUNT);
        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.orderRefund(orderRefund));
        assertEquals(TestData.ORDER_REFUND_WHEN_DOES_NOT_EXIST, ex.getMessage());
    }
}
