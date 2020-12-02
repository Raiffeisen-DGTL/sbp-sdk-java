package raiffeisen.sbp.sdk.model.in;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class RefundStatus {
    BigDecimal amount;
    String refundStatus;
}
