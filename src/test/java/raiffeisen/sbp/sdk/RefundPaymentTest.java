package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.data.StatusCodes;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
class RefundPaymentTest {

    public static long dynamicQrTransactionId;
    public static long staticQrTransactionId;

    @BeforeEach
    void setup() throws IOException, SbpException {
        staticQrTransactionId = TestUtils.initStaticQR().getTransactionId();
        dynamicQrTransactionId = TestUtils.initDynamicQR().getTransactionId();
    }

    @Test
    void refundPaymentStaticTest() throws SbpException, IOException {
        String refundId = TestUtils.getRandomUUID();
        BigDecimal moneyAmount = new BigDecimal(100);
        RefundInfo refundInfo = new RefundInfo(moneyAmount, TestUtils.getRandomUUID(), refundId);
        refundInfo.setTransactionId(staticQrTransactionId);

        RefundStatus response = TestUtils.CLIENT.refundPayment(refundInfo);
        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
        assertEquals(moneyAmount, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundPaymentDynamicTest() throws SbpException, IOException {
        String refundId = TestUtils.getRandomUUID();
        BigDecimal moneyAmount = new BigDecimal(100);
        RefundInfo refundInfo = new RefundInfo(moneyAmount, TestUtils.getRandomUUID(), refundId);
        refundInfo.setTransactionId(dynamicQrTransactionId);

        RefundStatus response = TestUtils.CLIENT.refundPayment(refundInfo);
        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
        assertEquals(moneyAmount, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundPaymentWithoutRefundIdNegative() {
        RefundInfo refundInfo = new RefundInfo(BigDecimal.ONE,null,null);
        refundInfo.setTransactionId(dynamicQrTransactionId);

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.refundPayment(refundInfo));
        assertEquals(TestData.MISSING_REFUND_ID_ERROR, ex.getMessage());
    }
}
