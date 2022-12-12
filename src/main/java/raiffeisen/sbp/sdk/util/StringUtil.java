package raiffeisen.sbp.sdk.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class StringUtil {
    public boolean isBlank(String string) {
        return string != null && !string.equals("");
    }
}
