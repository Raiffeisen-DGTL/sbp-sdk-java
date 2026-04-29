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

package raiffeisen.sbp.sdk.model.in;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public final class PaymentInfo {
    private String additionalInfo;
    private BigDecimal amount;
    private ZonedDateTime createDate;
    private String currency;
    private long merchantId;
    private String order;
    private String paymentStatus;
    private String qrId;
    private String sbpMerchantId;
    private ZonedDateTime transactionDate;
    private long transactionId;
}
