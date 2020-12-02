package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RefundInfo {
    final BigDecimal amount;
    final String order;
    final String refundId;

    String paymentDetails;
    long transactionId;
}
