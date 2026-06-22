package raiffeisen.sbp.sdk.fiscal;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.model.fiscal.Receipt;
import raiffeisen.sbp.sdk.model.fiscal.ReceiptClient;
import raiffeisen.sbp.sdk.model.fiscal.ReceiptItem;
import raiffeisen.sbp.sdk.model.fiscal.VatType;

import java.math.BigDecimal;

public class SellReceiptTest {
    private static final JsonMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    @SneakyThrows
    @Test
    public void test() {
        var receipt = new Receipt();
        ReceiptClient receiptClient = new ReceiptClient();
        receiptClient.setEmail("vladislav_fedotov_official@bk.ru");
        receipt.setClient(receiptClient);
        ReceiptItem receiptItem = new ReceiptItem();
        receiptItem.setName("test");
        receiptItem.setPrice(new BigDecimal(12).setScale(2));
        receiptItem.setQuantity(new BigDecimal(1));
        receiptItem.setAmount(new BigDecimal(12).setScale(2));
        receiptItem.setVatType(VatType.VAT0);
        receipt.setItems(new ReceiptItem[] {receiptItem});
        receipt.setTotal(new BigDecimal(12).setScale(2));
        var json = mapper.valueToTree(receipt);
/*
        System.out.println(json);
*/
        Assertions.assertNotNull(json);
        var secretKey = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJNQTYyMjk3NiIsImp0aSI6ImM5MTBjNGU4LTRhZmMtNDBlMS04ZGU3LWVlODg2N2JiOGU3NCJ9.rnPFEsixy9Wr4GhxT9D9s8dlBg5dRKWMLPfxl48oHAo";

        SbpClient sbpClient = new SbpClient(SbpClient.FISCAL_TEST_URL, "MA622976", secretKey);

    }
}
