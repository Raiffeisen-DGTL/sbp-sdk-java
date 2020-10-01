package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundInfo {

    BigDecimal amount;
    String order;
    String refundId;
    long transactionId;
}
