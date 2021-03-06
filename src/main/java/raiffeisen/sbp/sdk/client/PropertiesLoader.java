package raiffeisen.sbp.sdk.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesLoader {
    public static final String TEST_URL;
    public static final String PRODUCTION_URL;

    public static final String REGISTER_PATH;
    public static final String QR_INFO_PATH;
    public static final String PAYMENT_INFO_PATH;
    public static final String REFUND_PATH;
    public static final String REFUND_INFO_PATH;

    static {
        Properties properties = new Properties();
        log.info("Loading config file...");
        try (InputStream propertiesFile = PropertiesLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(propertiesFile);
            log.info("Loading config file is complete.");
        } catch (NullPointerException | IOException e) {
            log.error("Cannot load configuration file. Loading default values.");
        }

        TEST_URL = properties.getProperty("domain.sandbox", "https://test.ecom.raiffeisen.ru");
        PRODUCTION_URL = properties.getProperty("domain.production", "https://e-commerce.raiffeisen.ru");

        REGISTER_PATH = properties.getProperty("path.register.qr", "/api/sbp/v1/qr/register");
        QR_INFO_PATH = properties.getProperty("path.qr.info", "/api/sbp/v1/qr/?/info");
        PAYMENT_INFO_PATH = properties.getProperty("path.payment.info", "/api/sbp/v1/qr/?/payment-info");
        REFUND_PATH = properties.getProperty("path.refund", "/api/sbp/v1/refund");
        REFUND_INFO_PATH = properties.getProperty("path.refund.info", "/api/sbp/v1/refund/?");
    }
}
