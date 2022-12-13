package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.out.Order;
import raiffeisen.sbp.sdk.model.out.OrderId;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class OrderCancellationTest {

    @Test
    void OrderSuccessCancellation() throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        Order order = Order.builder().amount(new BigDecimal(314)).build();
        OrderInfo response = TestUtils.CLIENT.createOrder(order);

        TestUtils.CLIENT.orderCancellation(new OrderId(response.getId()));
    }

    @Test
    void OrderCancellationWithNotFoundStatus() {
        OrderId badOrderId = new OrderId(TestUtils.getRandomUUID());

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.orderCancellation(badOrderId));
        assertEquals(TestData.ORDER_NOT_FOUND_ERROR_CODE, ex.getCode());
        assertEquals(String.format(TestData.ORDER_NOT_FOUND_ERROR_MESSAGE, badOrderId.getOrderId()), ex.getMessage());
    }

    @Test
    void OrderCancellationWithPaidStatus() throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        OrderInfo response = TestUtils.initOrderWithQrVariable();
        OrderId orderId = new OrderId(response.getId());

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.orderCancellation(orderId));
        assertEquals(TestData.ORDER_HAS_FINAL_STATUS_CODE, ex.getCode());
        assertEquals(String.format(TestData.ORDER_HAS_FINAL_STATUS_MESSAGE, orderId.getOrderId()), ex.getMessage());
    }
}
