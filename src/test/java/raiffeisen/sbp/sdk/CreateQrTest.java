package raiffeisen.sbp.sdk;

import org.junit.Test;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.QRInfoCreator;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateQrTest {
    @Test
    public void createQrInfoTest() throws IOException {
        QRInfo QR = QRInfoCreator.creator().
                createDate("2019-08-22T09:14:38.107227+03:00").
                order("FirstOrderofSDK").
                qrType(QRType.QRDynamic).
                amount(new BigDecimal(315)).
                currency("RUB").
                sbpMerchantId("MA0000000552").
                create();

        Response response = SbpClient.registerQR(QR);
        response.getCode();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        assertEquals(200, response.getCode());
    }


}
