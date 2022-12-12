package raiffeisen.sbp.sdk;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.data.TestUtils;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.out.ModelId;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("integration")
public class GetOrderInfoTest {

    private static String orderId;

    @BeforeAll
    @Timeout(15)
    static void initTest() throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        orderId = TestUtils.initOrderWithQrVariable().getId();
    }

    @Test
    void getOrderInfoTest() throws Exception {
        ModelId id = new ModelId(orderId);
        OrderInfo response = TestUtils.CLIENT.getOrderInfo(id);

        assertNotNull(response.getId());
        assertNotNull(response.getAmount());
        assertNotNull(response.getComment());
        assertNotNull(response.getExtra());
        assertNotNull(response.getStatus());
        assertNotNull(response.getExpirationDate());
        assertNotNull(response.getQr());
    }

    @Test
    void getOrderInfoByBadQrIdNegativeTest() {
        String badQrId = TestUtils.getRandomUUID();

        ModelId badId = new ModelId(badQrId);

        SbpException ex = assertThrows(SbpException.class, () -> TestUtils.CLIENT.getOrderInfo(badId));
        assertEquals(TestData.ORDER_NOT_FOUND_ERROR_CODE, ex.getCode());
        assertEquals(TestData.ORDER_NOT_FOUND_ERROR_MESSAGE.replace("?", badQrId), ex.getMessage());
    }
}
