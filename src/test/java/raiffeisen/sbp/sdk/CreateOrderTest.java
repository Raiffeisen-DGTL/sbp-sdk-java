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
