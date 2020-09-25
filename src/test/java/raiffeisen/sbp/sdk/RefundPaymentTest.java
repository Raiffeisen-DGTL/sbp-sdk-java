package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.utils.TestData;
import raiffeisen.sbp.sdk.utils.TestUtils;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class RefundPaymentTest {

    public static long dynamicQrTransactionId;
    public static long staticQrTransactionId;

    @BeforeEach
    void setup() throws IOException, SbpException {
        staticQrTransactionId = TestUtils.initStaticQR();
        dynamicQrTransactionId = TestUtils.initDynamicQR();
    }

    @Test
    void refundPaymentStaticTest() throws SbpException, IOException {
        String refundId = TestUtils.getRandomUUID();
        BigDecimal moneyAmount = new BigDecimal(100);
        RefundInfo refundInfo =
                RefundInfo
                        .creator()
                        .amount(moneyAmount)
                        .order(TestUtils.getRandomUUID())
                        .refundId(refundId)
                        .transactionId(staticQrTransactionId)
                        .create();

        RefundStatus response = TestUtils.CLIENT.refundPayment(refundInfo);
        assertEquals("SUCCESS", response.getCode());
        assertEquals(moneyAmount, response.getAmount());
        assertTrue(response.getRefundStatus().equals("COMPLETED") || response.getRefundStatus().equals("IN_PROGRESS"));
    }

    @Test
    void refundPaymentDynamicTest() throws SbpException, IOException {
        String refundId = TestUtils.getRandomUUID();
        BigDecimal moneyAmount = new BigDecimal(100);
        RefundInfo refundInfo = RefundInfo
                .creator()
                .amount(moneyAmount)
                .order(TestUtils.getRandomUUID())
                .refundId(refundId)
                .transactionId(dynamicQrTransactionId)
                .create();

        RefundStatus response = TestUtils.CLIENT.refundPayment(refundInfo);
        assertEquals("SUCCESS", response.getCode());
        assertEquals(moneyAmount, response.getAmount());
        assertTrue(response.getRefundStatus().equals("COMPLETED") || response.getRefundStatus().equals("IN_PROGRESS"));
    }

    @Test
    void refundPaymentWithoutOrderNegative() {
        String refundId = TestUtils.getRandomUUID();
        RefundInfo refundInfo = RefundInfo
                .creator()
                .amount(new BigDecimal(1))
                .refundId(refundId)
                .transactionId(dynamicQrTransactionId)
                .create();

        assertThrows(SbpException.class, () -> TestUtils.CLIENT.refundPayment(refundInfo));
    }

    @Test
    void refundPaymentExceptionOldTest() {
        String refundId = TestUtils.getRandomUUID();
        RefundInfo refundInfo = RefundInfo.creator(). // without order info
                amount(new BigDecimal(1)).
                refundId(refundId).
                transactionId(dynamicQrTransactionId).
                create();


        try {
            TestUtils.CLIENT.refundPayment(refundInfo);
            assert false;
        }
        catch (Exception e) {
            assert (e instanceof SbpException);
        }
    }

}
