package raiffeisen.sbp.sdk.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class PaymentNotification {
    long transactionId;
    String qrId;
    String sbpMerchantId;
    long merchantId;
    BigDecimal amount;
    String currency;
    String transactionDate;
    String paymentStatus;
    String additionalInfo;
    String order;
    String createDate;
}
