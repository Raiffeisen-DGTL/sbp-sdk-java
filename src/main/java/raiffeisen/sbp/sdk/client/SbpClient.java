package raiffeisen.sbp.sdk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.in.OrderInfo;
import raiffeisen.sbp.sdk.model.in.PaymentInfo;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.in.RefundStatus;
import raiffeisen.sbp.sdk.model.out.Order;
import raiffeisen.sbp.sdk.model.out.QR;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.RefundId;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.web.ApacheClient;
import raiffeisen.sbp.sdk.web.WebClient;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SbpClient implements Closeable {
    public static final String TEST_URL = PropertiesLoader.TEST_URL;
    public static final String PRODUCTION_URL = PropertiesLoader.PRODUCTION_URL;

    private static final String REGISTER_PATH = PropertiesLoader.REGISTER_PATH;
    private static final String QR_INFO_PATH = PropertiesLoader.QR_INFO_PATH;
    private static final String PAYMENT_INFO_PATH = PropertiesLoader.PAYMENT_INFO_PATH;
    private static final String REFUND_PATH = PropertiesLoader.REFUND_PATH;
    private static final String REFUND_INFO_PATH = PropertiesLoader.REFUND_INFO_PATH;

    private static final String CREATE_ORDER_PATH = PropertiesLoader.CREATE_ORDER_PATH;

    private static final JsonMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    private final String domain;

    private final String secretKey;

    private final String sbpMerchantId;

    private final WebClient webClient;

    public SbpClient(String domain, String sbpMerchantId, String secretKey) {
        this(domain, sbpMerchantId, secretKey, new ApacheClient());
    }

    public SbpClient(String domain, String sbpMerchantId, String secretKey, WebClient customWebClient) {
        this.domain = domain;
        this.sbpMerchantId = sbpMerchantId;
        this.secretKey = secretKey;
        webClient = customWebClient;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    }

    public QRUrl registerQR(final QR customerQr) throws SbpException, ContractViolationException, IOException {
        final QR qr = customerQr.newInstance();
        qr.verify();
        ObjectNode jsonNode = mapper.valueToTree(qr);
        jsonNode.put("sbpMerchantId", sbpMerchantId);
        return post(domain + REGISTER_PATH, jsonNode.toString(), QRUrl.class);
    }

    public RefundStatus refundPayment(final RefundInfo refund) throws SbpException, ContractViolationException, IOException {
        return post(domain + REFUND_PATH, mapper.writeValueAsString(refund), secretKey, RefundStatus.class);
    }

    public QRUrl getQRInfo(final QRId qrId) throws SbpException, ContractViolationException, IOException {
        return get(domain + QR_INFO_PATH, qrId.getQrId(), secretKey, QRUrl.class);
    }

    public PaymentInfo getPaymentInfo(final QRId qrId) throws SbpException, ContractViolationException, IOException {
        return get(domain + PAYMENT_INFO_PATH, qrId.getQrId(), secretKey, PaymentInfo.class);
    }

    public RefundStatus getRefundInfo(final RefundId refundId) throws SbpException, ContractViolationException, IOException {
        return get(domain + REFUND_INFO_PATH, refundId.getRefundId(), secretKey, RefundStatus.class);
    }

    public OrderInfo createOrder(final Order order) throws SbpException, IOException, ContractViolationException {
        ObjectNode jsonNode = mapper.valueToTree(order);
        return post(domain + CREATE_ORDER_PATH, jsonNode.toString(), secretKey, OrderInfo.class);
    }

    private <T> T post(String url, String body, Class<T> resultClass)
            throws IOException, SbpException, ContractViolationException {
        Response response = webClient.request(WebClient.POST_METHOD, url, getHeaders(), body);
        return convert(response, resultClass);
    }

    private <T> T post(String url, String body, final String secretKey, Class<T> resultClass)
            throws IOException, SbpException, ContractViolationException {
        Response response = webClient.request(WebClient.POST_METHOD, url, prepareHeaders(secretKey), body);
        return convert(response, resultClass);
    }

    private <T> T get(String url, final String pathParameter, final String secretKey, Class<T> resultClass)
            throws IOException, SbpException, ContractViolationException {
        url = url.replace("?", pathParameter);
        Response response = webClient.request(WebClient.GET_METHOD, url, prepareHeaders(secretKey), null);
        return convert(response, resultClass);
    }


    private <T> T convert(Response response, Class<T> resultClass) throws SbpException, ContractViolationException {
        try {
            JsonNode codeNode = mapper.readTree(response.getBody()).get("code");
            int httpCode = response.getCode();
            if (httpCode == 200) {
                return successHandler(response, resultClass, codeNode);
            }
            errorHandler(response, codeNode);
            throw new ContractViolationException(response.getCode(), response.getBody());
        } catch (JsonProcessingException exception) {
            throw new ContractViolationException(response.getCode(), response.getBody());
        }
    }

    private <T> T successHandler(Response response, Class<T> resultClass, JsonNode codeNode) throws ContractViolationException, SbpException {
        try {
            T result = mapper.readValue(response.getBody(), resultClass);
            if (codeNode != null && !codeNode.textValue().contains("SUCCESS")) {
                errorHandler(response, codeNode);
            }
            return result;
        } catch (JsonProcessingException exception) {
            throw new ContractViolationException(response.getCode(), response.getBody());
        }
    }

    private void errorHandler(Response response, JsonNode codeNode) throws SbpException, ContractViolationException, JsonProcessingException {
        if (codeNode != null && codeNode.textValue().contains("ERROR.")) {
            String message = mapper.readTree(response.getBody()).get("message").textValue();
            throw new SbpException(codeNode.textValue(), message);
        }
        throw new ContractViolationException(response.getCode(), response.getBody());
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
