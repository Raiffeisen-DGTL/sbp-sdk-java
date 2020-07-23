package raiffeisen.sbp.sdk;

import org.junit.Test;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreateQrTest {

    private final String TEST_SBP_MERCHANT_ID = "MA0000000552";

    private String getOrderInfo() {
        return UUID.randomUUID().toString();
    }

    private String getCreateDate() {
        String timestamp = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString();
        return timestamp.substring(0,timestamp.indexOf("["));
    }

    private static SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN,"");

    @Test
    public void createQRInfoDynamicTest() throws IOException {
        QRInfo QR = QRInfo.creator().
                createDate(getCreateDate()).
                order(getOrderInfo()).
                qrType(QRType.QRDynamic).
                amount(new BigDecimal(314)).
                currency("RUB").
                sbpMerchantId(TEST_SBP_MERCHANT_ID).
                create();

        Response response = client.registerQR(QR);

        System.out.println(response.getCode());
        System.out.println(response.getBody());

        assertEquals(200, response.getCode());
        assertNotEquals(-1, response.getBody().indexOf("SUCCESS"));

    }

    @Test
    public void createQRInfoStaticTest() throws IOException {
        QRInfo QR = QRInfo.creator().
                createDate(getCreateDate()).
                order(getOrderInfo()).
                qrType(QRType.QRStatic).
                sbpMerchantId(TEST_SBP_MERCHANT_ID).
                create();

        Response response = client.registerQR(QR);

        System.out.println(response.getCode());
        System.out.println(response.getBody());

        assertEquals(200, response.getCode());
        assertNotEquals(-1, response.getBody().indexOf("SUCCESS"));
    }

    @Test
    public void createQRInfoMaxTest() throws IOException {
        // Test without "account" parameter
        QRInfo QR = QRInfo.creator().
                additionalInfo("Доп информация").
                amount(new BigDecimal(1110)).
                createDate(getCreateDate()).
                currency("RUB").
                order(getOrderInfo()).
                paymentDetails("Назначение платежа").
                qrType(QRType.QRStatic).
                qrExpirationDate("2023-07-22T09:14:38.107227+03:00").
                sbpMerchantId(TEST_SBP_MERCHANT_ID).
                create();

        Response response = client.registerQR(QR);

        System.out.println(response.getCode());
        System.out.println(response.getBody());

        assertEquals(200, response.getCode());
        assertNotEquals(-1, response.getBody().indexOf("SUCCESS"));

    }

}
