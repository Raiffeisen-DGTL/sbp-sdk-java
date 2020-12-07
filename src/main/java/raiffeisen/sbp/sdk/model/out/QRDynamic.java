package raiffeisen.sbp.sdk.model.out;

import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;

public final class QRDynamic extends QR {

    public QRDynamic(String order, BigDecimal amount) {
        this.order = order;
        this.amount = amount;
        setQrType(QRType.QRDynamic);
    }

    @Override
    public QR newInstance() {
        QRDynamic qrStatic = new QRDynamic(order, amount);
        qrStatic.setAccount(account);
        qrStatic.setAdditionalInfo(additionalInfo);
        qrStatic.setCreateDate(createDate);
        qrStatic.setPaymentDetails(paymentDetails);
        qrStatic.setQrExpirationDate(qrExpirationDate);
        return qrStatic;
    }
}
