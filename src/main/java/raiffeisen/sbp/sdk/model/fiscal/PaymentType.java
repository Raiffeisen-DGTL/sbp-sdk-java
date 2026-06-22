package raiffeisen.sbp.sdk.model.fiscal;

import lombok.Getter;

@Getter
public enum PaymentType {
    E_PAYMENT("E_PAYMENT"),
    PREPAID("PREPAID");
    private final String type;

    PaymentType(String type) {
        this.type = type;
    }

}
