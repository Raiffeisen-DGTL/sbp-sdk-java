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
@Builder (buildMethodName = "create", builderMethodName = "creator")
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
    private final String ExpirationDate;

}
