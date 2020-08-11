package raiffeisen.sbp.sdk.model.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class PaymentInfo {

    private String additionalInfo;
    private BigDecimal amount;
    private String code;
    private String createDate;
    private String currency;
    private long merchantId;
    private String order;
    private String paymentStatus;
    private String qrId;
    private String sbpMerchantId;
    private String transactionDate;
    private long transactionId;
}
