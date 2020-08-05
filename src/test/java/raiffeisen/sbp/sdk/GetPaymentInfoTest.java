package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetPaymentInfoTest {

    private static String TEST_QR_ID = null;

    private final static String TEST_SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTAwMDAwMD" +
            "A1NTIiLCJqdGkiOiI0ZDFmZWIwNy0xZDExLTRjOWEtYmViNi1kZjUwY2Y2Mzc5YTUifQ.pxU8KYfqbVlxvQV7wfbGps" +
            "u4AX1QoY26FqBiuNuyT-s";

    private static final SbpClient client = new SbpClient(SbpClient.TEST_DOMAIN, TEST_SECRET_KEY);

    private static final SbpClient clientUnauthorized = new SbpClient(SbpClient.TEST_DOMAIN, TEST_SECRET_KEY + "-");

    private static final String TEST_SBP_MERCHANT_ID = "MA0000000552";

    private static final String BAD_QR_ID = "BadQR";

    @BeforeAll
    public static void initTest() {
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

            if (response.getCode().equals("SUCCESS")) {
                initQrId(response.getQrId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void unauthorizedTest() {
        QRId id = QRId.creator().qrId(TEST_QR_ID).create();

        try {
            clientUnauthorized.getPaymentInfo(id);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof SbpException);
            assertTrue(e.getMessage().contains("Unauthorized"));
        }
    }

    @Test
    public void getPaymentInfo() {
        if (TEST_QR_ID == null) {
            System.out.println("InitTest failed");
        } else {

            QRId id = QRId.creator().qrId(TEST_QR_ID).create();

            try {
                PaymentInfo response = client.getPaymentInfo(id);
                assertEquals("SUCCESS", response.getCode());
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                fail();
            }
        }
    }

    @Test
    public void getPaymentInfoExceptionTest() {
        QRId badId = QRId.creator().qrId(BAD_QR_ID).create();

        boolean thrown = false;
        try {
            PaymentInfo response = client.getPaymentInfo(badId);
            assertNotEquals("SUCCESS", response.getCode());
        }
        catch (Exception e) {
            assertTrue(e instanceof SbpException);
            thrown = true;
        }
        assertTrue(thrown);
    }

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
