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
import raiffeisen.sbp.sdk.model.out.RefundId;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.utils.QrInfoUtils;
import raiffeisen.sbp.sdk.web.ApacheClient;
import raiffeisen.sbp.sdk.web.WebClient;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SbpClient implements Closeable {
    public static final String TEST_DOMAIN = PropertiesLoader.TEST_DOMAIN;
    public static final String PRODUCTION_DOMAIN = PropertiesLoader.PRODUCTION_DOMAIN;

    private static final String REGISTER_PATH = PropertiesLoader.REGISTER_PATH;
    private static final String QR_INFO_PATH = PropertiesLoader.QR_INFO_PATH;
    private static final String PAYMENT_INFO_PATH = PropertiesLoader.PAYMENT_INFO_PATH;
    private static final String REFUND_PATH = PropertiesLoader.REFUND_PATH;
    private static final String REFUND_INFO_PATH = PropertiesLoader.REFUND_INFO_PATH;

    private static final JsonMapper mapper = new JsonMapper();

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

    public RefundStatus getRefundInfo(final RefundId refundId) throws SbpException, IOException {
        return get(domain + REFUND_INFO_PATH, refundId.getRefundId(), secretKey, RefundStatus.class);
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
