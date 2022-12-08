package raiffeisen.sbp.sdk.model.out;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import raiffeisen.sbp.sdk.util.DateUtil;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Order {

    private static final DateTimeFormatter TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");

    @Setter(AccessLevel.NONE)
    private BigDecimal amount;
    @Setter(AccessLevel.NONE)
    private OrderQr qr;
    @Setter(AccessLevel.PUBLIC)
    private OrderExtra extra;

    private String id;
    private String comment;
    private String createDate;
    private String expirationDate;

    public void setExpirationDate(ZonedDateTime time) {
        expirationDate = time.format(TIME_PATTERN);
    }

    public void setExpirationDate(String time) {
        expirationDate = time;
    }

    public void verify() {
        createDate = DateUtil.checkDate(createDate);
        expirationDate = DateUtil.calculateExpirationDate(expirationDate, createDate, "Order.expirationDate");
    }

    public Order(BigDecimal amount) {
        this.amount = amount;
    }

    public Order(BigDecimal amount, OrderQr orderQr) {
        this.amount = amount;
        this.qr = orderQr;
    }

}
