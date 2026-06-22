package raiffeisen.sbp.sdk.model.fiscal;

import lombok.Data;

@Data
public class ReceiptClient {
    private String email;

    public void verify() {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("email error");
        }
    }
}
