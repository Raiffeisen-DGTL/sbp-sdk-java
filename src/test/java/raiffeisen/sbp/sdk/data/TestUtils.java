package raiffeisen.sbp.sdk.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRStatic;
import raiffeisen.sbp.sdk.model.out.RefundInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

    public static final SbpClient CLIENT = new SbpClient(SbpClient.TEST_URL, TestData.TEST_SBP_MERCHANT_ID, TestData.SECRET_KEY);

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    private static void payQR(QRId id) throws IOException {
        HttpGet getter = new HttpGet(TestData.PAYMENT_URL.replace("*", id.getQrId()));

        CloseableHttpResponse response;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            response = client.execute(getter);
        }

        assert response.getStatusLine().getStatusCode() == 200;
    }

    public static PaymentInfo initStaticQR() throws IOException, SbpException {
        String orderInfo = getRandomUUID();

        QRStatic qrStatic = new QRStatic(orderInfo);

        QRUrl qr = CLIENT.registerQR(qrStatic);
        QRId id = new QRId(qr.getQrId());

        payQR(id);

        PaymentInfo paymentInfo = CLIENT.getPaymentInfo(id);

        while (paymentInfo.getPaymentStatus().equals("NO_INFO")) {
            paymentInfo = CLIENT.getPaymentInfo(id);
        }

        return paymentInfo;
    }

    public static PaymentInfo initDynamicQR() throws SbpException, IOException {
        String orderInfo = getRandomUUID();
        BigDecimal moneyAmount = new BigDecimal(314);

        QRDynamic qrDynamic = new QRDynamic(orderInfo,moneyAmount);

        QRUrl qr = CLIENT.registerQR(qrDynamic);
        QRId id = new QRId(qr.getQrId());

        payQR(id);

        PaymentInfo paymentInfo = CLIENT.getPaymentInfo(id);

        while (paymentInfo.getPaymentStatus().equals("NO_INFO")) {
            paymentInfo = CLIENT.getPaymentInfo(id);
        }

        return paymentInfo;
    }

    public static String refundPayment(BigDecimal amount, long transactionId) throws IOException, SbpException {
        String refundId = getRandomUUID();
        String orderInfo = getRandomUUID();

        RefundInfo refundInfo = new RefundInfo(amount, orderInfo, refundId);
        refundInfo.setTransactionId(transactionId);

        RefundStatus response = CLIENT.refundPayment(refundInfo);
        //assert response.getCode().equals(StatusCodes.SUCCESS.getMessage());

        return refundId;
    }
}