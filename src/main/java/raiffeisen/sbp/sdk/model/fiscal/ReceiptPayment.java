package raiffeisen.sbp.sdk.model.fiscal;

import lombok.AllArgsConstructor;
import lombok.Data;
import raiffeisen.sbp.sdk.exception.ContractViolationException;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ReceiptPayment {
    private PaymentType paymentType;
    private BigDecimal amount;

    public void veify() throws ContractViolationException {
        if (paymentType == null) {
            throw new RuntimeException("receipt payment error_1");
        }
        if (amount == null) {
            throw new RuntimeException("receipt payment error_2");
        }
    }
}
