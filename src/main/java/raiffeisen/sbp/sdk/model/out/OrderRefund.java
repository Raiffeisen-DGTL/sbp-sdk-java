package raiffeisen.sbp.sdk.model.out;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRefund {
    private final String orderId;
    private final String refundId;
    private final BigDecimal amount;
    private String paymentDetails;
}
