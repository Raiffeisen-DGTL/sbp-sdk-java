package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RefundInfo {
    private final BigDecimal amount;
    private final String order;
    private final String refundId;
    private String paymentDetails;
    @Getter(AccessLevel.NONE)
    private Long transactionId;

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTransactionId() {
        return transactionId == null ? null : transactionId;
    }
}
