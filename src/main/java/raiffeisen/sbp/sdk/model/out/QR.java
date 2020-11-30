package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class QR {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");

    @Setter(AccessLevel.NONE)
    String order;
    @Setter(AccessLevel.NONE)
    BigDecimal amount;
    @Setter(AccessLevel.NONE)
    QRType qrType;

    String account;
    String additionalInfo;
    String createDate;
    String paymentDetails;
    String qrExpirationDate;
    String sbpMerchantId;

    public void setCreateDate(ZonedDateTime time) {
        createDate = time.format(TIME_PATTERN);
    }

    public void setCreateDate(String time) {
        createDate = time;
    }

    public void setQrExpirationDate(ZonedDateTime time) {
        qrExpirationDate = time.format(TIME_PATTERN);
    }

    public void setQrExpirationDate(String time) {
        qrExpirationDate = time;
    }
}
