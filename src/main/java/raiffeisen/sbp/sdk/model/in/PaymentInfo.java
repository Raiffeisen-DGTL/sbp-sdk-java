package raiffeisen.sbp.sdk.model.in;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public final class PaymentInfo {
    String additionalInfo;
    BigDecimal amount;
    ZonedDateTime createDate;
    String currency;
    long merchantId;
    String order;
    String paymentStatus;
    String qrId;
    String sbpMerchantId;
    ZonedDateTime transactionDate;
    long transactionId;
}
