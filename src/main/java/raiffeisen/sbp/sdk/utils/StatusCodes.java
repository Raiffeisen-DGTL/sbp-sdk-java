package raiffeisen.sbp.sdk.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusCodes {
    SUCCESS("SUCCESS"),
    COMPLETED("COMPLETED"),
    IN_PROGRESS("IN_PROGRESS");

    private final String message;
}
