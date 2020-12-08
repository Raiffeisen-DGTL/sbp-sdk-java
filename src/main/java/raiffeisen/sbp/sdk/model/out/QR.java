package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class QR {

    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");

    @Setter(AccessLevel.NONE)
    protected String order;
    @Setter(AccessLevel.NONE)
    protected BigDecimal amount;
    @Setter(AccessLevel.PROTECTED)
    protected QRType qrType;

    protected String account;
    protected String additionalInfo;
    protected String createDate;
    protected String paymentDetails;
    protected String qrExpirationDate;

    public abstract QR newInstance();

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

    public void verify() {
        checkDate();
        calculateExpirationDate();
    }

    private void checkDate() {
        if (createDate == null) {
            setCreateDate(ZonedDateTime.now());
        }
    }

    private void calculateExpirationDate() {
        if (qrExpirationDate != null && qrExpirationDate.startsWith("+")) {
            String str = qrExpirationDate.substring(1);

            if (Pattern.compile("[^\\dMdHms+]").matcher(str).find())
                throw new IllegalArgumentException("Invalid chars in QR.qrExpirationDate");

            if (str.isEmpty())
                throw new IllegalArgumentException("Time shift is not specified");

            Pattern pattern = Pattern.compile("\\d+[MdHms]{1}");
            Matcher matcher = pattern.matcher(str);
            ZonedDateTime time = ZonedDateTime.parse(createDate);
            while (matcher.find()) {
                String number = str.substring(matcher.start(), matcher.end() - 1);
                switch (str.charAt(matcher.end() - 1)) {
                    case ('M'):
                        time = time.plusMonths(Integer.parseInt(number));
                        break;
                    case ('d'):
                        time = time.plusDays(Integer.parseInt(number));
                        break;
                    case ('H'):
                        time = time.plusHours(Integer.parseInt(number));
                        break;
                    case ('m'):
                        time = time.plusMinutes(Integer.parseInt(number));
                        break;
                    case ('s'):
                        time = time.plusSeconds(Integer.parseInt(number));
                        break;
                    default:
                        throw new IllegalArgumentException("Bad input in QR.qrExpirationDate");
                }
            }
            if (!matcher.replaceAll("").equals("")) {
                throw new IllegalArgumentException("Bad input in QR.qrExpirationDate");
            }
            setQrExpirationDate(time);
        }
    }
}
