package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class GetPaymentInfoTest {

    private static String qrId;

    @BeforeAll
    @Timeout(15)
    public static void initTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        qrId = TestUtils.initDynamicQR().getQrId();
    }

    @Test
    void unauthorizedTest() {
        SbpClient clientUnauthorized = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, TestUtils.getRandomUUID());

        assertThrows(ContractViolationException.class, () -> clientUnauthorized.getPaymentInfo(qrId));
    }

    @Test
    void getPaymentInfo() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        PaymentInfo response = TestUtils.CLIENT.getPaymentInfo(qrId);

        assertNotNull(response.getAmount());
        assertNotNull(response.getCreateDate());
        assertNotNull(response.getCurrency());
        assertNotNull(response.getOrder());
        assertNotNull(response.getTransactionDate());
        assertNotNull(response.getPaymentStatus());
        assertNotNull(response.getQrId());
    }

    @Test
    void getPaymentInfoExceptionTest() {
        String badQrId = TestUtils.getRandomUUID();

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getPaymentInfo(badQrId));
        assertEquals(TestData.QR_CODE_NOT_FOUND_ERROR_CODE, ex.getCode());
        assertEquals(TestData.QR_CODE_NOT_FOUND_ERROR_MESSAGE, ex.getMessage());
    }

}
