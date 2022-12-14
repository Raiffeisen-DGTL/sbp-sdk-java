package raiffeisen.sbp.sdk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
import raiffeisen.sbp.sdk.model.out.OrderId;
import raiffeisen.sbp.sdk.model.out.QR;
import raiffeisen.sbp.sdk.model.out.QRId;
import raiffeisen.sbp.sdk.model.out.RefundId;
import raiffeisen.sbp.sdk.model.out.RefundInfo;
import raiffeisen.sbp.sdk.util.StringUtil;
import raiffeisen.sbp.sdk.web.SdkHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SbpClient {
    public static final String TEST_URL = PropertiesLoader.TEST_URL;
    public static final String PRODUCTION_URL = PropertiesLoader.PRODUCTION_URL;

    private static final String REGISTER_PATH = PropertiesLoader.REGISTER_PATH;
    private static final String QR_INFO_PATH = PropertiesLoader.QR_INFO_PATH;
    private static final String PAYMENT_INFO_PATH = PropertiesLoader.PAYMENT_INFO_PATH;
    private static final String REFUND_PATH = PropertiesLoader.REFUND_PATH;
    private static final String REFUND_INFO_PATH = PropertiesLoader.REFUND_INFO_PATH;

    private static final String CREATE_ORDER_PATH = PropertiesLoader.CREATE_ORDER_PATH;
    private static final String ORDER_PATH = PropertiesLoader.ORDER_PATH;

    private static final String ERROR_REQUIRED_PARAM_MISSING = "Field is required and should not be null or empty";

    private static final JsonMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    private final String domain;

    private final String secretKey;

    private final String sbpMerchantId;

    private final SdkHttpClient webClient;

    public SbpClient(String domain, String sbpMerchantId, String secretKey) {
        this(domain, sbpMerchantId, secretKey, new SdkHttpClient());
    }

    public SbpClient(String domain, String sbpMerchantId, String secretKey, SdkHttpClient sdkHttpClient) {
        this.domain = domain;
        this.sbpMerchantId = sbpMerchantId;
        this.secretKey = secretKey;
        webClient = sdkHttpClient;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
    }

    public QRUrl registerQR(final QR customerQr) throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        final QR qr = customerQr.newInstance();
        qr.verify();
        ObjectNode jsonNode = mapper.valueToTree(qr);
        jsonNode.put("sbpMerchantId", sbpMerchantId);
        return post(domain + REGISTER_PATH, jsonNode.toString(), QRUrl.class);
    }

    public RefundStatus refundPayment(final RefundInfo refund) throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        return post(domain + REFUND_PATH, mapper.writeValueAsString(refund), secretKey, RefundStatus.class);
    }

    public QRUrl getQRInfo(final QRId id) throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        if (StringUtil.isBlank(id.getQrId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        return get(domain + QR_INFO_PATH, id.getQrId(), secretKey, QRUrl.class);
    }

    public PaymentInfo getPaymentInfo(final QRId id) throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        if (StringUtil.isBlank(id.getQrId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        return get(domain + PAYMENT_INFO_PATH, id.getQrId(), secretKey, PaymentInfo.class);
    }

    public RefundStatus getRefundInfo(final RefundId id) throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        if (StringUtil.isBlank(id.getRefundId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        return get(domain + REFUND_INFO_PATH, id.getRefundId(), secretKey, RefundStatus.class);
    }

    public OrderInfo createOrder(final Order order) throws SbpException, IOException, ContractViolationException, URISyntaxException, InterruptedException {
        ObjectNode jsonNode = mapper.valueToTree(order);
        return post(domain + CREATE_ORDER_PATH, jsonNode.toString(), secretKey, OrderInfo.class);
    }

    public OrderInfo getOrderInfo(final OrderId id) throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        if (StringUtil.isBlank(id.getOrderId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        return get(domain + ORDER_PATH, id.getOrderId(), secretKey, OrderInfo.class);
    }

    public void orderCancellation(final OrderId orderId) throws ContractViolationException, SbpException, IOException, URISyntaxException, InterruptedException {
        if (StringUtil.isBlank(orderId.getOrderId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        delete(domain + ORDER_PATH, orderId.getOrderId(), secretKey);
    }

    private <T> T post(String url, String body, Class<T> resultClass)
            throws IOException, SbpException, ContractViolationException, URISyntaxException, InterruptedException {
        Response response = webClient.postRequest(url, getHeaders(), body);
        return convert(response, resultClass);
    }

    private <T> T post(String url, String body, final String secretKey, Class<T> resultClass)
            throws IOException, SbpException, ContractViolationException, URISyntaxException, InterruptedException {
        Response response = webClient.postRequest(url, prepareHeaders(secretKey), body);
        return convert(response, resultClass);
    }

    private <T> T get(String url, final String pathParameter, final String secretKey, Class<T> resultClass)
            throws IOException, SbpException, ContractViolationException, URISyntaxException, InterruptedException {
        url = url.replace("?", pathParameter);
        Response response = webClient.getRequest(url, prepareHeaders(secretKey));
        return convert(response, resultClass);
    }

    private void delete(String url, final String pathParameter, final String secretKey)
            throws IOException, SbpException, ContractViolationException, URISyntaxException, InterruptedException {
        url = url.replace("?", pathParameter);
        Response response = webClient.deleteRequest(url, prepareHeaders(secretKey));
        convert(response, null);
    }

    private <T> T convert(Response response, Class<T> resultClass) throws SbpException, ContractViolationException {
        var responseBody = response.getBody();
        var httpCode = response.getCode();
        try {
            var mappedBody = mapper.readTree(responseBody);
            var codeNode = mappedBody.get("code");

            if (codeNode != null &&
                    (codeNode.textValue().contains("ERROR.") || codeNode.textValue().contains("ORDER_"))) {
                var message = mappedBody.get("message").textValue();
                throw new SbpException(codeNode.textValue(), message);
            }

            if (httpCode == 200) {
                if (codeNode != null && !"SUCCESS".equals(codeNode.textValue())) {
                    throw new ContractViolationException(httpCode, responseBody);
                }
                return responseBody.isBlank() ? null : mapper.readValue(responseBody, resultClass);
            }
            throw new ContractViolationException(httpCode, responseBody);
        } catch (JsonProcessingException exception) {
            throw new ContractViolationException(httpCode, responseBody);
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
}
