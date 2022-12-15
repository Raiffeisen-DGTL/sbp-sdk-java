package raiffeisen.sbp.sdk.model.out;

import lombok.Data;

@Data
public class OrderRefundId {
    private final String orderId;
    private final String refundId;
}
