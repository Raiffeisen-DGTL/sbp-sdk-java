package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.model.out.QRStatic;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class CreateQrTest {

    @Test
    void createQRDynamicTest() throws IOException, SbpException {
        QRDynamic qrDynamic = new QRDynamic(TestUtils.getRandomUUID(), new BigDecimal(314));

        QRUrl response = TestUtils.CLIENT.registerQR(qrDynamic);

        assertNotNull(response.getQrId());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrUrl());
    }

    @Test
    void createQRStaticTest() throws IOException, SbpException {
        QRStatic qrStatic = new QRStatic(TestUtils.getRandomUUID());

        QRUrl response = TestUtils.CLIENT.registerQR(qrStatic);

        assertNotNull(response.getQrUrl());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrId());
    }

    @Test
    void createQRMaxTest() throws IOException, SbpException {
        // Test without "account" parameter
        QRStatic qrStatic = new QRStatic(TestUtils.getRandomUUID());
        qrStatic.setAdditionalInfo("Доп информация");
        qrStatic.setAmount(new BigDecimal(1110));
        qrStatic.setPaymentDetails("Назначение платежа");
        qrStatic.setQrExpirationDate("2023-07-22T09:14:38.107227+03:00");

        QRUrl response = TestUtils.CLIENT.registerQR(qrStatic);
        assertNotNull(response.getQrId());
        assertNotNull(response.getQrUrl());
        assertNotNull(response.getPayload());
    }

    @Test
    void createQRWithoutAmountNegativeTest() {
        QRStatic badQR = new QRStatic("");

        assertThrows(SbpException.class, () -> TestUtils.CLIENT.registerQR(badQR));
    }

}
