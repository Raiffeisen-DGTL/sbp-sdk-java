package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Value;
import lombok.Getter;

@Getter
@Value
@Builder (buildMethodName = "create", builderMethodName = "creator")
public class QRId {
    @JsonProperty
    String qrId;
}
