package raiffeisen.sbp.sdk.model;

import lombok.Getter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;
import raiffeisen.sbp.sdk.exception.EncryptionException;
import raiffeisen.sbp.sdk.json.JsonParser;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Getter
@Setter
public class PaymentNotification {
    private long transactionId;
    private String qrId;
    private String sbpMerchantId;
    private long merchantId;
    private BigDecimal amount;
    private String currency;
    private String transactionDate;
    private String paymentStatus;
    private String additionalInfo;
    private String order;
    private String createDate;

    public static PaymentNotification fromJson(String body) throws JsonProcessingException {
        return JsonParser.objectFromJson(body, PaymentNotification.class);
    }
}
