package raiffeisen.sbp.sdk.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

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

    public static final Logger logger = Logger.getLogger(PropertiesLoader.class);

    static {
        Properties properties = new Properties();
        logger.info("Loading config file...");
        boolean isLoaded = true;
        try {
            InputStream propertiesFile = ClassLoader.getSystemResourceAsStream("config.properties");
            properties.load(propertiesFile);
        } catch (NullPointerException | IOException e) {
            isLoaded = false;
            logger.warn("Cannot load configuration file. Loading default values.");
        }

        if (isLoaded) logger.info("Loading config file is complete.");

        TEST_DOMAIN = properties.getProperty("domain.sandbox", "https://test.ecom.raiffeisen.ru");
        PRODUCTION_DOMAIN = properties.getProperty("domain.production", "https://e-commerce.raiffeisen.ru");

        REGISTER_PATH = properties.getProperty("path.register.qr", "/api/sbp/v1/qr/register");
        QR_INFO_PATH = properties.getProperty("path.qr.info", "/api/sbp/v1/qr/?/info");
        PAYMENT_INFO_PATH = properties.getProperty("path.payment.info", "/api/sbp/v1/qr/?/payment-info");
        REFUND_PATH = properties.getProperty("path.refund", "/api/sbp/v1/refund");
        REFUND_INFO_PATH = properties.getProperty("path.refund.info", "/api/sbp/v1/refund/?");
    }
}
