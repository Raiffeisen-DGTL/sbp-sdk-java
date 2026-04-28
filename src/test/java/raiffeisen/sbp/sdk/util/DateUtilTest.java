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

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.model.out.Order;
import raiffeisen.sbp.sdk.model.out.QRStatic;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class DateUtilTest {
    @Test
    void success_GivenDates() {
        Order order = Order.builder().amount(BigDecimal.ZERO).build();
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate(TestData.DATE_QR_EXPIRATION_DATE);

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE, order.getCreateDate());
        assertEquals(TestData.DATE_QR_EXPIRATION_DATE, order.getExpirationDate());
    }

    @Test
    void success_ShiftMonth() {
        Order order = Order.builder().amount(BigDecimal.ZERO).build();
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+1M");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_MONTH, order.getExpirationDate());
    }

    @Test
    void success_ShiftDays() {
        Order order = Order.builder().amount(BigDecimal.ZERO).build();
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+1d");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void success_ShiftHours() {
        Order order = Order.builder().amount(BigDecimal.ZERO).build();
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+24H");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void success_ShiftMinutes() {
        Order order = Order.builder().amount(BigDecimal.ZERO).build();
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+" + 24 * 60 + "m");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void success_ShiftSeconds() {
        Order order = Order.builder().amount(BigDecimal.ZERO).build();
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+" + 24 * 60 * 60 + "s");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void success_ShiftAll() {
        Order order = Order.builder().amount(BigDecimal.ZERO).build();
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+23H59m60s");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void fail_EmptyShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> qrStatic.verify());
        assertEquals("Time shift is not specified", thrown.getMessage());
    }

    @Test
    void fail_InvalidSymbolsInShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+389r");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> qrStatic.verify());
        assertEquals("Invalid chars in QR.qrExpirationDate", thrown.getMessage());
    }

    @Test
    void fail_InvalidInputInShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+12Mm13sH");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> qrStatic.verify());
        assertEquals("Bad input in QR.qrExpirationDate", thrown.getMessage());
    }
}
