package raiffeisen.sbp.sdk.model.fiscal;

import lombok.Getter;

@Getter
public enum MeasurementUnit {
    PIECE("штука"),
    KILOGRAM("кг"),
    DAY("день");

    private final String type;

    MeasurementUnit(String type) {
        this.type = type;
    }

}
