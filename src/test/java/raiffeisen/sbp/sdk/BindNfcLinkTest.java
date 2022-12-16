package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.out.NFC;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class BindNfcLinkTest {

    @Test
    void createNfcLinkWithExceptionTest() {
        String qrId = TestUtils.getRandomUUID();
        NFC nfc = new NFC(qrId);

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.bindNfcLink(nfc));
        assertEquals(String.format(TestData.QR_DRAFT_DOES_NOT_REGISTERED, qrId), ex.getMessage());
    }
}
