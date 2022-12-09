package raiffeisen.sbp.sdk.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.model.out.QRStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
class QRTest {
    @Test
    void success_OrderSavedIfPresent() {
        final String expected = "1-2-3";

        QRStatic qrStatic = new QRStatic(expected);
        qrStatic.verify();
        assertEquals(expected, qrStatic.getOrder());
    }
}
