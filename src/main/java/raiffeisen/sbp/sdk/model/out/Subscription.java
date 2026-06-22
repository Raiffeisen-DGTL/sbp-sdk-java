package raiffeisen.sbp.sdk.model.out;

import lombok.Data;
import lombok.NonNull;

@Data
public class Subscription {
    private String id;
    @NonNull
    private String subscriptionPurpose;
    private AutoCharge autoCharge;
    private Extra extra;
}
