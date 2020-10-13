package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeEach;
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
        RefundInfo refundInfo =
                RefundInfo
                        .builder()
                        .amount(moneyAmount)
                        .order(TestUtils.getRandomUUID())
                        .refundId(refundId)
                        .transactionId(staticQrTransactionId)
                        .build();

        RefundStatus response = TestUtils.CLIENT.refundPayment(refundInfo);
        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
        assertEquals(moneyAmount, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundPaymentDynamicTest() throws SbpException, IOException {
        String refundId = TestUtils.getRandomUUID();
        BigDecimal moneyAmount = new BigDecimal(100);
        RefundInfo refundInfo = RefundInfo
                .builder()
                .amount(moneyAmount)
                .order(TestUtils.getRandomUUID())
                .refundId(refundId)
                .transactionId(dynamicQrTransactionId)
                .build();

        RefundStatus response = TestUtils.CLIENT.refundPayment(refundInfo);
        assertEquals(StatusCodes.SUCCESS.getMessage(), response.getCode());
        assertEquals(moneyAmount, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundPaymentWithoutRefundIdNegative() {
        RefundInfo refundInfo = RefundInfo
                .builder()
                .amount(new BigDecimal(1))
                .transactionId(dynamicQrTransactionId)
                .build();

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.refundPayment(refundInfo));
        assertEquals(TestData.MISSING_REFUND_ID_ERROR, ex.getMessage());
    }
}
