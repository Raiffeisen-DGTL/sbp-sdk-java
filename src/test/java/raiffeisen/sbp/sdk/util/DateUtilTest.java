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
        Order order = new Order(BigDecimal.ZERO);
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate(TestData.DATE_QR_EXPIRATION_DATE);

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE, order.getCreateDate());
        assertEquals(TestData.DATE_QR_EXPIRATION_DATE, order.getExpirationDate());
    }

    @Test
    void success_ShiftMonth() {
        Order order = new Order(BigDecimal.ZERO);
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+1M");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_MONTH, order.getExpirationDate());
    }

    @Test
    void success_ShiftDays() {
        Order order = new Order(BigDecimal.ZERO);
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+1d");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void success_ShiftHours() {
        Order order = new Order(BigDecimal.ZERO);
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+24H");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void success_ShiftMinutes() {
        Order order = new Order(BigDecimal.ZERO);
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+" + 24 * 60 + "m");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void success_ShiftSeconds() {
        Order order = new Order(BigDecimal.ZERO);
        order.setCreateDate(TestData.DATE_CREATE_DATE);
        order.setExpirationDate("+" + 24 * 60 * 60 + "s");

        order.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, order.getExpirationDate());
    }

    @Test
    void success_ShiftAll() {
        Order order = new Order(BigDecimal.ZERO);
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
