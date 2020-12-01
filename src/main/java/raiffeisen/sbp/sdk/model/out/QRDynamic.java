package raiffeisen.sbp.sdk.model.out;

import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;

public class QRDynamic extends QR {
    public QRDynamic(String order, BigDecimal amount) {
        this.order = order;
        this.amount = amount;
        this.qrType = QRType.QRDynamic;
    }

    public QRDynamic(QR code) {
        makeCopy(code);
    }
}
