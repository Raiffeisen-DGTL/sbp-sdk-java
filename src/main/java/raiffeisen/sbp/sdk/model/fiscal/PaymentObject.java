package raiffeisen.sbp.sdk.model.fiscal;

import lombok.Getter;

@Getter
public enum PaymentObject {
    COMMODITY("COMMODITY"),
    COMMODITY_MARKING_NO_CODE("COMMODITY_MARKING_NO_CODE"),
    COMMODITY_MARKING_WITH_CODE("COMMODITY_MARKING_WITH_CODE"),
    EXCISE("EXCISE"),
    EXCISE_MARKING_NO_CODE("EXCISE_MARKING_NO_CODE"),
    EXCISE_MARKING_WITH_CODE("EXCISE_MARKING_WITH_CODE"),
    JOB("JOB"),
    SERVICE("SERVICE"),
    PAYMENT("PAYMENT"),
    ANOTHER("ANOTHER");

    private String type;

    PaymentObject(String type) {
        this.type = type;
    }

}
