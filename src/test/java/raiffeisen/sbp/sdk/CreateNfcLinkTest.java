package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.NFC;
import raiffeisen.sbp.sdk.model.out.QRVariable;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class CreateNfcLinkTest {

    @Test
    void createNfcLinkWithExceptionTest() throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        NFC nfc = new NFC(TestUtils.getRandomUUID());

        String randomRefundId = TestUtils.getRandomUUID();
        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.bindNfcLink(nfc));
        assertEquals(String.format(String.format(TestData.QR_DRAFT_DOES_NOT_REGISTERED, nfc.getQrId()), randomRefundId), ex.getMessage());
    }
}
