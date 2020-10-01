package raiffeisen.sbp.sdk.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class SbpException extends Exception {

    private final String code;
    private final String message;

    @Override
    public String getMessage() {
        return code + ", " + message;
    }
}
