package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetQrInfoTest {

    private static String TEST_QR_ID = null;

    private final static String TEST_SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTAwMDAwMD" +
            "A1NTIiLCJqdGkiOiI0ZDFmZWIwNy0xZDExLTRjOWEtYmViNi1kZjUwY2Y2Mzc5YTUifQ.pxU8KYfqbVlxvQV7wfbGps" +
            "u4AX1QoY26FqBiuNuyT-s";

    private static SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TEST_SECRET_KEY);

    private final String BAD_QR_ID = "BadQR";

    @BeforeAll
    static void initTest() {
        QRInfo QR = QRInfo.creator().
                createDate(getCreateDate()).
                order(getOrderInfo()).
                qrType(QRType.QRDynamic).
                amount(new BigDecimal(314)).
                currency("RUB").
                sbpMerchantId(TEST_SBP_MERCHANT_ID).
                create();

        QRUrl response = null;
        try {
            response = client.registerQR(QR);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        assert response != null;

        if (response.getCode().equals("SUCCESS")) {
            initQrId(response.getQrId());
        }
    }

    @Test
    public void getQrInfoTest() throws Exception {

        if (TEST_QR_ID == null) {
            System.out.println("InitTest failed");
        } else {

            QRId id = QRId.creator().qrId(TEST_QR_ID).create();

            QRUrl response = client.getQRInfo(id);

            assertEquals("SUCCESS", response.getCode());
        }
    }

    @Test
    public void getQrInfoExceptionTest() {
        QRId badId = QRId.creator().qrId(BAD_QR_ID).create();

        boolean thrown = false;
        try {
            QRUrl response = client.getQRInfo(badId);
            assertNotEquals("SUCCESS", response.getCode());
        }
        catch (Exception e) {
            assertTrue(e instanceof SbpException);
            thrown = true;
        }
        assertTrue(thrown);
    }

    private static final String TEST_SBP_MERCHANT_ID = "MA0000000552";

    private static String getOrderInfo() {
        return UUID.randomUUID().toString();
    }

    private static String getCreateDate() {
        String timestamp = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString();
        return timestamp.substring(0,timestamp.indexOf("["));
    }

    private static void initQrId(String body) {
        TEST_QR_ID = body;
    }

}
