package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.data.StatusCodes;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreateQrTest {

    private final String TEST_SBP_MERCHANT_ID = TestData.SBP_MERCHANT_ID;

    @Test
    void createQRInfoDynamicTest() throws IOException, SbpException {
        QRInfo QR = QRInfo.builder()
                .order(TestUtils.getRandomUUID())
                .qrType(QRType.QRDynamic)
                .amount(new BigDecimal(314))
                .currency("RUB")
                .sbpMerchantId(TEST_SBP_MERCHANT_ID)
                .build();

        QRUrl response = TestUtils.CLIENT.registerQR(QR);
        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
    }

    @Test
    void createQRInfoStaticTest() throws IOException, SbpException {
        QRInfo QR = QRInfo.builder()
                .order(TestUtils.getRandomUUID())
                .qrType(QRType.QRStatic)
                .sbpMerchantId(TEST_SBP_MERCHANT_ID)
                .build();

        QRUrl response = TestUtils.CLIENT.registerQR(QR);
        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
    }

    @Test
    void createQRInfoMaxTest() throws IOException, SbpException {
        // Test without "account" parameter
        QRInfo QR = QRInfo.builder()
                .additionalInfo("Доп информация")
                .amount(new BigDecimal(1110))
                .currency("RUB")
                .order(TestUtils.getRandomUUID())
                .paymentDetails("Назначение платежа")
                .qrType(QRType.QRStatic)
                .qrExpirationDate("2023-07-22T09:14:38.107227+03:00")
                .sbpMerchantId(TEST_SBP_MERCHANT_ID)
                .build();

        QRUrl response = TestUtils.CLIENT.registerQR(QR);
        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
    }

    @Test
    void createQRWithoutAmountNegativeTest() {
        QRInfo badQR = QRInfo.builder() // QR without type and without order
                .sbpMerchantId(TEST_SBP_MERCHANT_ID)
                .build();

        assertThrows(SbpException.class, () -> TestUtils.CLIENT.registerQR(badQR));
    }

}
