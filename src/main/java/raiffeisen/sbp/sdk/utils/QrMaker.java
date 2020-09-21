package raiffeisen.sbp.sdk.utils;


import raiffeisen.sbp.sdk.model.out.QRInfo;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QrMaker {
    private QrMaker() {
        throw new IllegalStateException("Utility class");
    }

    public static QRInfo verifyQr(QRInfo qrInfo) throws IOException {
        String createDate;
        String qrExpirationDate;
        ZonedDateTime time;

        if (qrInfo.getCreateDate() == null) {
            time = ZonedDateTime.now();
            createDate = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSXXX"));
        }
        else {
            createDate = qrInfo.getCreateDate();
            time = ZonedDateTime.parse(createDate);
        }

        if (qrInfo.getQrExpirationDate().startsWith("+")) {
            String str = qrInfo.getQrExpirationDate();

            if (Pattern.compile("[^\\dMdHms+]").matcher(str).find())
                throw new IOException("Invalid chars in QRInfo");

            Pattern pattern = Pattern.compile("\\d+[MdHms]");
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
                        throw new IOException("Bad input in QRInfo");
                }
            }
            qrExpirationDate = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSXXX"));
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
}