package raiffeisen.sbp.sdk.model.in;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RefundStatus {

    private String code;
    private BigDecimal amount;
    private String refundStatus;
}
