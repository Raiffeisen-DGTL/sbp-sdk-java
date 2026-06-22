package raiffeisen.sbp.sdk.model.fiscal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import raiffeisen.sbp.sdk.exception.ContractViolationException;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Receipt {
    protected String receiptNumber;
    protected ReceiptClient client;
    protected ReceiptItem[] items;
    protected ReceiptPayment[] payments;
    protected BigDecimal total;

    public void verify() throws ContractViolationException {
        try {
            if (receiptNumber == null) {
                throw new NullPointerException("receipt number is null");
            }
            client.verify();
            for (ReceiptItem receiptItem : items) {
                receiptItem.verify();
            }
            if (total == null) {
                throw new NullPointerException("total is null");
            }
        } catch (RuntimeException e) {
            throw new ContractViolationException(400, e.getMessage());
        }
    }
}
