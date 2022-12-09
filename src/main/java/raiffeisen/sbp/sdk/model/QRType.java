package raiffeisen.sbp.sdk.model;

public enum QRType {
    QRStatic("QRStatic"),
    QRDynamic("QRDynamic"),
    QRVariable("QRVariable");

    private final String type;

    QRType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "QRType=" + type + '\n';
    }
}
