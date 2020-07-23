package raiffeisen.sbp.sdk;

import org.junit.Before;
import org.junit.Test;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GetPaymentInfoTest {

    private String TEST_QR_ID = null;

    private final static String TEST_SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTAwMDAwMD" +
            "A1NTIiLCJqdGkiOiI0ZDFmZWIwNy0xZDExLTRjOWEtYmViNi1kZjUwY2Y2Mzc5YTUifQ.pxU8KYfqbVlxvQV7wfbGps" +
            "u4AX1QoY26FqBiuNuyT-s";

    private static SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TEST_SECRET_KEY);

    private final String TEST_SBP_MERCHANT_ID = "MA0000000552";

    @Before
    public void initTest() {
        QRInfo QR = QRInfo.creator().
                createDate(getCreateDate()).
                order(getOrderInfo()).
                qrType(QRType.QRDynamic).
                amount(new BigDecimal(314)).
                currency("RUB").
                sbpMerchantId(TEST_SBP_MERCHANT_ID).
                create();

        Response response = null;
        try {
            response = client.registerQR(QR);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(response.getCode());
        System.out.println(response.getBody());

        if ((response.getCode() == 200) && (response.getBody().indexOf("SUCCESS") != -1)) {
            initQrId(response.getBody());
        }
    }

    @Test
    public void getPaymentInfo() throws IOException {
        if (TEST_QR_ID == null) {
            System.out.println("InitTest failed");
        } else {

            QRId id = QRId.creator().qrId(TEST_QR_ID).create();

            Response response = client.getPaymentInfo(id);

            System.out.println(response.getCode());
            System.out.println(response.getBody());

            assertEquals(200, response.getCode());
            assertNotEquals(-1, response.getBody().indexOf("SUCCESS"));
        }
    }

    private String getOrderInfo() {
        return UUID.randomUUID().toString();
    }

    private String getCreateDate() {
        String timestamp = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString();
        return timestamp.substring(0,timestamp.indexOf("["));
    }

    private void initQrId(String body) {
        body = body.substring(body.indexOf("qrId") + 7, body.indexOf("payload")-3);
        System.out.println(body);
        TEST_QR_ID = body;
    }

}
