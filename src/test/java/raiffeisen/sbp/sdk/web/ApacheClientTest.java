package raiffeisen.sbp.sdk.web;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raiffeisen.sbp.sdk.data.TestData;
import raiffeisen.sbp.sdk.model.Response;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class ApacheClientTest {
    @Mock(name = "httpClient")
    CloseableHttpClient httpClientMock;

    @Mock
    CloseableHttpResponse httpResponse;

    @Mock
    StatusLine statusLine;

    @InjectMocks
    ApacheClient client;

    @Test
    void success_getRequest() throws Exception {
        ContentType content = ContentType.APPLICATION_JSON.withCharset("UTF-8");
        HttpEntity entity = EntityBuilder.create().setText(TestData.RESPONSE_BODY).setContentType(content).build();
        Mockito.when(httpResponse.getEntity()).thenReturn(entity);
        Mockito.when(statusLine.getStatusCode()).thenReturn(200);
        Mockito.when(httpResponse.getStatusLine()).thenReturn(statusLine);
        ArgumentCaptor<HttpGet> getCaptor = ArgumentCaptor.forClass(HttpGet.class);
        Mockito.when(httpClientMock.execute(getCaptor.capture())).thenReturn(httpResponse);

        Response response = client.request(WebClient.GET_METHOD, TestData.QR_INFO_PATH, TestData.MAP_HEADERS, null);

        assertEquals(200, response.getCode());
        assertEquals(TestData.RESPONSE_BODY, response.getBody());
        assertTrue(getCaptor.getValue().containsHeader("content-type"));
        assertTrue(getCaptor.getValue().containsHeader("charset"));
    }

    @Test
    void success_postRequest() throws Exception {
        ContentType content = ContentType.APPLICATION_JSON.withCharset("UTF-8");
        HttpEntity entity = EntityBuilder.create().setText(TestData.RESPONSE_BODY).setContentType(content).build();
        Mockito.when(httpResponse.getEntity()).thenReturn(entity);
        Mockito.when(statusLine.getStatusCode()).thenReturn(200);
        Mockito.when(httpResponse.getStatusLine()).thenReturn(statusLine);
        ArgumentCaptor<HttpPost> postCaptor = ArgumentCaptor.forClass(HttpPost.class);
        Mockito.when(httpClientMock.execute(postCaptor.capture())).thenReturn(httpResponse);

        Response response = client.request(WebClient.POST_METHOD, TestData.REGISTER_PATH, TestData.MAP_HEADERS, "NotNullEntity");

        assertEquals(200, response.getCode());
        assertEquals(TestData.RESPONSE_BODY, response.getBody());
        assertTrue(postCaptor.getValue().containsHeader("content-type"));
        assertTrue(postCaptor.getValue().containsHeader("charset"));
    }

    @Test
    void fail_noSuchHttpMethod() throws Exception {
        Response response = client.request("Invalid http method", TestData.QR_INFO_PATH, TestData.MAP_HEADERS, null);

        assertNull(response);
    }
}
