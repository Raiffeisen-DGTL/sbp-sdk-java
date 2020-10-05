package raiffeisen.sbp.sdk.model.out;

import lombok.Builder;
import lombok.Value;
import lombok.Getter;

@Value
@Getter
@Builder
public class RefundId {
    String refundId;
}
