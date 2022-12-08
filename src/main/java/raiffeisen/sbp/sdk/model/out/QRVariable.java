package raiffeisen.sbp.sdk.model.out;

import raiffeisen.sbp.sdk.model.QRType;

public final class QRVariable extends QR {

    public QRVariable() {
        setQrType(QRType.QRVariable);
    }

    @Override
    public QR newInstance() {
        QRVariable qrVariable = new QRVariable();
        qrVariable.setAccount(account);
        return qrVariable;
    }
}
