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
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.AutoCharge;
import raiffeisen.sbp.sdk.model.out.Extra;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.model.out.QRStatic;
import raiffeisen.sbp.sdk.model.out.QRVariable;
import raiffeisen.sbp.sdk.model.out.Subscription;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
class CreateQrTest {

    @Test
    void createQRDynamicTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        QRDynamic qrDynamic = new QRDynamic(TestUtils.getRandomUUID(), new BigDecimal(314));

        QRUrl response = TestUtils.CLIENT.registerQR(qrDynamic);

        assertNotNull(response.getQrId());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrUrl());
    }

    @Test
    void createQRStaticTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        QRStatic qrStatic = new QRStatic(TestUtils.getRandomUUID());

        QRUrl response = TestUtils.CLIENT.registerQR(qrStatic);

        assertNotNull(response.getQrUrl());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrId());
    }

    @Test
    void createQRVariableTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        QRVariable qrVariable = new QRVariable();
        QRUrl response = TestUtils.CLIENT.registerQR(qrVariable);

        assertNotNull(response.getQrUrl());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrId());
    }

    @Test
    void createQRMaxTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        // Test without "account" parameter
        QRStatic qrStatic = new QRStatic(TestUtils.getRandomUUID());
        qrStatic.setAdditionalInfo("Доп информация");
        qrStatic.setAmount(new BigDecimal(1110));
        qrStatic.setPaymentDetails("Назначение платежа");
        qrStatic.setQrExpirationDate("2025-07-22T09:14:38.107227+03:00");
        qrStatic.setQrDescription("Описание QR кода");

        QRUrl response = TestUtils.CLIENT.registerQR(qrStatic);
        assertNotNull(response.getQrId());
        assertNotNull(response.getQrUrl());
        assertNotNull(response.getPayload());
    }

    @Test
    void createQRWithSubscription() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        QRDynamic qrDynamic = new QRDynamic(TestUtils.getRandomUUID(), new BigDecimal(100));
        qrDynamic.setAdditionalInfo("Доп информация");
        qrDynamic.setPaymentDetails("Назначение платежа");
        qrDynamic.setQrExpirationDate(ZonedDateTime.now().plusDays(90));
        qrDynamic.setQrDescription("Описание QR кода");
        Extra extra = new Extra();
        extra.put("extraParam1", "Пример экстра параметра 1");
        extra.put("extraParam2", "Пример экстра параметра 2");
        qrDynamic.setExtra(extra);
        qrDynamic.setRedirectUrl("https://cool-company.zone/paid");
        Subscription subscription = new Subscription("Подписка на услуги");
        subscription.setId(TestUtils.getRandomUUID());
        AutoCharge autoCharge = new AutoCharge();
        autoCharge.setFrequency("MONTHLY");
        autoCharge.setAmount(new BigDecimal(100));
        autoCharge.setFirstChargeDate(LocalDate.now().plusDays(7));
        subscription.setAutoCharge(autoCharge);
        subscription.setExtra((Extra) new Extra().put("key1", "value1"));
        qrDynamic.setSubscription(subscription);

        QRUrl response = TestUtils.CLIENT.registerQR(qrDynamic);
        assertNotNull(response.getQrId());
        assertNotNull(response.getQrUrl());
        assertTrue(isValidDateFormat(autoCharge.getFirstChargeDate().toString()), "Дата не соответствует формату YYYY-MM-DD");
    }

    @Test
    void createQRWithoutAmountNegativeTest() {
        QRStatic badQR = new QRStatic("");

        assertThrows(SbpException.class, () -> TestUtils.CLIENT.registerQR(badQR));
    }

    private boolean isValidDateFormat(String value) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(value, dateTimeFormatter);
            return true;
        } catch ( DateTimeParseException e) {
            return false;
        }
    }
}
