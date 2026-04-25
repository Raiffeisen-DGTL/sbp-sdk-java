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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.Order;
import raiffeisen.sbp.sdk.model.out.OrderQr;
import raiffeisen.sbp.sdk.model.out.QRVariable;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("integration")
public class CreateOrderTest {
    @Test
    void createOrderTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        Order order = Order.builder().amount(new BigDecimal(314)).build();
        OrderInfo response = TestUtils.CLIENT.createOrder(order);

        assertNotNull(response.getId());
        assertNotNull(response.getAmount());
        assertNotNull(response.getExtra());
    }

    @Test
    void createOrderWithQr() throws SbpException, IOException, ContractViolationException, URISyntaxException, InterruptedException {
        QRVariable qrVariable = new QRVariable();
        QRUrl qr = TestUtils.CLIENT.registerQR(qrVariable);

        OrderQr orderQr = new OrderQr();
        orderQr.setId(qr.getQrId());

        Order order = Order.builder().amount(new BigDecimal(314)).qr(orderQr).build();
        OrderInfo response = TestUtils.CLIENT.createOrder(order);

        assertNotNull(response.getId());
        assertNotNull(response.getAmount());
        assertNotNull(response.getExtra());
        assertNotNull(response.getQr());
    }
}
