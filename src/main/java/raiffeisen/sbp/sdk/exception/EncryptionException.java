package raiffeisen.sbp.sdk.exception;

public class EncryptionException extends RuntimeException {
    public EncryptionException(Throwable cause) {
        super(cause);
    }

    public EncryptionException(String cause) {
        super(cause);
    }
}
