package raiffeisen.sbp.sdk.model.out;

import lombok.Data;

@Data
public class OrderExtra {
    private final String apiClient = "sbp-sdk-java";
    private final String apiClientVersion = "1.0.5";
}
