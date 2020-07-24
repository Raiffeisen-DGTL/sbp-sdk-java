package raiffeisen.sbp.sdk.model.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentInfo {

    private final String additionalInfo;
    private final BigDecimal amount;
    private final String code;
    private final String createDate;
    private final String currency;
    private final long merchantId;
    private final String order;
    private final String paymentStatus;
    private final String qrId;
    private final String sbpMerchantId;
    private final String transactionDate;
    private final long transactionId;

    public PaymentInfo(String body) throws JsonProcessingException {
        JsonNode json = new ObjectMapper().readTree(body);
        code = json.path("code").asText();
        additionalInfo = json.path("additionalInfo").asText();
        String amountStr = json.path("amount").asText();
        if(amountStr.length() == 0) {
            amount = null;
        }
        else {
            amount = new BigDecimal(amountStr);
        }
        createDate = json.path("createDate").asText();
        currency = json.path("currency").asText();
        merchantId = json.path("merchantId").asLong();
        order = json.path("order").asText();
        paymentStatus = json.path("paymentStatus").asText();
        qrId = json.path("qrId").asText();
        sbpMerchantId = json.path("sbpMerchantId").asText();
        transactionDate = json.path("transactionDate").asText();
        transactionId = json.path("transactionId").asLong();
    }
}
