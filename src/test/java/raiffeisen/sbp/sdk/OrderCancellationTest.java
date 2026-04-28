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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.out.Order;
import raiffeisen.sbp.sdk.model.out.OrderId;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class OrderCancellationTest {

    @Test
    void OrderSuccessCancellation() throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        Order order = Order.builder().amount(new BigDecimal(314)).build();
        OrderInfo response = TestUtils.CLIENT.createOrder(order);
        OrderId orderId = new OrderId(response.getId());

        TestUtils.CLIENT.orderCancellation(new OrderId(response.getId()));

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.orderCancellation(orderId));
        assertEquals(String.format(TestData.ORDER_HAS_CANCELLED_STATUS_MESSAGE, orderId.getOrderId()), ex.getMessage());
    }

    @Test
    void OrderCancellationWithNotFoundStatus() {
        OrderId badOrderId = new OrderId(TestUtils.getRandomUUID());

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.orderCancellation(badOrderId));
        assertEquals(TestData.ORDER_NOT_FOUND_ERROR_CODE, ex.getCode());
        assertEquals(String.format(TestData.ORDER_NOT_FOUND_ERROR_MESSAGE, badOrderId.getOrderId()), ex.getMessage());
    }

    @Test
    void OrderCancellationWithPaidStatus() throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        OrderInfo response = TestUtils.initOrderWithQrVariable();
        OrderId orderId = new OrderId(response.getId());

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.orderCancellation(orderId));
        assertEquals(TestData.ORDER_HAS_FINAL_STATUS_CODE, ex.getCode());
        assertEquals(String.format(TestData.ORDER_HAS_FINAL_STATUS_MESSAGE, orderId.getOrderId()), ex.getMessage());
    }
}
