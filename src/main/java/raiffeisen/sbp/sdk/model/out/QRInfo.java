package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.Getter;
import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;

@Getter
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QRInfo {

    String createDate;
    String order;
    QRType qrType;
    String sbpMerchantId;

    String account;
    String additionalInfo;
    BigDecimal amount;
    String currency;
    String paymentDetails;
    String qrExpirationDate;
}
