package raiffeisen.sbp.sdk.util;

import lombok.experimental.UtilityClass;

import java.io.InvalidObjectException;

@UtilityClass
public final class StringUtil {
    public void isBlank(String string) throws InvalidObjectException {
        if (string == null || string.equals("")) {
            throw new InvalidObjectException("String id is blank!");
        }
    }
}
