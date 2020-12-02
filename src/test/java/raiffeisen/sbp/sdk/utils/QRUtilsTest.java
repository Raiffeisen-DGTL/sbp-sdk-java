package raiffeisen.sbp.sdk.utils;

import lombok.ToString;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.model.out.QR;
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

        QR qr = QRUtils.prepareQR(qrDynamic);

        assertEquals(TestData.DATE_CREATE_DATE, qr.getCreateDate());
        assertEquals(TestData.DATE_QR_EXPIRATION_DATE, qr.getQrExpirationDate());
    }

    @Test
    void success_ShiftMonth() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+1M");

        QR qr = QRUtils.prepareQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_MONTH, qr.getQrExpirationDate());
    }

    @Test
    void success_ShiftDays() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+1d");

        QR qr = QRUtils.prepareQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qr.getQrExpirationDate());
    }

    @Test
    void success_ShiftHours() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+24H");

        QR qr = QRUtils.prepareQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qr.getQrExpirationDate());
    }

    @Test
    void success_ShiftMinutes() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+" + 24 * 60 + "m");

        QR qr = QRUtils.prepareQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qr.getQrExpirationDate());
    }

    @Test
    void success_ShiftSeconds() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+" + 24 * 60 * 60 + "s");

        QR qr = QRUtils.prepareQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qr.getQrExpirationDate());
    }

    @Test
    void success_ShiftAll() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setCreateDate(TestData.DATE_CREATE_DATE);
        qrStatic.setQrExpirationDate("+23H59m60s");

        QR qr = QRUtils.prepareQR(qrStatic);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, qr.getQrExpirationDate());
    }

    @Test
    void success_OrderSavedIfPresent() {
        QRStatic qrStatic = new QRStatic("1-2-3");

        QR qr = QRUtils.prepareQR(qrStatic);

        assertEquals("1-2-3", qr.getOrder());
    }

    @Test
    void fail_EmptyShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QRUtils.prepareQR(qrStatic));
        assertEquals("Time shift is not specified", thrown.getMessage());
    }

    @Test
    void fail_InvalidSymbolsInShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+389r");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QRUtils.prepareQR(qrStatic));
        assertEquals("Invalid chars in QRInfo.qrExpirationDate", thrown.getMessage());
    }

    @Test
    void fail_InvalidInputInShiftDate() {
        QRStatic qrStatic = new QRStatic("");
        qrStatic.setQrExpirationDate("+12Mm13sH");

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QRUtils.prepareQR(qrStatic));
        assertEquals("Bad input in QRInfo.qrExpirationDate", thrown.getMessage());
    }
}
