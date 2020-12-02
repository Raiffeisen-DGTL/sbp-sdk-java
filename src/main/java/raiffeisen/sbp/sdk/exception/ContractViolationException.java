package raiffeisen.sbp.sdk.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContractViolationException extends Exception {

    private final int httpCode;
    private final String message;
}
