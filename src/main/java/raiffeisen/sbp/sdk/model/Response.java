package raiffeisen.sbp.sdk.model;

import lombok.Data;

@Data
public final class Response {
    private final int code;
    private final String body;
}
