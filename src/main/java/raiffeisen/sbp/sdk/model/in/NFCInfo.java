package raiffeisen.sbp.sdk.model.in;

import lombok.Data;

@Data
public class NFCInfo {
    private String qrId;
    private String qrStatus;
    private String qrExpirationDate;
    private String payload;
    private String qrUrl;
    private String subscriptionId;
}
