package raiffeisen.sbp.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    public static String getMessageFromJson(String body) throws JsonProcessingException {
        return new ObjectMapper().readTree(body).path("message").asText();
    }
}
