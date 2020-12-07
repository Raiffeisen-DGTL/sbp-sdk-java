package raiffeisen.sbp.sdk.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.model.out.QRStatic;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
class QRUtilsTest {

    @Test
    void success_GivenDates() {
        QRDynamic qrDynamic = new QRDynamic("", BigDecimal.ZERO);
        qrDynamic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrDynamic.setQrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE);

        qrDynamic.verify();

        assertEquals(TestData.DATE_CREATE_DATE, qrDynamic.getCreateDate());
        assertEquals(TestData.DATE_QR_EXPIRATION_DATE, qrDynamic.getQrExpirationDate());
    }

    @Test
    void success_ShiftMonth() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+1M");

        qrStatic.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_MONTH, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftDays() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+1d");

        qrStatic.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftHours() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+24H");

        qrStatic.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftMinutes() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+" + 24 * 60 + "m");

        qrStatic.verify();
        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftSeconds() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+" + 24 * 60 * 60 + "s");

        qrStatic.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftAll() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+23H59m60s");

        qrStatic.verify();

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }


    @Test
    void success_OrderSavedIfPresent() {
        final String expected = "1-2-3";

        QRStatic qrStatic = new QRStatic(expected);
        qrStatic.verify();
        assertEquals(expected, qrStatic.getOrder());
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
        assertEquals("Invalid chars in QRInfo.qrExpirationDate", thrown.getMessage());
    }

    @Test
    void fail_InvalidInputInShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+12Mm13sH");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> qrStatic.verify());
        assertEquals("Bad input in QRInfo.qrExpirationDate", thrown.getMessage());
    }
}
