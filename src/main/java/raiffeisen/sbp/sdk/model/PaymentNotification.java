package raiffeisen.sbp.sdk.model;

import lombok.Getter;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Setter;
import raiffeisen.sbp.sdk.json.JsonParser;

import java.math.BigDecimal;

@Getter
@Setter
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
