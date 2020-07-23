package raiffeisen.sbp.sdk.model;

import lombok.Getter;

@Getter
public class Response {
    private final int сode;
    private final String body;

    public Response(int _code, String _body) {
        this.сode = _code;
        this.body = _body;
    }
}
