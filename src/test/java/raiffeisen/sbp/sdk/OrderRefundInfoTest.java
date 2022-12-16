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
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.OrderRefund;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class OrderRefundInfoTest {

    private static final BigDecimal AMOUNT = new BigDecimal(100);
    private static String refundId;
    private static String orderId;

    @BeforeAll
    @Timeout(15)
    static void setup() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        OrderInfo orderInfo = TestUtils.initOrderWithQrVariable();
        orderId = orderInfo.getId();
        refundId = TestUtils.getRandomUUID();
        TestUtils.CLIENT.orderRefund(new OrderRefund(orderId, refundId, AMOUNT));

    }

    @Test
    void refundInfoTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        RefundStatus response = TestUtils.CLIENT.orderRefundStatus(orderId, refundId);

        assertEquals(AMOUNT, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundInfoExceptionTest() {
        String randomRefundId = TestUtils.getRandomUUID();
        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.orderRefundStatus(orderId, randomRefundId));
        assertEquals(String.format(TestData.REFUND_NOT_FOUND_ERROR_MESSAGE, randomRefundId), ex.getMessage());
    }

}
