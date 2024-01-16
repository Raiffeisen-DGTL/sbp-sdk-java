package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.model.out.QRStatic;
import raiffeisen.sbp.sdk.model.out.QRVariable;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class CreateQrTest {

    @Test
    void createQRDynamicTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        QRDynamic qrDynamic = new QRDynamic(TestUtils.getRandomUUID(), new BigDecimal(314));

        QRUrl response = TestUtils.CLIENT.registerQR(qrDynamic);

        assertNotNull(response.getQrId());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrUrl());
    }

    @Test
    void createQRStaticTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        QRStatic qrStatic = new QRStatic(TestUtils.getRandomUUID());

        QRUrl response = TestUtils.CLIENT.registerQR(qrStatic);

        assertNotNull(response.getQrUrl());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrId());
    }

    @Test
    void createQRVariableTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        QRVariable qrVariable = new QRVariable();
        QRUrl response = TestUtils.CLIENT.registerQR(qrVariable);

        assertNotNull(response.getQrUrl());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrId());
    }

    @Test
    void createQRMaxTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        // Test without "account" parameter
        QRStatic qrStatic = new QRStatic(TestUtils.getRandomUUID());
        qrStatic.setAdditionalInfo("Доп информация");
        qrStatic.setAmount(new BigDecimal(1110));
        qrStatic.setPaymentDetails("Назначение платежа");
        qrStatic.setQrExpirationDate("2025-07-22T09:14:38.107227+03:00");
        qrStatic.setQrDescription("Наименование платежа");

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
