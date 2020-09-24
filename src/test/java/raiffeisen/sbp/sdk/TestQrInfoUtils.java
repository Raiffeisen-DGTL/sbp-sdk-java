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
        QRInfo result = QrInfoUtils.verify(qrInfo);

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
        QRInfo result = QrInfoUtils.verify(qrInfo);

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
        QRInfo result = QrInfoUtils.verify(qrInfo);

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
        QRInfo result = QrInfoUtils.verify(qrInfo);

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
        QRInfo result = QrInfoUtils.verify(qrInfo);

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
        QRInfo result = QrInfoUtils.verify(qrInfo);

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
        QRInfo result = QrInfoUtils.verify(qrInfo);

        // assert
        assertEquals(TestData.DATE_CREATE_DATE_PLUS_DAY, result.getQrExpirationDate());
    }

    @Test
    void success_GetUUID() {
        assertFalse(QrInfoUtils.generateOrderNum().isEmpty());
    }

    @Test
    void success_GenerateUUID() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                order(TestData.ORDER_NUMBER).
                create();

        // act
        QRInfo result = QrInfoUtils.verify(qrInfo);

        // assert
        assertEquals(TestData.ORDER_NUMBER, result.getOrder());
    }

    @Test
    void fail_EmptyShift() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                qrExpirationDate("+").
                create();

        // act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> QrInfoUtils.verify(qrInfo));

        // assert
        assertEquals("Time shift is not specified", thrown.getMessage());
    }

    @Test
    void fail_InvalidSymbols() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                qrExpirationDate("+389r").
                create();

        // act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> QrInfoUtils.verify(qrInfo));
        // assert
        assertEquals("Invalid chars in QRInfo.qrExpirationDate", thrown.getMessage());
    }

    @Test
    void fail_InvalidInput() {
        // arrange
        QRInfo qrInfo = QRInfo.creator().
                qrExpirationDate("+12Mm13sH").
                create();

        // act
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> QrInfoUtils.verify(qrInfo));
        // assert
        assertEquals("Bad input in QRInfo.qrExpirationDate", thrown.getMessage());
    }
}
