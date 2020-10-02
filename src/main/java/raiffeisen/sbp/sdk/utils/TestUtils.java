package raiffeisen.sbp.sdk.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.model.out.RefundInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static final SbpClient CLIENT = new SbpClient(SbpClient.TEST_DOMAIN, TestData.SECRET_KEY);

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getCreateDate() {
        String timestamp = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString();
        return timestamp.substring(0, timestamp.indexOf("["));
    }

    private static void payQR(QRId id) throws IOException {
        HttpGet getter = new HttpGet(TestData.PAYMENT_URL.replace("*", id.getQrId()));

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(getter);

        assert response.getStatusLine().getStatusCode() == 200;
    }

    public static PaymentInfo initStaticQR() throws IOException, SbpException {
        String orderInfo = getRandomUUID();
        QRInfo qrStatic = QRInfo
                .creator()
                .createDate(getCreateDate())
                .order(orderInfo)
                .qrType(QRType.QRStatic)
                .sbpMerchantId(TestData.SBP_MERCHANT_ID)
                .create();

        QRUrl qr = CLIENT.registerQR(qrStatic);
        QRId id = QRId.creator().qrId(qr.getQrId()).create();

        payQR(id);

        return CLIENT.getPaymentInfo(id);
    }

    public static PaymentInfo initDynamicQR() throws SbpException, IOException {
        String orderInfo = getRandomUUID();
        BigDecimal moneyAmount = new BigDecimal(314);
        QRInfo qrDynamic = QRInfo.creator()
                .createDate(getCreateDate())
                .order(orderInfo)
                .qrType(QRType.QRDynamic)
                .amount(moneyAmount)
                .currency("RUB")
                .sbpMerchantId(TestData.SBP_MERCHANT_ID)
                .create();

        QRUrl qr = CLIENT.registerQR(qrDynamic);
        QRId id = QRId.creator().qrId(qr.getQrId()).create();

        payQR(id);

        return CLIENT.getPaymentInfo(id);
    }

    public static String refundPayment(BigDecimal amount, long transactionId) throws IOException, SbpException {
        String refundId = getRandomUUID();
        String orderInfo = getRandomUUID();

        RefundInfo refundInfo = RefundInfo.creator()
                .amount(amount)
                .order(orderInfo)
                .refundId(refundId)
                .transactionId(transactionId)
                .create();

        RefundStatus response = CLIENT.refundPayment(refundInfo);
        assert response.getCode().equals(StatusCodes.SUCCESS.getMessage());

        return refundId;
    }
}