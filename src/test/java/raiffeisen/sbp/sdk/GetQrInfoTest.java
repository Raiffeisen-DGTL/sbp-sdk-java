package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.data.StatusCodes;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetQrInfoTest {

    private static String qrId;

    @BeforeAll
    static void initTest() throws IOException, SbpException {
        qrId = TestUtils.initDynamicQR().getQrId();
    }

    @Test
    void getQrInfoTest() throws Exception {
        QRId id = QRId.builder().qrId(qrId).build();
        QRUrl response = TestUtils.CLIENT.getQRInfo(id);

        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
    }

    @Test
    void getQrInfoByBadQrIdNegativeTest() {
        String badQrId = TestUtils.getRandomUUID();

        QRId badId = QRId.builder().qrId(badQrId).build();

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getQRInfo(badId));
        assertEquals(TestData.QR_CODE_NOT_MATCHING_ERROR, ex.getMessage());
    }
}
