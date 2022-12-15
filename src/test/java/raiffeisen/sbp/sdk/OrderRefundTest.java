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
import raiffeisen.sbp.sdk.model.out.OrderRefundId;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class OrderRefundTest {
    private static final BigDecimal AMOUNT = new BigDecimal(100);
    private static OrderInfo orderInfo;

    @BeforeAll
    @Timeout(15)
    static void setup() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        orderInfo = TestUtils.initOrderWithQrVariable();
    }

    @Test
    void OrderRefundSuccessTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        String refundId = TestUtils.getRandomUUID();
        OrderRefundId orderRefundId = new OrderRefundId(orderInfo.getId(), refundId);
        OrderRefund orderRefund = new OrderRefund(AMOUNT);

        RefundStatus response = TestUtils.CLIENT.orderRefund(orderRefund, orderRefundId);

        assertEquals(AMOUNT, response.getAmount());
        assertEquals(StatusCodes.IN_PROGRESS.getMessage(), response.getRefundStatus());
    }

    @Test
    void refundInfoExceptionTest() {
        String refundId = TestUtils.getRandomUUID();
        String orderId = TestUtils.getRandomUUID();
        OrderRefundId orderRefundId = new OrderRefundId(orderId, refundId);
        OrderRefund orderRefund = new OrderRefund(AMOUNT);
        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.orderRefund(orderRefund, orderRefundId));
        assertEquals(TestData.ORDER_REFUND_WHEN_DOES_NOT_EXIST, ex.getMessage());
    }
}
