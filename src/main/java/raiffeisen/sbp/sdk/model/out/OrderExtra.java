package raiffeisen.sbp.sdk.model.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderExtra {
    private String apiClient;
    private String apiClientVersion;
}
