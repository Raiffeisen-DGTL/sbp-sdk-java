package raiffeisen.sbp.sdk.model.in;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public final class PaymentInfo {
    private String additionalInfo;
    private BigDecimal amount;
    private ZonedDateTime createDate;
    private String currency;
    private long merchantId;
    private String order;
    private String paymentStatus;
    private String qrId;
    private String sbpMerchantId;
    private ZonedDateTime transactionDate;
    private long transactionId;
}
