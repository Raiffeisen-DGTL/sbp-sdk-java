package raiffeisen.sbp.sdk.model;

import raiffeisen.sbp.sdk.model.out.QRInfo;

import lombok.Builder;

import java.math.BigDecimal;

public class QRInfoCreator {
    @Builder(builderMethodName = "creator", buildMethodName = "create")
    public static QRInfo newQrInfo(String createDate
            , String order
            , QRType qrType
            , String sbpMerchantId
            , String account
            , String additionalInfo
            , BigDecimal amount
            , String currency
            , String paymentDetails
            , String ExpirationDate) {
        return new QRInfo(createDate
                , order
                , qrType
                , sbpMerchantId
                , account
                , additionalInfo
                , amount
                , currency
                , paymentDetails
                , ExpirationDate);
    }
}