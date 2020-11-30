package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundInfo {
    @Setter(AccessLevel.NONE)
    final BigDecimal amount;
    @Setter(AccessLevel.NONE)
    final String order;
    @Setter(AccessLevel.NONE)
    final String refundId;

    String paymentDetails;
    long transactionId;
}
