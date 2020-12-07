package raiffeisen.sbp.sdk.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QRUtils {

    public static String generateOrderNumber() {
        return UUID.randomUUID().toString();
    }
}
