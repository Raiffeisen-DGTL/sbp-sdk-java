package raiffeisen.sbp.sdk.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.Order;
import raiffeisen.sbp.sdk.model.out.OrderExtra;
import raiffeisen.sbp.sdk.model.out.OrderQr;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRStatic;
import raiffeisen.sbp.sdk.model.out.QRVariable;
import raiffeisen.sbp.sdk.model.out.RefundInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

    public static final SbpClient CLIENT = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, TestData.SECRET_KEY);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private static void payQR(String id) throws IOException {
        HttpPost httpPost = new HttpPost(TestData.PAYMENT_URL);
        InitPayment initPayment = new InitPayment(id, BigDecimal.valueOf(314L));
        StringEntity entity = new StringEntity(MAPPER.writeValueAsString(initPayment));
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            response = client.execute(httpPost);
        }

        assert response.getStatusLine().getStatusCode() == 200;
    }

    public static PaymentInfo initStaticQR() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        String orderInfo = getRandomUUID();

        QRStatic qrStatic = new QRStatic(orderInfo);

        QRUrl qr = CLIENT.registerQR(qrStatic);
        QRId id = new QRId(qr.getQrId());

        payQR(id.getQrId());

        PaymentInfo paymentInfo = CLIENT.getPaymentInfo(id);

        while (paymentInfo.getPaymentStatus().equals("NO_INFO")) {
            paymentInfo = CLIENT.getPaymentInfo(id);
        }

        return paymentInfo;
    }

    public static PaymentInfo initDynamicQR() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        String orderInfo = getRandomUUID();
        BigDecimal moneyAmount = new BigDecimal(314);

        QRDynamic qrDynamic = new QRDynamic(orderInfo, moneyAmount);

        QRUrl qr = CLIENT.registerQR(qrDynamic);
        QRId id = new QRId(qr.getQrId());

        payQR(id.getQrId());

        PaymentInfo paymentInfo = CLIENT.getPaymentInfo(id);

        while (paymentInfo.getPaymentStatus().equals("NO_INFO")) {
            paymentInfo = CLIENT.getPaymentInfo(id);
        }

        return paymentInfo;
    }

    public static String refundPayment(BigDecimal amount, long transactionId)
            throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        String refundId = getRandomUUID();
        String orderInfo = getRandomUUID();

        RefundInfo refundInfo = new RefundInfo(amount, orderInfo, refundId);
        refundInfo.setTransactionId(transactionId);
        refundInfo.setPaymentDetails("Возврат");

        RefundStatus response = CLIENT.refundPayment(refundInfo);

        assert (response.getAmount().equals(amount));
        assert (!response.getRefundStatus().isEmpty());

        return refundId;
    }

    public static OrderInfo initOrderWithQrVariable() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        QRVariable qrVariable = new QRVariable();
        QRUrl qr = TestUtils.CLIENT.registerQR(qrVariable);

        OrderQr orderQr = new OrderQr();
        orderQr.setId(qr.getQrId());

        Order order = Order.builder()
                .amount(new BigDecimal(314))
                .qr(orderQr)
                .comment("comment")
                .extra(new OrderExtra("apiClient", "1.0.3"))
                .build();

        return TestUtils.CLIENT.createOrder(order);
    }
}