package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Value
@Builder (buildMethodName = "create", builderMethodName = "creator")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundInfo {

    @JsonProperty
    BigDecimal amount;

    @JsonProperty
    String order;

    @JsonProperty
    String refundId;

    @JsonProperty
    long transactionId;
}
