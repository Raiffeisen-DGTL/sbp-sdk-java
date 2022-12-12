package raiffeisen.sbp.sdk.web;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raiffeisen.sbp.sdk.client.PropertiesLoader;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.model.Response;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class SdkHttpClientTest {
    @Mock(name = "httpClient")
    HttpClient httpClientMock;

    @Mock
    HttpResponse<String> httpResponse;

    @InjectMocks
    SdkHttpClient client;

    @Test
    void success_getRequest() throws Exception {
        Mockito.when(httpResponse.body()).thenReturn(TestData.RESPONSE_BODY);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        ArgumentCaptor<HttpRequest> getCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        Mockito.when(httpClientMock.send(getCaptor.capture(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);

        Response response = client.getRequest(PropertiesLoader.TEST_URL + TestData.QR_INFO_PATH, TestData.MAP_HEADERS);

        assertEquals(200, response.getCode());
        assertEquals(TestData.RESPONSE_BODY, response.getBody());
        assertTrue(getCaptor.getValue().headers().toString().contains("content-type"));
        assertTrue(getCaptor.getValue().headers().toString().contains("charset"));
    }

    @Test
    void success_postRequest() throws Exception {
        String entity = TestData.RESPONSE_BODY;
        Mockito.when(httpResponse.body()).thenReturn(entity);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        ArgumentCaptor<HttpRequest> postCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        Mockito.when(httpClientMock.send(postCaptor.capture(), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(httpResponse);

        Response response = client.postRequest(PropertiesLoader.TEST_URL + TestData.REGISTER_PATH, TestData.MAP_HEADERS, "NotNullEntity");

        assertEquals(200, response.getCode());
        assertEquals(TestData.RESPONSE_BODY, response.getBody());
        assertTrue(postCaptor.getValue().headers().toString().contains("content-type"));
        assertTrue(postCaptor.getValue().headers().toString().contains("charset"));
    }
}
