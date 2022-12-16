package raiffeisen.sbp.sdk.model.out;

import lombok.Data;

@Data
public class NFC {
    private final String qrId;
    private String account;
    private String redirectUrl;
}
