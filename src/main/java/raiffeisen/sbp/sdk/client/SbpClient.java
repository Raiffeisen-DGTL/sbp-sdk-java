package raiffeisen.sbp.sdk.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
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


public class SbpClient implements Closeable {
    public static final String TEST_DOMAIN = "https://test.ecom.raiffeisen.ru";
    public static final String PRODUCTION_DOMAIN = "https://e-commerce.raiffeisen.ru";

    private static final String REGISTER_PATH = "/api/sbp/v1/qr/register";
    private static final String QR_INFO_PATH = "/api/sbp/v1/qr/?/info";
    private static final String PAYMENT_INFO_PATH = "/api/sbp/v1/qr/?/payment-info";
    private static final String REFUND_PATH = "/api/sbp/v1/refund";
    private static final String REFUND_INFO_PATH = "/api/sbp/v1/refund/?";
    private static final JsonMapper mapper = new JsonMapper();
    private final String domain;
    private final String secretKey;
    private final PostRequester postRequester;
    private final GetRequester getRequester;
    private WebClient webClient;

    public SbpClient(String domain, String secretKey) {
        this(domain, secretKey, new ApacheClient());
    }

    public SbpClient(String domain, String secretKey, WebClient customWebClient) {
        this.domain = domain;
        this.secretKey = secretKey;
        this.webClient = customWebClient;
        this.postRequester = new PostRequester(this.webClient);
        this.getRequester = new GetRequester(this.webClient);
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public void setWebClient(WebClient client) {
        webClient = client;
        postRequester.setWebClient(webClient);
        getRequester.setWebClient(webClient);
    }

    public QRUrl registerQR(final QRInfo qr) throws SbpException, IOException {
        QRInfo verifiedQr = QrInfoUtils.calculateDate(qr);
        Response tempResponse = postRequester.request(domain + REGISTER_PATH, mapper.writeValueAsString(verifiedQr), null);
        try {
            return mapper.readValue(tempResponse.getBody(), QRUrl.class);
        } catch (JsonProcessingException e) {
            throw new SbpException(String.valueOf(tempResponse.getCode()), tempResponse.getBody());

        }
    }

    public RefundStatus refundPayment(final RefundInfo refund) throws SbpException, IOException {
        Response tempResponse = postRequester.request(domain + REFUND_PATH, mapper.writeValueAsString(refund), secretKey);
        try {
            return mapper.readValue(tempResponse.getBody(), RefundStatus.class);
        } catch (JsonProcessingException e) {
            throw new SbpException(String.valueOf(tempResponse.getCode()), tempResponse.getBody());
        }
    }

    public QRUrl getQRInfo(final QRId qrId) throws SbpException, IOException {
        Response tempResponse = getRequester.request(domain + QR_INFO_PATH, qrId, secretKey);
        try {
            return mapper.readValue(tempResponse.getBody(), QRUrl.class);
        } catch (JsonProcessingException e) {
            throw new SbpException(String.valueOf(tempResponse.getCode()), tempResponse.getBody());

        }
    }

    public PaymentInfo getPaymentInfo(final QRId qrId) throws SbpException, IOException {
        Response tempResponse = getRequester.request(domain + PAYMENT_INFO_PATH, qrId, secretKey);
        try {
            return mapper.readValue(tempResponse.getBody(), PaymentInfo.class);
        } catch (JsonProcessingException e) {
            throw new SbpException(String.valueOf(tempResponse.getCode()), tempResponse.getBody());

        }
    }

    public RefundStatus getRefundInfo(final String refundId) throws SbpException, IOException {
        Response tempResponse = getRequester.request(domain + REFUND_INFO_PATH, refundId, secretKey);
        try {
            return mapper.readValue(tempResponse.getBody(), RefundStatus.class);
        } catch (JsonProcessingException e) {
            throw new SbpException(String.valueOf(tempResponse.getCode()), tempResponse.getBody());

        }
    }

    @Override
    public void close() throws IOException {
        webClient.close();
    }
}
