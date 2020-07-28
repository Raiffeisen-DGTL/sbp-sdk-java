package raiffeisen.sbp.sdk.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SbpException extends Exception {

    private String code;
    private String message;

    @Override
    public String getMessage() {
        return code + ", " + message;
    }
}
