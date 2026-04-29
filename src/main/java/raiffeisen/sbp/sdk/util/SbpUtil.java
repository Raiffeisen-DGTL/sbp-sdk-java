/*
 * Copyright 2020-present AO Raiffeisenbank
 *
 * This file is part of SBP SDK Java.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package raiffeisen.sbp.sdk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import raiffeisen.sbp.sdk.model.PaymentNotification;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SbpUtil {

    private static final String SHA_256_ALGORITHM = "HmacSHA256";
    private static final Charset ENCODING = StandardCharsets.UTF_8;
    private static final String SEPARATOR = "|";

    private static final JsonMapper mapper = new JsonMapper();

    public static PaymentNotification parseNotification(String json) throws JsonProcessingException {
        return mapper.readValue(json, PaymentNotification.class);
    }

    public static boolean checkNotificationSignature(String jsonBody, String headerSignature, String secretKey) {
        String hash = encrypt(joinFields(jsonBody), secretKey);
        return hash.equals(headerSignature);
    }

    public static boolean checkNotificationSignature(PaymentNotification notification, String headerSignature, String secretKey) {
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
            String secretKey) {

        String data = joinFields(amount.toString(), sbpMerchantId, order, paymentStatus, transactionDate);
        String hash = encrypt(data, secretKey);
        return hash.equals(headerSignature);
    }

    @SneakyThrows
    private static String encrypt(String data, String key) {
        if (data.isEmpty()) {
            return "";
        }
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(ENCODING), SHA_256_ALGORITHM);
        Mac mac = Mac.getInstance(SHA_256_ALGORITHM);
        mac.init(secret);
        byte[] encoded = mac.doFinal(data.getBytes(ENCODING));
        return Hex.encodeHexString(encoded);
    }

    private static String joinFields(String jsonString) {
        try {
            JsonNode json = new ObjectMapper().readTree(jsonString);
            return joinFields(json.path("amount").asText(),
                    json.path("sbpMerchantId").asText(),
                    json.path("order").asText(),
                    json.path("paymentStatus").asText(),
                    json.path("transactionDate").asText());
        } catch (JsonProcessingException e) {
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
