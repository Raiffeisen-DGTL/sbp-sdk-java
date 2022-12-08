package raiffeisen.sbp.sdk.model.out;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderQr {
    private String id;
    private String additionalInfo;
    private String paymentDetails;

    public OrderQr(String id) {
        this.id = id;
    }
}
