package raiffeisen.sbp.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T objectFromJson(String body, Class<T> tClass) throws JsonProcessingException {
        return mapper.readValue(body, tClass);
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
