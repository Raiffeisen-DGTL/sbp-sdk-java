/*
 * Copyright 2020-present AO Raiffeisenbank
 *
 * This file is part of SBP SDK Java.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package raiffeisen.sbp.sdk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.Response;
import raiffeisen.sbp.sdk.model.in.*;
import raiffeisen.sbp.sdk.model.fiscal.Receipt;
import raiffeisen.sbp.sdk.model.fiscal.ReceiptInfo;
import raiffeisen.sbp.sdk.model.out.*;
import raiffeisen.sbp.sdk.util.StringUtil;
import raiffeisen.sbp.sdk.web.SdkHttpClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class SbpClient {
    public static final String TEST_URL = PropertiesLoader.TEST_URL;
    public static final String PRODUCTION_URL = PropertiesLoader.PRODUCTION_URL;
    public static final String FISCAL_TEST_URL = PropertiesLoader.FISCAL_TEST_URL;
    public static final String FISCAL_PRODUCTION_URL = PropertiesLoader.FISCAL_PRODUCTION_URL;

    private static final String REGISTER_PATH = PropertiesLoader.REGISTER_PATH;
    private static final String QR_INFO_PATH = PropertiesLoader.QR_INFO_PATH;
    private static final String PAYMENT_INFO_PATH = PropertiesLoader.PAYMENT_INFO_PATH;
    private static final String REFUND_PATH = PropertiesLoader.REFUND_PATH;
    private static final String REFUND_INFO_PATH = PropertiesLoader.REFUND_INFO_PATH;

    private static final String CREATE_ORDER_PATH = PropertiesLoader.CREATE_ORDER_PATH;
    private static final String ORDER_PATH = PropertiesLoader.ORDER_PATH;
    private static final String ORDER_REFUND_PATH = PropertiesLoader.ORDER_REFUND_PATH;
    private static final String NFC_PATH = PropertiesLoader.NFC_PATH;

    private static final String FISCAL_SAVE_SELL_PATH = PropertiesLoader.FISCAL_SAVE_SELL_PATH;
    private static final String FISCAL_REGISTER_SELL_PATH = PropertiesLoader.FISCAL_REGISTER_SELL_PATH;
    private static final String FISCAL_SELL_INFO_PATH = PropertiesLoader.FISCAL_SELL_INFO_PATH;
    private static final String FISCAL_SAVE_REFUND_PATH = PropertiesLoader.FISCAL_SAVE_REFUND_PATH;
    private static final String FISCAL_REGISTER_REFUND_PATH = PropertiesLoader.FISCAL_REGISTER_REFUND_PATH;
    private static final String FISCAL_REFUND_INFO_PATH = PropertiesLoader.FISCAL_REFUND_INFO_PATH;

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
        String url = String.format(domain + QR_INFO_PATH, id.getQrId());
        return get(url, secretKey, QRUrl.class);

    }

    public PaymentInfo getPaymentInfo(final QRId id) throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        if (StringUtil.isBlank(id.getQrId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        String url = String.format(domain + PAYMENT_INFO_PATH, id.getQrId());
        return get(url, secretKey, PaymentInfo.class);
    }

    public RefundStatus getRefundInfo(final RefundId id) throws SbpException, ContractViolationException, IOException, URISyntaxException, InterruptedException {
        if (StringUtil.isBlank(id.getRefundId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        String url = String.format(domain + REFUND_INFO_PATH, id.getRefundId());
        return get(url, secretKey, RefundStatus.class);
    }

    public OrderInfo createOrder(final Order order) throws SbpException, IOException, ContractViolationException, URISyntaxException, InterruptedException {
        ObjectNode jsonNode = mapper.valueToTree(order);
        return post(domain + CREATE_ORDER_PATH, jsonNode.toString(), secretKey, OrderInfo.class);
    }

    public OrderInfo getOrderInfo(final OrderId id) throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        if (StringUtil.isBlank(id.getOrderId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        String url = String.format(domain + ORDER_PATH, id.getOrderId());
        return get(url, secretKey, OrderInfo.class);
    }

    public void orderCancellation(final OrderId orderId) throws ContractViolationException, SbpException, IOException, URISyntaxException, InterruptedException {
        if (StringUtil.isBlank(orderId.getOrderId())) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        delete(domain + ORDER_PATH, orderId.getOrderId(), secretKey);
    }

    public RefundStatus orderRefund(final OrderRefund orderRefund) throws ContractViolationException, SbpException, IOException, URISyntaxException, InterruptedException {
        String orderId = orderRefund.getOrderId();
        String refundId = orderRefund.getRefundId();
        if (StringUtil.isBlank(orderId) || StringUtil.isBlank(refundId)) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        String url = String.format(domain + ORDER_REFUND_PATH, orderId, refundId);
        String body = mapper.writeValueAsString(orderRefund);
        return post(url, body, secretKey, RefundStatus.class);
    }

    public RefundStatus orderRefundStatus(final String orderId, final String refundId) throws ContractViolationException, SbpException, IOException, URISyntaxException, InterruptedException {
        if (StringUtil.isBlank(orderId) || StringUtil.isBlank(refundId)) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        String url = String.format(domain + ORDER_REFUND_PATH, orderId, refundId);
        return get(url, secretKey, RefundStatus.class);
    }

    public NFCInfo bindNfcLink(final NFC nfc) throws ContractViolationException, IOException, SbpException, URISyntaxException, InterruptedException {
        String qrId = nfc.getQrId();
        if (StringUtil.isBlank(qrId)) {
            throw new ContractViolationException(400, ERROR_REQUIRED_PARAM_MISSING);
        }
        String body = mapper.writeValueAsString(nfc);
        String url = String.format(domain + NFC_PATH, qrId);
        return post(url, body, secretKey, NFCInfo.class);
    }

    public ReceiptInfo saveSellReceipt(final Receipt receipt) throws ContractViolationException, IOException, SbpException, URISyntaxException, InterruptedException {
        receipt.verify();
        var body = mapper.valueToTree(receipt);

        String url = domain + FISCAL_SAVE_SELL_PATH;
        return post(url, body.toString(), secretKey, ReceiptInfo.class);
    }

    public ReceiptInfo registerSellReceipt(final String receiptNumber) throws SbpException, URISyntaxException, IOException, ContractViolationException, InterruptedException {
        String url = String.format(domain + FISCAL_REGISTER_SELL_PATH, receiptNumber);
        return put(url, secretKey, ReceiptInfo.class);
    }

    public ReceiptInfo getSellReceiptInfo(final String receiptNumber) throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        String url = String.format(domain + FISCAL_SELL_INFO_PATH, receiptNumber);
        return get(url, secretKey, ReceiptInfo.class);
    }

    public ReceiptInfo saveRefundReceipt(final Receipt receipt) throws ContractViolationException, IOException, SbpException, URISyntaxException, InterruptedException {
        receipt.verify();
        var body = mapper.valueToTree(receipt);
        String url = domain + FISCAL_SAVE_REFUND_PATH;
        return post(url, body.toString(), secretKey, ReceiptInfo.class);
    }

    public ReceiptInfo registerRefundReceipt(final String receiptNumber) throws SbpException, URISyntaxException, IOException, ContractViolationException, InterruptedException {
        String url = String.format(domain + FISCAL_REGISTER_REFUND_PATH, receiptNumber);
        return put(url, secretKey, ReceiptInfo.class);
    }

    public ReceiptInfo getRefundReceiptInfo(final String receiptNumber) throws SbpException, IOException, URISyntaxException, ContractViolationException, InterruptedException {
        String url = String.format(domain + FISCAL_REFUND_INFO_PATH, receiptNumber);
        return get(url, secretKey, ReceiptInfo.class);
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

    private <T> T get(String url, final String secretKey, Class<T> resultClass)
            throws IOException, SbpException, ContractViolationException, URISyntaxException, InterruptedException {
        Response response = webClient.getRequest(url, prepareHeaders(secretKey));
        return convert(response, resultClass);
    }

    private void delete(String url, final String pathParameter, final String secretKey)
            throws IOException, SbpException, ContractViolationException, URISyntaxException, InterruptedException {
        url = String.format(url, pathParameter);
        Response response = webClient.deleteRequest(url, prepareHeaders(secretKey));
        convert(response, null);
    }

    private <T> T put(String url, final String secretKey, Class<T> resultClass) throws URISyntaxException, IOException, InterruptedException, SbpException, ContractViolationException {
        Response response = webClient.putRequest(url, prepareHeaders(secretKey));
        return convert(response, resultClass);
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
