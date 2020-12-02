package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class QR {

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");

    @Setter(AccessLevel.NONE)
    protected String order;
    @Setter(AccessLevel.NONE)
    protected BigDecimal amount;
    @Setter(AccessLevel.NONE)
    protected QRType qrType;

    protected String account;
    protected String additionalInfo;
    protected String createDate;
    protected String paymentDetails;
    protected String qrExpirationDate;

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

    protected void makeCopy(QR copy) {
        order = copy.order;
        amount = copy.amount;
        qrType = copy.qrType;

        account = copy.account;
        additionalInfo = copy.additionalInfo;
        createDate = copy.createDate;
        paymentDetails = copy.paymentDetails;
        qrExpirationDate = copy.qrExpirationDate;
    }
}
