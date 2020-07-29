package raiffeisen.sbp.sdk.model.in;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRUrl {

    private String code;
    private String qrId;
    private String payload;
    private String qrUrl;
}
