package raiffeisen.sbp.sdk.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
public class StringUtilTest {

    @Test
    void isEmptyFalse() {
        assertFalse(StringUtil.isBlank("12345"));
    }

    @Test
    void isEmptyWithNullTrue() {
        assertTrue(StringUtil.isBlank(null));
    }

    @Test
    void isEmptyWithBlankTrue() {
        assertTrue(StringUtil.isBlank(""));
    }
}
