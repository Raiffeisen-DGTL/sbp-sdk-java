package raiffeisen.sbp.sdk.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.InvalidObjectException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class StringUtilTest {

    @Test
    void isEmptySuccess() throws InvalidObjectException {
        StringUtil.isBlank("12345");
    }

    @Test
    void isEmptyWithNull() {
        InvalidObjectException thrown = assertThrows(InvalidObjectException.class, () -> StringUtil.isBlank(null));
        assertEquals("String id is blank!", thrown.getMessage());
    }

    @Test
    void isEmptyWithBlank() {
        InvalidObjectException thrown = assertThrows(InvalidObjectException.class, () -> StringUtil.isBlank(""));
        assertEquals("String id is blank!", thrown.getMessage());
    }
}
