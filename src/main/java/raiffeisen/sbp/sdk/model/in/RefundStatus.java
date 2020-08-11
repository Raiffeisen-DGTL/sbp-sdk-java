package raiffeisen.sbp.sdk.model.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class RefundStatus {

    private String code;
    private BigDecimal amount;
    private String refundStatus;
}
