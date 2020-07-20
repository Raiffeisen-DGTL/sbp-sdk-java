package raiffeisen.sbp.sdk.model;

import lombok.Builder;
import lombok.Value;
import lombok.Getter;

@Getter
@Value
@Builder (buildMethodName = "create", builderMethodName = "creator")
public class Response {
    private final int code;

    private final String body;
}
