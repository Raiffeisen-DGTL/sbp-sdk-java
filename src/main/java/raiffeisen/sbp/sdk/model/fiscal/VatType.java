package raiffeisen.sbp.sdk.model.fiscal;

import lombok.Getter;

@Getter
public enum VatType {
    NONE("NONE"),
    VAT0("VAT0"),
    VAT10("VAT10"),
    VAT110("VAT110"),
    VAT20("VAT20"),
    VAT120("VAT120");
    private final String type;

    VatType(String type) {
        this.type = type;
    }

}
