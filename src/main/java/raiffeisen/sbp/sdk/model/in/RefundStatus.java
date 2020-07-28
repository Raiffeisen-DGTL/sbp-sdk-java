package raiffeisen.sbp.sdk.model.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import raiffeisen.sbp.sdk.exception.SbpException;

import java.math.BigDecimal;

@Getter
@Setter
public class RefundStatus {

    private String code;
    private BigDecimal amount;
    private String refundStatus;
}
