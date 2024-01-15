package raiffeisen.sbp.sdk.model.out;

import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;

public final class QRStatic extends QR {

    public QRStatic(String order) {
        this.order = order;
        setQrType(QRType.QRStatic);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public QRStatic newInstance() {
        QRStatic qrStatic = new QRStatic(order);
        qrStatic.setAmount(amount);
        qrStatic.setAccount(account);
        qrStatic.setAdditionalInfo(additionalInfo);
        qrStatic.setCreateDate(createDate);
        qrStatic.setPaymentDetails(paymentDetails);
        qrStatic.setQrExpirationDate(qrExpirationDate);
        qrStatic.setQrDescription(qrDescription);
        return qrStatic;
    }
}
