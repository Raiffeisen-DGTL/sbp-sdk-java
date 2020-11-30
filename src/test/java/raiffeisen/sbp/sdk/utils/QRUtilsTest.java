package raiffeisen.sbp.sdk.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.model.out.QRStatic;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class QRUtilsTest {

    @Test
    void success_GivenDates() {
        QRDynamic qrDynamic = new QRDynamic("", BigDecimal.ZERO);
        qrDynamic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrDynamic.setQrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE);

        QRUtils.verifyQR(qrDynamic);

        assertEquals(TestData.DATE_CREATE_DATE, qrDynamic.getCreateDate());
        assertEquals(TestData.DATE_QR_EXPIRATION_DATE, qrDynamic.getQrExpirationDate());
    }

    @Test
    void success_ShiftMonth() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+1M");

        QRUtils.verifyQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_MONTH, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftDays() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+1d");

        QRUtils.verifyQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftHours() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+24H");

        QRUtils.verifyQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftMinutes() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+" + 24 * 60 + "m");

        QRUtils.verifyQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftSeconds() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+" + 24 * 60 * 60 + "s");

        QRUtils.verifyQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_ShiftAll() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+23H59m60s");

        QRUtils.verifyQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qrStatic.getQrExpirationDate());
    }

    @Test
    void success_OrderSavedIfPresent() {
        QRStatic qrStatic = new QRStatic("1-2-3");

        QRUtils.verifyQR(qrStatic);

        assertEquals("1-2-3", qrStatic.getOrder());
    }

    @Test
    void fail_EmptyShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QRUtils.verifyQR(qrStatic));
        assertEquals("Time shift is not specified", thrown.getMessage());
    }

    @Test
    void fail_InvalidSymbolsInShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+389r");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QRUtils.verifyQR(qrStatic));
        assertEquals("Invalid chars in QRInfo.qrExpirationDate", thrown.getMessage());
    }

    @Test
    void fail_InvalidInputInShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+12Mm13sH");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QRUtils.verifyQR(qrStatic));
        assertEquals("Bad input in QRInfo.qrExpirationDate", thrown.getMessage());
    }
}
