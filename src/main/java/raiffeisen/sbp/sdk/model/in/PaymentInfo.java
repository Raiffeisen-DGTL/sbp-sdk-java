package raiffeisen.sbp.sdk.model.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Getter
public class PaymentInfo {

    private final String additionalInfo;
    private final String amount;
    private final String code;
    private final String createDate;
    private final String currency;
    private final String merchantId;
    private final String order;
    private final String paymentStatus;
    private final String qrId;
    private final String sbpMerchantId;
    private final String transactionDate;
    private final String transactionId;

    public PaymentInfo(String body) throws JsonProcessingException {
        JsonNode json = new ObjectMapper().readTree(body);
        code = json.path("code").asText();
        additionalInfo = json.path("additionalInfo").asText();
        amount = json.path("amount").asText();
        createDate = json.path("createDate").asText();
        currency = json.path("currency").asText();
        merchantId = json.path("merchantId").asText();
        order = json.path("order").asText();
        paymentStatus = json.path("paymentStatus").asText();
        qrId = json.path("qrId").asText();
        sbpMerchantId = json.path("sbpMerchantId").asText();
        transactionDate = json.path("transactionDate").asText();
        transactionId = json.path("transactionId").asText();
    }
}
