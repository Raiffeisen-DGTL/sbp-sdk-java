package raiffeisen.sbp.sdk.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class PaymentNotification {
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
