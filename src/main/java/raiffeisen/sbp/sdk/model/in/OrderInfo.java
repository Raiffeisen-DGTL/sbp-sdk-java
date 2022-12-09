package raiffeisen.sbp.sdk.model.in;

import lombok.Data;
import raiffeisen.sbp.sdk.model.out.OrderExtra;
import raiffeisen.sbp.sdk.model.out.OrderQr;

import java.math.BigDecimal;

@Data
public class OrderInfo {
    private String id;
    private BigDecimal amount;
    private String comment;
    private OrderExtra extra;
    private OrderStatus status;
    private String expirationDate;
    private OrderQr qr;
}
