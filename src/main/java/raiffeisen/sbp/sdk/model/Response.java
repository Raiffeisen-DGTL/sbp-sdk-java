package raiffeisen.sbp.sdk.model;

import lombok.Getter;

@Getter
public class Response {
    private final int code;
    private final String body;

    public Response(int _code, String _body) {
        this.code = _code;
        this.body = _body;
    }
}
