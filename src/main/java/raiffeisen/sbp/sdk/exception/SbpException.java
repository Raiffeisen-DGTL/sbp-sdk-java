package raiffeisen.sbp.sdk.exception;

import lombok.Getter;

@Getter
public class SbpException extends Exception {

    private final String message;

    public SbpException(String errorMessage) {
        message = errorMessage;
    }
}
