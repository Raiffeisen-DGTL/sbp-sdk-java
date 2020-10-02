package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.utils.StatusCodes;
import raiffeisen.sbp.sdk.utils.TestData;
import raiffeisen.sbp.sdk.utils.TestUtils;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefundInfoTest {

    private static final BigDecimal AMOUNT = new BigDecimal(100);
    private static String refundId;

    @BeforeAll
    static void setup() throws IOException, SbpException {
        long transactionId = TestUtils.initStaticQR().getTransactionId();
        refundId = TestUtils.refundPayment(AMOUNT, transactionId);
    }

    @Test
    void refundInfoTest() throws IOException, SbpException {
        RefundStatus response = TestUtils.CLIENT.getRefundInfo(refundId);

        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
        assertEquals(AMOUNT, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundInfoExceptionTest() {
        String refundId = TestUtils.getRandomUUID();
        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getRefundInfo(refundId));
        assertEquals(TestData.getNotFoundRefundError(refundId), ex.getMessage());
    }
}
