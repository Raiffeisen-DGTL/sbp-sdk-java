package raiffeisen.sbp.sdk.model.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateQr {

    @JsonProperty
    private final String createDate;
    @JsonProperty
    private final String order;
    @JsonProperty
    private final QRType qrType;
    @JsonProperty
    private final String sbpMerchantId;

    @JsonProperty
    private String account = null;
    @JsonProperty
    private String additionalInfo = null;
    @JsonProperty
    private BigDecimal amount = null;
    @JsonProperty
    private String currency = null;
    @JsonProperty
    private String paymentDetails = null;
    @JsonProperty
    private String ExpirationDate = null;


    public CreateQr(String createDate,
                    String order,
                    QRType qrType,
                    String sbpMerchantId) {
        this.createDate = createDate;
        this.order = order;
        this.qrType = qrType;
        this.sbpMerchantId = sbpMerchantId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getOrder() {
        return order;
    }

    public QRType getQrType() {
        return qrType;
    }

    public String getSbpMerchantId() {
        return sbpMerchantId;
    }

    public String getAccount() {
        return account;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExpirationDate() {
        return ExpirationDate;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

}
