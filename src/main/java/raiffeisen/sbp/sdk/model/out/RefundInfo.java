package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RefundInfo {
    private final BigDecimal amount;
    private final String order;
    private final String refundId;
    private String paymentDetails;
    private long transactionId;
}
