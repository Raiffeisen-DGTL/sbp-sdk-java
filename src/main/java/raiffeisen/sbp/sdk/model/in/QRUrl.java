package raiffeisen.sbp.sdk.model.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Getter
public class QRUrl {

    private final String code;
    private final String qrId;
    private final String payload;
    private final String qrUrl;

    public QRUrl(String body) throws JsonProcessingException {
        JsonNode json = new ObjectMapper().readTree(body);
        code = json.path("code").asText();
        qrId = json.path("qrId").asText();
        payload = json.path("payload").asText();
        qrUrl = json.path("qrUrl").asText();
    }
}
