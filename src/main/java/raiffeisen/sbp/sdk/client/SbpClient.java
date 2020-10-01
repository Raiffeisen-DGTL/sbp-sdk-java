package raiffeisen.sbp.sdk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.QRInfo;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.utils.QrInfoUtils;
import raiffeisen.sbp.sdk.web.ApacheClient;
import raiffeisen.sbp.sdk.web.WebClient;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.HashMap;
import java.util.Map;

public class SbpClient implements Closeable {
    public static final String TEST_DOMAIN;
    public static final String PRODUCTION_DOMAIN;

    private static final String REGISTER_PATH;
    private static final String QR_INFO_PATH;
    private static final String PAYMENT_INFO_PATH;
    private static final String REFUND_PATH;
    private static final String REFUND_INFO_PATH;

    private static final JsonMapper mapper = new JsonMapper();

    static {
        Properties properties = new Properties();
        try {
            InputStream propertiesFile = ClassLoader.getSystemResourceAsStream("config.properties");
            properties.load(propertiesFile);
        } catch (NullPointerException e) {
            // TODO: do logging here
        } catch (IOException e) {
            // TODO: do logging here
        }

        TEST_DOMAIN = properties.getProperty("domain.sandbox", "https://test.ecom.raiffeisen.ru");
        PRODUCTION_DOMAIN = properties.getProperty("domain.production", "https://e-commerce.raiffeisen.ru");

        REGISTER_PATH = properties.getProperty("path.register.qr", "/api/sbp/v1/qr/register");
        QR_INFO_PATH = properties.getProperty("path.qr.info", "/api/sbp/v1/qr/?/info");
        PAYMENT_INFO_PATH = properties.getProperty("path.payment.info", "/api/sbp/v1/qr/?/payment-info");
        REFUND_PATH = properties.getProperty("path.refund", "/api/sbp/v1/refund");
        REFUND_INFO_PATH = properties.getProperty("path.refund.info", "/api/sbp/v1/refund/?");
    }

    private final String domain;

    private final String secretKey;

    private WebClient webClient;

    public SbpClient(String domain, String secretKey) {
        this(domain, secretKey, new ApacheClient());
    }

    public SbpClient(String domain, String secretKey, WebClient customWebClient) {
        this.domain = domain;
        this.secretKey = secretKey;
        webClient = customWebClient;
    }

    public void setWebClient(WebClient client) {
        webClient = client;
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public QRUrl registerQR(final QRInfo qr) throws SbpException, IOException {
        QRInfo verifiedQr = QrInfoUtils.verify(qr);
        return post(domain + REGISTER_PATH, mapper.writeValueAsString(verifiedQr), QRUrl.class);
    }

    public RefundStatus refundPayment(final RefundInfo refund) throws SbpException, IOException {
        return post(domain + REFUND_PATH, mapper.writeValueAsString(refund), secretKey, RefundStatus.class);
    }

    public QRUrl getQRInfo(final QRId qrId) throws SbpException, IOException {
        return get(domain + QR_INFO_PATH, qrId.getQrId(), secretKey, QRUrl.class);
    }

    public PaymentInfo getPaymentInfo(final QRId qrId) throws SbpException, IOException {
        return get(domain + PAYMENT_INFO_PATH, qrId.getQrId(), secretKey, PaymentInfo.class);
    }

    public RefundStatus getRefundInfo(final String refundId) throws SbpException, IOException {
        return get(domain + REFUND_INFO_PATH, refundId, secretKey, RefundStatus.class);
    }

    private <T> T post(String url, String body, Class<T> resultClass) throws SbpException, IOException {
        Response response = webClient.request(WebClient.POST_METHOD, url, getHeaders(), body);
        return convert(response, resultClass);
    }

    private <T> T post(String url, String body, final String secretKey, Class<T> resultClass) throws SbpException, IOException {
        Response response = webClient.request(WebClient.POST_METHOD, url, prepareHeaders(secretKey), body);
        return convert(response, resultClass);
    }

    private <T> T get(String url, final String pathParameter, final String secretKey, Class<T> resultClass) throws SbpException, IOException {
        url = url.replace("?", pathParameter);
        Response response = webClient.request(WebClient.GET_METHOD, url, prepareHeaders(secretKey), null);
        return convert(response, resultClass);
    }

    private <T> T convert(Response response, Class<T> resultClass) throws SbpException {
        try {
            return mapper.readValue(response.getBody(), resultClass);
        }
        catch (JsonProcessingException e) {
            throw new SbpException(String.valueOf(response.getCode()), response.getBody());
        }
    }

    private Map<String, String> prepareHeaders(String secretKey) {
        Map<String, String> headers = getHeaders();
        headers.put("Authorization", "Bearer " + secretKey);
        return headers;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("charset", "UTF-8");
        return headers;
    }

    @Override
    public void close() throws IOException {
        webClient.close();
    }
}
