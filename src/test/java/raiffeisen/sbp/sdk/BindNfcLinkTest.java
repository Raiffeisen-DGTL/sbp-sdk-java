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
        NFC nfc = new NFC(TestUtils.getRandomUUID());

        String qrId = nfc.getQrId();
        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.bindNfcLink(nfc));
        assertEquals(String.format(String.format(TestData.QR_DRAFT_DOES_NOT_REGISTERED, qrId), qrId), ex.getMessage());
    }
}
