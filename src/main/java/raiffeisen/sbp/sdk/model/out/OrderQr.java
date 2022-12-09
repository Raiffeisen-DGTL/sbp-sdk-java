package raiffeisen.sbp.sdk.model.out;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class OrderQr {
    @NonNull
    private String id;
    private String additionalInfo;
    private String paymentDetails;

}
