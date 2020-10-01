package raiffeisen.sbp.sdk.utils;

import org.junit.jupiter.api.Test;

import raiffeisen.sbp.sdk.model.out.QRInfo;

import static org.junit.jupiter.api.Assertions.*;

class QrInfoUtilsTest {

    @Test
    void success_GivenDates() {
        QRInfo qrInfo = QRInfo.creator()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE)
                .create();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE, result.getCreateDate());
        assertEquals(TestData.DATE_QR_EXPIRATION_DATE, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftMonth() {
        QRInfo qrInfo = QRInfo.creator()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+1M")
                .create();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_MONTH, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftDays() {
        QRInfo qrInfo = QRInfo.creator()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+1d")
                .create();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftHours() {
        QRInfo qrInfo = QRInfo.creator()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+24H")
                .create();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftMinutes() {
        QRInfo qrInfo = QRInfo.creator()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+" + 24 * 60 + "m")
                .create();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftSeconds() {
        QRInfo qrInfo = QRInfo.creator()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+" + 24 * 60 * 60 + "s")
                .create();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftAll() {
        QRInfo qrInfo = QRInfo.creator()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+23H59m60s")
                .create();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_GetUUID() {
        assertFalse(QrInfoUtils.createUUID().isEmpty());
    }

    @Test
    void success_GenerateUUID() {
        QRInfo qrInfo = QRInfo.creator().
                order("1-2-3").
                create();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals("1-2-3", result.getOrder());
    }

    @Test
    void fail_EmptyShift() {
        QRInfo qrInfo = QRInfo.creator()
                .qrExpirationDate("+")
                .create();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QrInfoUtils.verify(qrInfo));
        assertEquals("Time shift is not specified", thrown.getMessage());
    }

    @Test
    void fail_InvalidSymbols() {
        QRInfo qrInfo = QRInfo.creator()
                .qrExpirationDate("+389r")
                .create();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QrInfoUtils.verify(qrInfo));
        assertEquals("Invalid chars in QRInfo.qrExpirationDate", thrown.getMessage());
    }

    @Test
    void fail_InvalidInput() {
        QRInfo qrInfo = QRInfo.creator()
                .qrExpirationDate("+12Mm13sH")
                .create();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QrInfoUtils.verify(qrInfo));
        assertEquals("Bad input in QRInfo.qrExpirationDate", thrown.getMessage());
    }
}
