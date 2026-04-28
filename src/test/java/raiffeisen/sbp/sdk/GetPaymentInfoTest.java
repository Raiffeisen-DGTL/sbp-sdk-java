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
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.out.QRId;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class GetPaymentInfoTest {

    private static String qrId;

    @BeforeAll
    @Timeout(15)
    public static void initTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        qrId = TestUtils.initDynamicQR().getQrId();
    }

    @Test
    void unauthorizedTest() {
        SbpClient clientUnauthorized = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, TestUtils.getRandomUUID());
        QRId id = new QRId(qrId);

        assertThrows(ContractViolationException.class, () -> clientUnauthorized.getPaymentInfo(id));
    }

    @Test
    void getPaymentInfo() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        QRId id = new QRId(qrId);

        PaymentInfo response = TestUtils.CLIENT.getPaymentInfo(id);

        assertNotNull(response.getAmount());
        assertNotNull(response.getCreateDate());
        assertNotNull(response.getCurrency());
        assertNotNull(response.getOrder());
        assertNotNull(response.getTransactionDate());
        assertNotNull(response.getPaymentStatus());
        assertNotNull(response.getQrId());
    }

    @Test
    void getPaymentInfoExceptionTest() {
        String badQrId = TestUtils.getRandomUUID();

        QRId badId = new QRId(badQrId);

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getPaymentInfo(badId));
        assertEquals(TestData.QR_CODE_NOT_FOUND_ERROR_CODE, ex.getCode());
        assertEquals(TestData.QR_CODE_NOT_FOUND_ERROR_MESSAGE, ex.getMessage());
    }

}
