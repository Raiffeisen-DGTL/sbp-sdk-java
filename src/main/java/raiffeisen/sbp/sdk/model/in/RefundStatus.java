package raiffeisen.sbp.sdk.model.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class RefundStatus {
    private final String code;
    private final BigDecimal amount;
    private final String refundStatus;

    public RefundStatus(String body) throws JsonProcessingException {
        JsonNode json = new ObjectMapper().readTree(body);
        code = json.path("code").asText();
        amount = new BigDecimal(json.path("amount").asText());
        refundStatus = json.path("refundStatus").asText();
    }
}
