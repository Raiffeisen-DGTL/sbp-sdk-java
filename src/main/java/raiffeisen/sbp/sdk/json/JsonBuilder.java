package raiffeisen.sbp.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonBuilder {

    private static final JsonMapper mapper = new JsonMapper();

    public static String fromObject(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

}
