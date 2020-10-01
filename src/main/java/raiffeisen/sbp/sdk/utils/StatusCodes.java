package raiffeisen.sbp.sdk.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusCodes {
    SUCCESS("SUCCESS"),
    COMPLETED("COMPLETED"),
    IN_PROGRESS("IN_PROGRESS");

    private final String message;
}
