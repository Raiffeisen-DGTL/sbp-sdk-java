package raiffeisen.sbp.sdk.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class PaymentNotification {
    private long transactionId;
    private String qrId;
    private String sbpMerchantId;
    private long merchantId;
    private BigDecimal amount;
    private String currency;
    private String transactionDate;
    private String paymentStatus;
    private String additionalInfo;
    private String order;
    private String createDate;
}
