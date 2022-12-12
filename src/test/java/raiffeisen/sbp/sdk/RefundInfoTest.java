package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import raiffeisen.sbp.sdk.data.StatusCodes;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.RefundId;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class RefundInfoTest {

    private static final BigDecimal AMOUNT = new BigDecimal(100);
    private static RefundId refundId;

    @BeforeAll
    @Timeout(15)
    static void setup() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        long transactionId = TestUtils.initStaticQR().getTransactionId();
        String id = TestUtils.refundPayment(AMOUNT, transactionId);
        refundId = new RefundId(id);
    }

    @Test
    void refundInfoTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        RefundStatus response = TestUtils.CLIENT.getRefundInfo(refundId);

        assertEquals(AMOUNT, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundInfoExceptionTest() {
        String id = TestUtils.getRandomUUID();
        RefundId randomRefundId = new RefundId(id);
        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getRefundInfo(randomRefundId));
        assertEquals(TestData.getNotFoundRefundError(id), ex.getMessage());
    }
}
