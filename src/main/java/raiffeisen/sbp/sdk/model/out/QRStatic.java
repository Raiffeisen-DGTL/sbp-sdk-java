package raiffeisen.sbp.sdk.model.out;

import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;

public final class QRStatic extends QR {
    public QRStatic(String order) {
        this.order = order;
        qrType = QRType.QRStatic;
    }

    public QRStatic(QR qr) {
        makeCopy(qr);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
