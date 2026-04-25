/*
 * Copyright 2020-present AO Raiffeisenbank
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package raiffeisen.sbp.sdk.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertiesLoader {
    public static final String TEST_URL;
    public static final String PRODUCTION_URL;

    public static final String REGISTER_PATH;
    public static final String QR_INFO_PATH;
    public static final String PAYMENT_INFO_PATH;
    public static final String REFUND_PATH;
    public static final String REFUND_INFO_PATH;
    public static final String CREATE_ORDER_PATH;
    public static final String ORDER_PATH;
    public static final String ORDER_REFUND_PATH;
    public static final String NFC_PATH;

    static {
        Properties properties = new Properties();
        System.out.println("Loading config file...");
        try (InputStream propertiesFile = PropertiesLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(propertiesFile);
            System.out.println("Loading config file is complete.");
        } catch (NullPointerException | IOException e) {
            System.err.println("Cannot load configuration file. Loading default values.");
        }

        TEST_URL = properties.getProperty("domain.sandbox", "https://pay-test.raif.ru");
        PRODUCTION_URL = properties.getProperty("domain.production", "https://pay.raif.ru");

        REGISTER_PATH = properties.getProperty("path.register.qr", "/api/sbp/v2/qrs");
        QR_INFO_PATH = properties.getProperty("path.qr.info", "/api/sbp/v2/qrs/%s");
        PAYMENT_INFO_PATH = properties.getProperty("path.payment.info", "/api/sbp/v1/qr/%s/payment-info");
        REFUND_PATH = properties.getProperty("path.refund", "/api/sbp/v1/refund");
        REFUND_INFO_PATH = properties.getProperty("path.refund.info", "/api/sbp/v1/refund/%s");
        CREATE_ORDER_PATH = properties.getProperty("path.create.order", "/api/payment/v1/orders");
        ORDER_PATH = properties.getProperty("path.order", "/api/payment/v1/orders/%s");
        ORDER_REFUND_PATH = properties.getProperty("path.order.refund", "/api/payments/v1/orders/%s/refunds/%s");
        NFC_PATH = properties.getProperty("path.nfc", "/api/sbp/v1/qr-drafts/%s");
    }
}
