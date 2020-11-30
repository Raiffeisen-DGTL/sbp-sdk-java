package raiffeisen.sbp.sdk.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import raiffeisen.sbp.sdk.model.out.QR;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QRUtils {
    public static String generateOrderNum() {
        return UUID.randomUUID().toString();
    }

    public static void verifyQR(QR qr) {
        if (qr.getCreateDate() == null) {
            qr.setCreateDate(ZonedDateTime.now());
        }
        verifyQrExpirationDate(qr);
    }

    private static void verifyQrExpirationDate(QR qr) {
        if (qr.getQrExpirationDate() != null && qr.getQrExpirationDate().startsWith("+")) {
            String str = qr.getQrExpirationDate().substring(1);

            if (Pattern.compile("[^\\dMdHms+]").matcher(str).find())
                throw new IllegalArgumentException("Invalid chars in QRInfo.qrExpirationDate");

            if (str.isEmpty())
                throw new IllegalArgumentException("Time shift is not specified");

            Pattern pattern = Pattern.compile("\\d+[MdHms]{1}");
            Matcher matcher = pattern.matcher(str);
            ZonedDateTime time = ZonedDateTime.parse(qr.getCreateDate());
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
            qr.setQrExpirationDate(time);
        }
    }
}
