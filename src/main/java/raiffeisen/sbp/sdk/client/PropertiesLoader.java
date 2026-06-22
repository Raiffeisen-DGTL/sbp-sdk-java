/*
 * Copyright 2020-present AO Raiffeisenbank
 *
 * This file is part of SBP SDK Java.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
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
    public static final String FISCAL_TEST_URL;
    public static final String FISCAL_PRODUCTION_URL;

    public static final String REGISTER_PATH;
    public static final String QR_INFO_PATH;
    public static final String PAYMENT_INFO_PATH;
    public static final String REFUND_PATH;
    public static final String REFUND_INFO_PATH;
    public static final String CREATE_ORDER_PATH;
    public static final String ORDER_PATH;
    public static final String ORDER_REFUND_PATH;
    public static final String NFC_PATH;
    public static final String FISCAL_SAVE_SELL_PATH;
    public static final String FISCAL_REGISTER_SELL_PATH;
    public static final String FISCAL_SELL_INFO_PATH;
    public static final String FISCAL_SAVE_REFUND_PATH;
    public static final String FISCAL_REGISTER_REFUND_PATH;
    public static final String FISCAL_REFUND_INFO_PATH;


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
        FISCAL_TEST_URL = properties.getProperty("domain.fiscal.sandbox", "https://test.ecom.raiffeisen.ru");
        FISCAL_PRODUCTION_URL = properties.getProperty("domain.fiscal.production", "https://e-commerce.raiffeisen.ru");

        REGISTER_PATH = properties.getProperty("path.register.qr", "/api/sbp/v2/qrs");
        QR_INFO_PATH = properties.getProperty("path.qr.info", "/api/sbp/v2/qrs/%s");
        PAYMENT_INFO_PATH = properties.getProperty("path.payment.info", "/api/sbp/v1/qr/%s/payment-info");
        REFUND_PATH = properties.getProperty("path.refund", "/api/sbp/v1/refund");
        REFUND_INFO_PATH = properties.getProperty("path.refund.info", "/api/sbp/v1/refund/%s");
        CREATE_ORDER_PATH = properties.getProperty("path.create.order", "/api/payment/v1/orders");
        ORDER_PATH = properties.getProperty("path.order", "/api/payment/v1/orders/%s");
        ORDER_REFUND_PATH = properties.getProperty("path.order.refund", "/api/payments/v1/orders/%s/refunds/%s");
        NFC_PATH = properties.getProperty("path.nfc", "/api/sbp/v1/qr-drafts/%s");


        FISCAL_SAVE_SELL_PATH = properties.getProperty("path.fiscal.save.sell", "/api/fiscal/v1/receipts/sell");
        FISCAL_REGISTER_SELL_PATH = properties.getProperty("path.fiscal.register.sell", "/api/fiscal/v1/receipts/sell/%s");
        FISCAL_SELL_INFO_PATH = properties.getProperty("path.fiscal.sell.info", "/api/fiscal/v1/receipts/sell/%s");
        FISCAL_SAVE_REFUND_PATH = properties.getProperty("path.fiscal.save.refund", "/api/fiscal/v1/receipts/refund");
        FISCAL_REGISTER_REFUND_PATH = properties.getProperty("path.fiscal.register.refund", "/api/fiscal/v1/receipts/refund/%s");
        FISCAL_REFUND_INFO_PATH = properties.getProperty("path.fiscal.refund.info", "/api/fiscal/v1/receipts/refund/%s");
    }
}
