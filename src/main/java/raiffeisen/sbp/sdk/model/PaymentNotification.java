package raiffeisen.sbp.sdk.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import raiffeisen.sbp.sdk.exception.EncryptionException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PaymentNotification {
    private final long transactionId;
    private final String qrId;
    private final String sbpMerchantId;
    private final long merchantId;
    private final BigDecimal amount;
    private final String currency;
    private final String transactionDate;
    private final String paymentStatus;
    private final String additionalInfo;
    private final String order;
    private final String createDate;

    private static final String SHA_256_ALGORITHM = "HmacSHA256";
    private static final Charset ENCODING = StandardCharsets.UTF_8;

    public PaymentNotification(String body, String headerSignature, String secretKey) throws JsonProcessingException {
        if (checkNotificationSignature(body, headerSignature, secretKey) == false) {
            throw new EncryptionException("Signatures are not equal.");
        }
        JsonNode json = new ObjectMapper().readTree(body);
        transactionId = Long.parseLong(json.path("transactionId").asText());
        qrId = json.path("qrId").asText();
        sbpMerchantId = json.path("sbpMerchantId").asText();
        merchantId = Long.parseLong(json.path("merchantId").asText());
        amount = new BigDecimal(json.path("amount").asText());
        currency = json.path("currency").asText();
        transactionDate = json.path("transactionDate").asText();
        paymentStatus = json.path("paymentStatus").asText();
        additionalInfo = json.path("additionalInfo").asText();
        order = json.path("order").asText();
        createDate = json.path("createDate").asText();
    }

    public static boolean checkNotificationSignature(String body, String headerSignature, String secretKey) {
        String hash = encrypt(joinFields(body),secretKey);
        return hash.equals(headerSignature);
    }

    public static String encrypt(String fields, String secretKey) {
        if (fields.isEmpty()) {
            return "";
        }

        try {
            SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(ENCODING), SHA_256_ALGORITHM);
            Mac mac = Mac.getInstance(SHA_256_ALGORITHM);
            mac.init(secret);
            byte[] encoded = mac.doFinal(fields.getBytes(ENCODING));
            return Hex.encodeHexString(encoded);
        }
        catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new EncryptionException(e);
        }

    }

    public static String joinFields(String body) {
        try {
            JsonNode json = new ObjectMapper().readTree(body);
            String fields = "";
            fields += json.path("amount").asText();
            fields += "|";
            fields += json.path("sbpMerchantId").asText();
            fields += "|";
            fields += json.path("order").asText();
            fields += "|";
            fields += json.path("paymentStatus").asText();
            fields += "|";
            fields += json.path("transactionDate").asText();
            return fields;
        }
        catch (JsonProcessingException e) {
            return "";
        }
    }
}
