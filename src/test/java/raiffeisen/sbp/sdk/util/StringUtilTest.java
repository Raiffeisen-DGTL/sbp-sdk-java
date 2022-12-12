package raiffeisen.sbp.sdk.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
public class StringUtilTest {

    @Test
    void isEmptyTrue() {
        assertTrue(StringUtil.isBlank("12345"));
    }

    @Test
    void isEmptyWithNullFalse() {
        assertFalse(StringUtil.isBlank(null));
    }

    @Test
    void isEmptyWithBlankFalse() {
        assertFalse(StringUtil.isBlank(""));
    }
}
