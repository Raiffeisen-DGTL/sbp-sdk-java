package raiffeisen.sbp.sdk.model.fiscal;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceiptInfo {
    private String receiptNumber;
    private ReceiptType receiptType;
    private String status;
    private ReceiptClient client;
    private ReceiptItem[] items;
    private ReceiptPayment[] payments;
    private BigDecimal total;
    private String ofdUrl;
}
