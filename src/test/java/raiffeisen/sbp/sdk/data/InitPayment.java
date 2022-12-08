package raiffeisen.sbp.sdk.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class InitPayment {
    private String qrId;
    private BigDecimal amount;
}
