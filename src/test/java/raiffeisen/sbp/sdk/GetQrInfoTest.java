package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QRId;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class GetQrInfoTest {

    private static String qrId;

    @BeforeAll
    @Timeout(15)
    static void initTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        qrId = TestUtils.initDynamicQR().getQrId();
    }

    @Test
    void getQrInfoTest() throws Exception {
        QRId id = new QRId(qrId);
        QRUrl response = TestUtils.CLIENT.getQRInfo(id);

        assertNotNull(response.getQrId());
        assertNotNull(response.getPayload());
        assertNotNull(response.getQrUrl());
    }

    @Test
    void getQrInfoByBadQrIdNegativeTest() {
        String badQrId = TestUtils.getRandomUUID();

        QRId badId = new QRId(badQrId);

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getQRInfo(badId));
        assertEquals(TestData.QR_CODE_NOT_FOUND_ERROR_CODE, ex.getCode());
        assertEquals(TestData.QR_CODE_NOT_FOUND_ERROR_MESSAGE, ex.getMessage());
    }
}


