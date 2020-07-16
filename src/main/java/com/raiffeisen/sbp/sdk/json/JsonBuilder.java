package com.raiffeisen.sbp.sdk.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonBuilder {

    public static String fromObject(Object obj) throws JsonProcessingException {
        return new JsonMapper().writeValueAsString(obj);
    }

}
