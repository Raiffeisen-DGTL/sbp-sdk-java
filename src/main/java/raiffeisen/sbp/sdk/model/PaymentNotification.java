package raiffeisen.sbp.sdk.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import raiffeisen.sbp.sdk.json.JsonParser;

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

    public static PaymentNotification fromJson(String body) throws JsonProcessingException {
        return JsonParser.objectFromJson(body, PaymentNotification.class);
    }
}
