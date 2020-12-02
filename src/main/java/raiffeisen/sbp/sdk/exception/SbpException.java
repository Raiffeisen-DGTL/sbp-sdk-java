package raiffeisen.sbp.sdk.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SbpException extends Exception {
    private final String code;
    private final String message;
}
