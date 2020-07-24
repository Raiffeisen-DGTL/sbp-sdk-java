package raiffeisen.sbp.sdk.model.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import raiffeisen.sbp.sdk.exception.SbpException;

import java.math.BigDecimal;

@Getter
public class RefundStatus {
    private final String code;
    private final BigDecimal amount;
    private final String refundStatus;

    public RefundStatus(String body) throws JsonProcessingException {
        JsonNode json = new ObjectMapper().readTree(body);
        code = json.path("code").asText();
        String amountStr = json.path("amount").asText();
        if(amountStr.length() == 0) {
            amount = null;
        }
        else {
            amount = new BigDecimal(amountStr);
        }
        refundStatus = json.path("refundStatus").asText();
    }
}
