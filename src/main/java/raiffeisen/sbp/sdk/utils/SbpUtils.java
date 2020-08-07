package raiffeisen.sbp.sdk.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import raiffeisen.sbp.sdk.exception.EncryptionException;
import raiffeisen.sbp.sdk.model.PaymentNotification;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SbpUtils {

    private static final String SHA_256_ALGORITHM = "HmacSHA256";
    private static final Charset ENCODING = StandardCharsets.UTF_8;
    private static final String SEPARATOR = "|";

    public static boolean checkNotificationSignature(String jsonBody, String headerSignature, String secretKey) throws EncryptionException {
        String hash = encrypt(joinFields(jsonBody),secretKey);
        return hash.equals(headerSignature);
    }

    public static boolean checkNotificationSignature(PaymentNotification notification, String headerSignature, String secretKey) throws EncryptionException {
        String data = joinFields(notification.getAmount().toString(),
                notification.getSbpMerchantId(),
                notification.getOrder(),
                notification.getPaymentStatus(),
                notification.getTransactionDate());
        String hash = encrypt(data, secretKey);
        return hash.equals(headerSignature);
    }

    public static boolean checkNotificationSignature(
            BigDecimal amount,
            String sbpMerchantId,
            String order,
            String paymentStatus,
            String transactionDate,
            String headerSignature,
            String secretKey) throws EncryptionException {

        String data = joinFields(amount.toString(), sbpMerchantId, order, paymentStatus, transactionDate);
        String hash = encrypt(data, secretKey);
        return hash.equals(headerSignature);
    }


    public static String encrypt(String data, String key) throws EncryptionException {
        if (data.isEmpty()) {
            return "";
        }
        try {
            SecretKeySpec secret = new SecretKeySpec(key.getBytes(ENCODING), SHA_256_ALGORITHM);
            Mac mac = Mac.getInstance(SHA_256_ALGORITHM);
            mac.init(secret);
            byte[] encoded = mac.doFinal(data.getBytes(ENCODING));
            return Hex.encodeHexString(encoded);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new EncryptionException(e);
        }

    }

    private static String joinFields(String jsonString) {
        try {
            JsonNode json = new ObjectMapper().readTree(jsonString);
            return joinFields(json.path("amount").asText(),
                    json.path("sbpMerchantId").asText(),
                    json.path("order").asText(),
                    json.path("paymentStatus").asText(),
                    json.path("transactionDate").asText());
        }
        catch (JsonProcessingException e) {
            return "";
        }
    }

    private static String joinFields(String amount, String sbpMerchantId, String order, String paymentStatus, String transactionDate) {
        return amount +
                SEPARATOR +
                sbpMerchantId +
                SEPARATOR +
                order +
                SEPARATOR +
                paymentStatus +
                SEPARATOR +
                transactionDate;
    }
}
