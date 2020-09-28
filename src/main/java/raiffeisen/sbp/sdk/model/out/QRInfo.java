package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.Getter;
import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;

@Getter
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder (buildMethodName = "create", builderMethodName = "creator")
public class QRInfo {

    @JsonProperty
    String createDate;
    @JsonProperty
    String order;
    @JsonProperty
    QRType qrType;
    @JsonProperty
    String sbpMerchantId;

    @JsonProperty
    String account;
    @JsonProperty
    String additionalInfo;
    @JsonProperty
    BigDecimal amount;
    @JsonProperty
    String currency;
    @JsonProperty
    String paymentDetails;
    @JsonProperty
    String qrExpirationDate;
}
