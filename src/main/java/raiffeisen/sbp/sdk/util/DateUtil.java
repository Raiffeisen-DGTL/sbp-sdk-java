package raiffeisen.sbp.sdk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static String checkDate(String createDate) {
        if (createDate == null) {
            return ZonedDateTime.now().toString();
        }
        return createDate;
    }

    public static String calculateExpirationDate(String qrExpirationDate, String createDate, String classAndFieldName) {
        if (qrExpirationDate != null && qrExpirationDate.startsWith("+")) {
            String str = qrExpirationDate.substring(1);

            if (Pattern.compile("[^\\dMdHms+]").matcher(str).find())
                throw new IllegalArgumentException("Invalid chars in " + classAndFieldName);

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
                        throw new IllegalArgumentException("Bad input in " + classAndFieldName);
                }
            }
            if (!matcher.replaceAll("").equals("")) {
                throw new IllegalArgumentException("Bad input in " + classAndFieldName);
            }
            return time.toString();
        }
        return qrExpirationDate;
    }
}
