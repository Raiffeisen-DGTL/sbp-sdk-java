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

package raiffeisen.sbp.sdk.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public final class PaymentNotification {
    private long transactionId;
    private String qrId;
    private String sbpMerchantId;
    private long merchantId;
    private BigDecimal amount;
    private String currency;
    private String transactionDate;
    private String paymentStatus;
    private String additionalInfo;
    private String order;
    private String createDate;
}
