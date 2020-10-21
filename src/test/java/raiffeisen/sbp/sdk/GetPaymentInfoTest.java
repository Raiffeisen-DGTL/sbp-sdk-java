package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.data.StatusCodes;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class GetPaymentInfoTest {

    private static String qrId;

    @BeforeAll
    public static void initTest() throws IOException, SbpException {
        qrId = TestUtils.initDynamicQR().getQrId();
    }

    @Test
    void unauthorizedTest() {
        SbpClient clientUnauthorized = new SbpClient(SbpClient.TEST_DOMAIN, TestUtils.getRandomUUID());
        QRId id = QRId.builder().qrId(qrId).build();

        assertThrows(SbpException.class, () -> clientUnauthorized.getPaymentInfo(id));
    }

    @Test
    void getPaymentInfo() throws IOException, SbpException {
        QRId id = QRId.builder().qrId(qrId).build();

        PaymentInfo response = TestUtils.CLIENT.getPaymentInfo(id);
        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
    }

    @Test
    void getPaymentInfoExceptionTest() {
        String badQrId = TestUtils.getRandomUUID();

        QRId badId = QRId.builder().qrId(badQrId).build();

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getPaymentInfo(badId));
        assertEquals(TestData.QR_CODE_NOT_MATCHING_ERROR, ex.getMessage());
    }

}
