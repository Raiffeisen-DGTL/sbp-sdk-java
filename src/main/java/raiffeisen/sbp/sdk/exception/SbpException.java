package raiffeisen.sbp.sdk.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SbpException extends Exception {

    private String code;
    private String message;

    @Override
    public String getMessage() {
        return code + ", " + message;
    }
}
