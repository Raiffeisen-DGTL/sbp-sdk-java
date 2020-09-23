package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.utils.QrInfoUtils;

import static org.junit.jupiter.api.Assertions.*;

class TestQrInfoUtils {

    @Test
    void success_GivenDates() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate(TestData.DATE_QR_EXPIRATION_DATE).
                create();

        // act
        QRInfo result = QrInfoUtils.calculateDate(qrInfo);

        // assert
        assertEquals(TestData.DATE_CREATE_DATE, result.getCreateDate());
        assertEquals(TestData.DATE_QR_EXPIRATION_DATE, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftMonth() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate("+1M").
                create();

        // act
        QRInfo result = QrInfoUtils.calculateDate(qrInfo);

        // assert
        assertEquals(TestData.DATE_CREATE_DATE_PLUS_MONTH, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftDays() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate("+1d").
                create();

        // act
        QRInfo result = QrInfoUtils.calculateDate(qrInfo);

        // assert
        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftHours() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate("+24H").
                create();

        // act
        QRInfo result = QrInfoUtils.calculateDate(qrInfo);

        // assert
        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftMinutes() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate("+" + 24*60 + "m").
                create();

        // act
        QRInfo result = QrInfoUtils.calculateDate(qrInfo);

        // assert
        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftSeconds() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate("+" + 24*60*60 + "s").
                create();

        // act
        QRInfo result = QrInfoUtils.calculateDate(qrInfo);

        // assert
        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_ShiftAll() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                createDate(TestData.DATE_CREATE_DATE).
                qrExpirationDate("+23H59m60s").
                create();

        // act
        QRInfo result = QrInfoUtils.calculateDate(qrInfo);

        // assert
        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_GetUUID() {
        assertFalse(QrInfoUtils.createUUID().isEmpty());
    }

    @Test
    void fail_EmptyShift() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                qrExpirationDate("+").
                create();

        // act
        try {
            QRInfo result = QrInfoUtils.calculateDate(qrInfo);
        }
        catch (IllegalArgumentException e) {
            // assert
            assertEquals("Time shift is not specified", e.getMessage());
        }
    }

    @Test
    void fail_InvalidSymbols() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                qrExpirationDate("+389r").
                create();

        // act
        try {
            QRInfo result = QrInfoUtils.calculateDate(qrInfo);
        }
        catch (IllegalArgumentException e) {
            // assert
            assertEquals("Invalid chars in QRInfo.qrExpirationDate", e.getMessage());
        }
    }

    @Test
    void fail_InvalidInput() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                qrExpirationDate("+12Mm13sH").
                create();

        // act
        try {
            QRInfo result = QrInfoUtils.calculateDate(qrInfo);
        }
        catch (IllegalArgumentException e) {
            // assert
            assertEquals("Bad input in QRInfo.qrExpirationDate", e.getMessage());
        }
    }
}
