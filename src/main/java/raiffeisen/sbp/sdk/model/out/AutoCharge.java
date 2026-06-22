package raiffeisen.sbp.sdk.model.out;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AutoCharge {
    private String frequency;
    private LocalDate firstChargeDate;
    private BigDecimal amount;
}
