package raiffeisen.sbp.sdk;

import org.junit.Test;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreateQrTest {
    @Test
    public void createQRInfoDynamicTest() throws IOException {
        QRInfo QR = QRInfo.creator().
                createDate("2019-08-22T09:14:38.107227+03:00").
                order("TestOrderOfSDKDynamic").
                qrType(QRType.QRDynamic).
                amount(new BigDecimal(314)).
                currency("RUB").
                sbpMerchantId("MA0000000552").
                create();

        Response response = SbpClient.registerQR(SbpClient.URL_REGISTER_TEST, QR);

        System.out.println(response.getCode());
        System.out.println(response.getBody());

        assertEquals(200, response.getCode());
        assertNotEquals(-1, response.getBody().indexOf("SUCCESS"));
    }

    @Test
    public void createQRInfoStaticTest() throws IOException {
        QRInfo QR = QRInfo.creator().
                createDate("2020-07-20T10:14:38.107227+03:00").
                order("TestOrderOfSDKStatic").
                qrType(QRType.QRStatic).
                sbpMerchantId("MA0000000552").
                create();

        Response response = SbpClient.registerQR(SbpClient.URL_REGISTER_TEST, QR);

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
                createDate("2020-07-20T13:14:38.107227+03:00").
                currency("RUB").
                order("TestOrderOfSDKMaxInfo").
                paymentDetails("Назначение платежа").
                qrType(QRType.QRStatic).
                qrExpirationDate("2023-07-22T09:14:38.107227+03:00").
                sbpMerchantId("MA0000000552").
                create();

        Response response = SbpClient.registerQR(SbpClient.URL_REGISTER_TEST, QR);

        System.out.println(response.getCode());
        System.out.println(response.getBody());

        assertEquals(200, response.getCode());
        assertNotEquals(-1, response.getBody().indexOf("SUCCESS"));

    }


}
