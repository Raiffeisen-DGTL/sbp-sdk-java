package raiffeisen.sbp.sdk.utils;

import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QrInfoUtils {
    private QrInfoUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static String createDate;
    private static String qrExpirationDate;
    private static ZonedDateTime time;

    public static QRInfo calculateDate(QRInfo qrInfo) {
        checkCreateDate(qrInfo);

        if (qrInfo.getQrExpirationDate() != null && qrInfo.getQrExpirationDate().startsWith("+")) {
            calculateQrExpirationDate(qrInfo);
        }
        else {
            qrExpirationDate = qrInfo.getQrExpirationDate();
        }

        return QRInfo.creator().
                createDate(createDate).
                order(qrInfo.getOrder()).
                qrType(qrInfo.getQrType()).
                sbpMerchantId(qrInfo.getSbpMerchantId()).
                account(qrInfo.getAccount()).
                additionalInfo(qrInfo.getAdditionalInfo()).
                amount(qrInfo.getAmount()).
                currency(qrInfo.getCurrency()).
                paymentDetails(qrInfo.getPaymentDetails()).
                qrExpirationDate(qrExpirationDate).create();
    }

    private static void checkCreateDate(QRInfo qrInfo) {
        if (qrInfo.getCreateDate() == null) {
            time = ZonedDateTime.now();
            createDate = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"));
        }
        else {
            createDate = qrInfo.getCreateDate();
            time = ZonedDateTime.parse(createDate);
        }
    }

    private static void calculateQrExpirationDate(QRInfo qrInfo) {
        String str = qrInfo.getQrExpirationDate().substring(1);

        if (Pattern.compile("[^\\dMdHms+]").matcher(str).find())
            throw new IllegalArgumentException("Invalid chars in QRInfo.qrExpirationDate");

        if (str.isEmpty())
            throw new IllegalArgumentException("Time shift is not specified");

        Pattern pattern = Pattern.compile("\\d+[MdHms]{1}");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String number = str.substring(matcher.start(), matcher.end() - 1);
            switch (str.charAt(matcher.end() - 1)) {
                case('M'):
                    time = time.plusMonths(Integer.parseInt(number));
                    break;
                case('d'):
                    time = time.plusDays(Integer.parseInt(number));
                    break;
                case('H'):
                    time = time.plusHours(Integer.parseInt(number));
                    break;
                case('m'):
                    time = time.plusMinutes(Integer.parseInt(number));
                    break;
                case('s'):
                    time = time.plusSeconds(Integer.parseInt(number));
                    break;
                default:
                    throw new IllegalArgumentException("Bad input in QRInfo.qrExpirationDate");
            }
        }
        if (!matcher.replaceAll("").equals("")) {
            throw new IllegalArgumentException("Bad input in QRInfo.qrExpirationDate");
        }
        qrExpirationDate = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX"));
    }
}