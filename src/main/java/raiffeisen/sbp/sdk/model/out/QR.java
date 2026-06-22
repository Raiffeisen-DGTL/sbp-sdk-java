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

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import raiffeisen.sbp.sdk.model.QRType;
import raiffeisen.sbp.sdk.util.DateUtil;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class QR {

    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");

    @Setter(AccessLevel.NONE)
    protected String order;
    @Setter(AccessLevel.NONE)
    protected BigDecimal amount;
    @Setter(AccessLevel.PROTECTED)
    protected QRType qrType;

    protected String account;
    protected String additionalInfo;
    protected String createDate;
    protected String paymentDetails;
    protected String qrExpirationDate;
    protected String qrDescription;
    protected String redirectUrl;
    protected Extra extra;
    protected Subscription subscription;

    public abstract QR newInstance();

    public void setCreateDate(ZonedDateTime time) {
        createDate = time.format(TIME_PATTERN);
    }

    public void setCreateDate(String time) {
        createDate = time;
    }

    public void setQrExpirationDate(ZonedDateTime time) {
        qrExpirationDate = time.format(TIME_PATTERN);
    }

    public void setQrExpirationDate(String time) {
        qrExpirationDate = time;
    }

    public void verify() {
        createDate = DateUtil.checkDate(createDate);
        qrExpirationDate = DateUtil.calculateExpirationDate(qrExpirationDate, createDate, "QR.qrExpirationDate");
    }

}
