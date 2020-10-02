package raiffeisen.sbp.sdk.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesLoader {
    public static final String TEST_DOMAIN;
    public static final String PRODUCTION_DOMAIN;

    public static final String REGISTER_PATH;
    public static final String QR_INFO_PATH;
    public static final String PAYMENT_INFO_PATH;
    public static final String REFUND_PATH;
    public static final String REFUND_INFO_PATH;

    static {
        Properties properties = new Properties();
        try {
            InputStream propertiesFile = ClassLoader.getSystemResourceAsStream("config.properties");
            properties.load(propertiesFile);
        } catch (NullPointerException e) {
            // TODO: do logging here
        } catch (IOException e) {
            // TODO: do logging here
        }

        TEST_DOMAIN = properties.getProperty("domain.sandbox", "https://test.ecom.raiffeisen.ru");
        PRODUCTION_DOMAIN = properties.getProperty("domain.production", "https://e-commerce.raiffeisen.ru");

        REGISTER_PATH = properties.getProperty("path.register.qr", "/api/sbp/v1/qr/register");
        QR_INFO_PATH = properties.getProperty("path.qr.info", "/api/sbp/v1/qr/?/info");
        PAYMENT_INFO_PATH = properties.getProperty("path.payment.info", "/api/sbp/v1/qr/?/payment-info");
        REFUND_PATH = properties.getProperty("path.refund", "/api/sbp/v1/refund");
        REFUND_INFO_PATH = properties.getProperty("path.refund.info", "/api/sbp/v1/refund/?");
    }
}
