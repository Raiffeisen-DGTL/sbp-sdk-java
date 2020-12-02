package raiffeisen.sbp.sdk.model;

import lombok.Data;

@Data
public final class Response {
    final int code;
    final String body;
}
