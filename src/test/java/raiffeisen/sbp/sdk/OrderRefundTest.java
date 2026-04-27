/*
 * Copyright 2020-present AO Raiffeisenbank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
