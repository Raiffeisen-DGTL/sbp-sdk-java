package raiffeisen.sbp.sdk.model.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QRUrl {

    private String code;
    private String qrId;
    private String payload;
    private String qrUrl;
}
