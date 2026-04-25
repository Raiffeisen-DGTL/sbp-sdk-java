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
