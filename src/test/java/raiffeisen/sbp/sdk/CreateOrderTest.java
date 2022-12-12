package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.Order;
import raiffeisen.sbp.sdk.model.out.OrderExtra;
import raiffeisen.sbp.sdk.model.out.OrderQr;
import raiffeisen.sbp.sdk.model.out.QRVariable;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("integration")
public class CreateOrderTest {
    @Test
    void createOrderTest() throws IOException, ContractViolationException, SbpException, URISyntaxException, InterruptedException {
        Order order = Order.builder().amount(new BigDecimal(314)).build();
        OrderInfo response = TestUtils.CLIENT.createOrder(order);

        assertNotNull(response.getId());
        assertNotNull(response.getAmount());
    }

    @Test
    void createOrderWithQr() throws SbpException, IOException, ContractViolationException, URISyntaxException, InterruptedException {
        QRVariable qrVariable = new QRVariable();
        QRUrl qr = TestUtils.CLIENT.registerQR(qrVariable);

        OrderQr orderQr = new OrderQr();
        orderQr.setId(qr.getQrId());

        Order order = Order.builder().amount(new BigDecimal(314)).qr(orderQr).build();
        OrderInfo response = TestUtils.CLIENT.createOrder(order);

        assertNotNull(response.getId());
        assertNotNull(response.getAmount());
        assertNotNull(response.getQr());
    }

    @Test
    void createOrderWithExtra() throws SbpException, IOException, ContractViolationException, URISyntaxException, InterruptedException {
        OrderExtra orderExtra = new OrderExtra("apiClient", "1.0.2");
        Order order = Order.builder().amount(new BigDecimal(314)).extra(orderExtra).build();
        OrderInfo response = TestUtils.CLIENT.createOrder(order);
        assertNotNull(response.getId());
        assertNotNull(response.getAmount());
        assertNotNull(response.getExtra());

    }
}
