package raiffeisen.sbp.sdk.model.in;

import lombok.Data;

@Data
public final class QRUrl {
    private String qrId;
    private String payload;
    private String qrUrl;
}
