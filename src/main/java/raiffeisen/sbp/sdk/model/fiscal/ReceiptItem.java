package raiffeisen.sbp.sdk.model.fiscal;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiptItem {
    private String name;
    private BigDecimal price;
    private BigDecimal quantity;
    private BigDecimal amount;
    private PaymentObject paymentObject;
    private PaymentMode paymentMode;
    private MeasurementUnit measurementUnit;
    private String nomenclatureCode;
    private VatType vatType;

    public void verify() {
        if (name == null || name.isBlank()) {
            throw new RuntimeException("receipt item error_1");
        }
        if (price == null) {
            throw new RuntimeException("receipt item error_2");
        }
        if (quantity == null) {
            throw new RuntimeException("receipt item error_3");
        }
        if (amount == null) {
            throw new RuntimeException("receipt item error_4");
        }
        if (vatType == null) {
            throw new RuntimeException("receipt item error_5");
        }
    }
}
