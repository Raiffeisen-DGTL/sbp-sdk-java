package raiffeisen.sbp.sdk.model.in;

import lombok.Data;

@Data
public final class QRUrl {
    String qrId;
    String payload;
    String qrUrl;
}
