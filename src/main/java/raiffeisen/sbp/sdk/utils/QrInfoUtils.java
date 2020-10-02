package raiffeisen.sbp.sdk.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QrInfoUtils {
    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");

    public static String generateOrderNum() {
        return UUID.randomUUID().toString();
    }

    public static QRInfo verify(QRInfo qrInfo) {
        String createDate = checkCreateDate(qrInfo);

        String qrExpirationDate;

        if (qrInfo.getQrExpirationDate() != null && qrInfo.getQrExpirationDate().startsWith("+")) {
            qrExpirationDate = calculateQrExpirationDate(qrInfo, createDate);
        }
        else {
            qrExpirationDate = qrInfo.getQrExpirationDate();
        }

        return QRInfo.builder().
                createDate(createDate).
                order(qrInfo.getOrder() == null ? generateOrderNum() : qrInfo.getOrder()).
                qrType(qrInfo.getQrType()).
                sbpMerchantId(qrInfo.getSbpMerchantId()).
                account(qrInfo.getAccount()).
                additionalInfo(qrInfo.getAdditionalInfo()).
                amount(qrInfo.getAmount()).
                currency(qrInfo.getCurrency()).
                paymentDetails(qrInfo.getPaymentDetails()).
                qrExpirationDate(qrExpirationDate).build();
    }

    private static String checkCreateDate(QRInfo qrInfo) {
        if (qrInfo.getCreateDate() == null) {
            return ZonedDateTime.now().format(TIME_PATTERN);
        }
        else {
            return qrInfo.getCreateDate();
        }
    }

    private static String calculateQrExpirationDate(QRInfo qrInfo, String createDate) {
        String str = qrInfo.getQrExpirationDate().substring(1);

        if (Pattern.compile("[^\\dMdHms+]").matcher(str).find())
            throw new IllegalArgumentException("Invalid chars in QRInfo.qrExpirationDate");

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
                    throw new IllegalArgumentException("Bad input in QRInfo.qrExpirationDate");
            }
        }
        if (!matcher.replaceAll("").equals("")) {
            throw new IllegalArgumentException("Bad input in QRInfo.qrExpirationDate");
        }
        return time.format(TIME_PATTERN);
    }
}