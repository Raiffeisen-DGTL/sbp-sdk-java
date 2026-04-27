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
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.out.OrderId;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class GetOrderInfoTest {

    private static OrderId orderId;

    @BeforeAll
    @Timeout(15)
    static void initTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        orderId = new OrderId(TestUtils.initOrderWithQrVariable().getId());
    }

    @Test
    void getOrderInfoTest() throws Exception {
        OrderInfo response = TestUtils.CLIENT.getOrderInfo(orderId);

        assertNotNull(response.getId());
        assertNotNull(response.getAmount());
        assertNotNull(response.getComment());
        assertNotNull(response.getExtra());
        assertNotNull(response.getStatus());
        assertNotNull(response.getExpirationDate());
        assertNotNull(response.getQr());
    }

    @Test
    void getOrderInfoByBadQrIdNegativeTest() {
        OrderId badOrderId = new OrderId(TestUtils.getRandomUUID());

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getOrderInfo(badOrderId));
        assertEquals(TestData.ORDER_NOT_FOUND_ERROR_CODE, ex.getCode());
        assertEquals(String.format(TestData.ORDER_NOT_FOUND_ERROR_MESSAGE, badOrderId.getOrderId()), ex.getMessage());
    }
}
