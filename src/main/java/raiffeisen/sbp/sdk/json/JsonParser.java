package raiffeisen.sbp.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {
    public static String getMessageFromJson(String body) throws JsonProcessingException {
        return new ObjectMapper().readTree(body).path("message").asText();
    }

    public static <T> T objectFromJson(String body, Class<T> tClass) throws JsonProcessingException {
        return new ObjectMapper().readValue(body, tClass);
    }

    public static <E extends Throwable, T> T getObjectOrThrow(String body, Class<T> resultClass, Class<E> throwClass) throws E, JsonProcessingException {
        try {
            return objectFromJson(body, resultClass);
        }
        catch (JsonProcessingException e) {
            throw objectFromJson(body, throwClass);
        }
    }
}
