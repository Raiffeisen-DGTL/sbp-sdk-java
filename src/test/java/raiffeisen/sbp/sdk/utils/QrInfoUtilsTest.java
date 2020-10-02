package raiffeisen.sbp.sdk.utils;

import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import static org.junit.jupiter.api.Assertions.*;

class QrInfoUtilsTest {

    @Test
    void success_GivenDates() {
        QRInfo qrInfo = QRInfo.builder()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE)
                .build();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE, result.getCreateDate());
        assertEquals(TestData.DATE_QR_EXPIRATION_DATE, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftMonth() {
        QRInfo qrInfo = QRInfo.builder()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+1M")
                .build();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_MONTH, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftDays() {
        QRInfo qrInfo = QRInfo.builder()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+1d")
                .build();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftHours() {
        QRInfo qrInfo = QRInfo.builder()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+24H")
                .build();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftMinutes() {
        QRInfo qrInfo = QRInfo.builder()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+" + 24 * 60 + "m")
                .build();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftSeconds() {
        QRInfo qrInfo = QRInfo.builder()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+" + 24 * 60 * 60 + "s")
                .build();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftAll() {
        QRInfo qrInfo = QRInfo.builder()
                .createDate(TestData.DATE_CREATE_DATE)
                .qrExpirationDate("+23H59m60s")
                .build();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_GetUUID() {
        assertFalse(QrInfoUtils.generateOrderNum().isEmpty());
    }

    @Test
    void success_GenerateUUID() {
        QRInfo qrInfo = QRInfo.builder().
                order("1-2-3").
                build();

        QRInfo result = QrInfoUtils.verify(qrInfo);

        assertEquals("1-2-3", result.getOrder());
    }

    @Test
    void fail_EmptyShift() {
        QRInfo qrInfo = QRInfo.builder()
                .qrExpirationDate("+")
                .build();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QrInfoUtils.verify(qrInfo));
        assertEquals("Time shift is not specified", thrown.getMessage());
    }

    @Test
    void fail_InvalidSymbols() {
        QRInfo qrInfo = QRInfo.builder()
                .qrExpirationDate("+389r")
                .build();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QrInfoUtils.verify(qrInfo));
        assertEquals("Invalid chars in QRInfo.qrExpirationDate", thrown.getMessage());
    }

    @Test
    void fail_InvalidInput() {
        QRInfo qrInfo = QRInfo.builder()
                .qrExpirationDate("+12Mm13sH")
                .build();

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> QrInfoUtils.verify(qrInfo));
        assertEquals("Bad input in QRInfo.qrExpirationDate", thrown.getMessage());
    }
}
