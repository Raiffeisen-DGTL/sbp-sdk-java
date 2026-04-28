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

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import raiffeisen.sbp.sdk.util.DateUtil;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class Order {

    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
    private final OrderExtra extra = new OrderExtra();

    @NonNull
    private BigDecimal amount;
    private OrderQr qr;
    private String id;
    private String comment;
    private String createDate;
    private String expirationDate;

    public void verify() {
        createDate = DateUtil.checkDate(createDate);
        expirationDate = DateUtil.calculateExpirationDate(expirationDate, createDate, "Order.expirationDate");
    }

}
