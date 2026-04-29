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

package raiffeisen.sbp.sdk.model.out;

import raiffeisen.sbp.sdk.model.QRType;

import java.math.BigDecimal;

public final class QRStatic extends QR {

    public QRStatic(String order) {
        this.order = order;
        setQrType(QRType.QRStatic);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public QRStatic newInstance() {
        QRStatic qrStatic = new QRStatic(order);
        qrStatic.setAmount(amount);
        qrStatic.setAccount(account);
        qrStatic.setAdditionalInfo(additionalInfo);
        qrStatic.setCreateDate(createDate);
        qrStatic.setPaymentDetails(paymentDetails);
        qrStatic.setQrExpirationDate(qrExpirationDate);
        return qrStatic;
    }
}
