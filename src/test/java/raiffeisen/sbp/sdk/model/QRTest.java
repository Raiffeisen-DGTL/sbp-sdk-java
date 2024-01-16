package raiffeisen.sbp.sdk.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import raiffeisen.sbp.sdk.model.out.QR;
import raiffeisen.sbp.sdk.model.out.QRStatic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@Tag("unit")
class QRTest {
    @Test
    void success_OrderSavedIfPresent() {
        final String expected = "1-2-3";

        QRStatic qrStatic = new QRStatic(expected);
        qrStatic.verify();
        assertEquals(expected, qrStatic.getOrder());
    }

    @Test
    void testIsExistsQrDescriptionField() {
        try {
            Field qrDescriptionField = QR.class.getDeclaredField("qrDescription");
            assertNotNull(qrDescriptionField);
        } catch (NoSuchFieldException e) {
            fail("Поле qrDescription не найдено");
        }
    }
}
