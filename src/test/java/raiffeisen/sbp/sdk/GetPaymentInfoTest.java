package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class GetPaymentInfoTest {

    private static String qrId;

    @BeforeAll
    @Timeout(15)
    public static void initTest() throws SbpException, ContractViolationException, IOException {
        qrId = TestUtils.initDynamicQR().getQrId();
    }

    @Test
    void unauthorizedTest() {
        SbpClient clientUnauthorized = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, TestUtils.getRandomUUID());
        QRId id = new QRId(qrId);

        assertThrows(ContractViolationException.class, () -> clientUnauthorized.getPaymentInfo(id));
    }

    @Test
    void getPaymentInfo() throws SbpException, ContractViolationException, IOException {
        QRId id = new QRId(qrId);

        PaymentInfo response = TestUtils.CLIENT.getPaymentInfo(id);

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

        QRId badId = new QRId(badQrId);

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getPaymentInfo(badId));
        assertEquals(TestData.QR_CODE_NOT_FOUND_ERROR_CODE, ex.getCode());
        assertEquals(TestData.QR_CODE_NOT_FOUND_ERROR_MESSAGE, ex.getMessage());
    }

}
