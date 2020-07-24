package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.Getter;
import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QRInfo {

    @JsonProperty
    private final String createDate;
    @JsonProperty
    private final String order;
    @JsonProperty
    private final QRType qrType;
    @JsonProperty
    private final String sbpMerchantId;

    @JsonProperty
    private final String account;
    @JsonProperty
    private final String additionalInfo;
    @JsonProperty
    private final BigDecimal amount;
    @JsonProperty
    private final String currency;
    @JsonProperty
    private final String paymentDetails;
    @JsonProperty
    private final String qrExpirationDate;

    @Builder (buildMethodName = "create", builderMethodName = "creator")
    public QRInfo(String createDate, String order, QRType qrType, String sbpMerchantId,
                  String account, String additionalInfo, BigDecimal amount, String currency,
                  String paymentDetails, String qrExpirationDate) {
        if (createDate == null) {
            this.createDate = getCurrentDate();
        }
        else {
            this.createDate = createDate;
        }
        this.order = order;
        this.qrType = qrType;
        this.sbpMerchantId = sbpMerchantId;
        this.account = account;
        this.additionalInfo = additionalInfo;
        this.amount = amount;
        this.currency = currency;
        this.paymentDetails = paymentDetails;
        this.qrExpirationDate = qrExpirationDate;

    }

    private String getCurrentDate() {
        String timestamp = ZonedDateTime.now(ZoneId.of("Europe/Moscow")).toString();
        return timestamp.substring(0,timestamp.indexOf("["));
    }

}
