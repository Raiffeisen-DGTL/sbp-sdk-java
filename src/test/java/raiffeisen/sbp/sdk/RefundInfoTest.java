package raiffeisen.sbp.sdk;

import org.junit.Test;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.model.Response;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RefundInfoTest {

    private static final String SECRET_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJNQTAwMDAwMDA1NTIiLCJqdGkiOiI0ZDFmZWIwNy0xZDExLTRjOWEtYmViNi" +
            "1kZjUwY2Y2Mzc5YTUifQ.pxU8KYfqbVlxvQV7wfbGpsu4AX1QoY26FqBiuNuyT-s";

    @Test
    public void refundInfoTest() throws IOException {

        Response response = SbpClient.getRefundInfo(SbpClient.TEST_DOMAIN, getRefundId(), SECRET_KEY);

        System.out.println(response.getCode());
        System.out.println(response.getBody());

        assertEquals(200, response.getCode());
        assertTrue(response.getBody().contains("SUCCESS"));
        assertTrue(response.getBody().contains("IN_PROGRESS")
                || response.getBody().contains("COMPLETED"));

    }

    private static String getRefundId() {
        return UUID.randomUUID().toString();
    }
}
