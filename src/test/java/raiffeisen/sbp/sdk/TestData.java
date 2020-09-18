package raiffeisen.sbp.sdk;

import raiffeisen.sbp.sdk.model.Response;

public class TestData {

    public static final Response successRegisterQR = new Response(200,
            "{\"code\": \"SUCCESS\"," +
                    "\"qrId\": \"qrId\"," +
                    "\"payload\": \"payloadUrl\"," +
                    "\"qrUrl\": \"qrUrl\" }");

}
