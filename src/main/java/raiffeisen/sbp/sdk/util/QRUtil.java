package raiffeisen.sbp.sdk.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QRUtil {

    public static String generateOrderNumber() {
        return UUID.randomUUID().toString();
    }
}
