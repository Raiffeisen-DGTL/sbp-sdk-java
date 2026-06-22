package raiffeisen.sbp.sdk.model.fiscal;

import lombok.Getter;

@Getter
public enum PaymentMode {
    FULL_PREPAYMENT("FULL_PREPAYMENT"),
    FULL_PAYMENT("FULL_PAYMENT"),
    ADVANCE("ADVANCE"),
    PREPAYMENT("PREPAYMENT");

    private final String type;

    PaymentMode(String type) {
        this.type = type;
    }

}
