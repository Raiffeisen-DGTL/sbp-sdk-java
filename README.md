## SBP API Java SDK

#### Документация

**API**: [https://e-commerce.raiffeisen.ru/api/doc/sbp.html](Документация)

#### Использование

Для использования SDK необходимо создать объект класса `SbpClient`, указав в конструкторе домен, на который будут отправляться запросы (`SbpClient.PRODUCTION_DOMAIN` или `SbpClient.TEST_DOMAIN`), и секретный ключ для авторизации:

 ~~~ java
String secretKey = "...";

SbpClient client = new SbpClient(SbpClient.PRODUCTION_DOMAIN, secretKey);
 ~~~

Все запросы осуществляются классаом `SbpClient` и возвращают объект класса `Response`, в котором содержится вся информация об ответе сервера.

#### Регистрация QR-кода

Для регистрации кода необходимо создать экземпляр класса `QRInfo` и заполнить поля. Для разных типов QR-кодов обязательные параметры отличаются. Полную информацию о возможных параметрах можно посмотреть в [документации](https://e-commerce.raiffeisen.ru/api/doc/sbp.html#operation/registerUsingPOST_1 "Документация к API").

Обязательные параметры:
- (*для `QRDynamic`*) cумма в рублях `amount(BigDecimal)`
- время формирования заявки `createDate(String <YYYY-MM-DD ТHH24:MM:SS±HH:MM>)`
- (*для `QRDynamic`*) валюта платежа `currency("RUB")`
- уникальный идентификатор заказа в системе партнёра `order(String)`
- тип QR-кода `qrType(QRType.QRDynamic)`
- идентификатор зарегистрированного партнёра в СБП `sbpMerchantId(String)`

~~~ java
QRInfo staticQR = QRInfo.creator().
                createDate("2019-07-22T09:14:38.107227+03:00").
                order("1-22-333").
                qrType(QRType.QRStatic).
                sbpMerchantId("MA0000000552").
                create();

QRInfo dynamicQR = QRInfo.creator().
                createDate("2019-07-22T09:14:38.107227+03:00").
                order("1-22-333").
                qrType(QRType.QRDynamic).
                amount(new BigDecimal(1110)).
                currency("RUB").
                sbpMerchantId("MA0000000552").
                create();
~~~

Для выполнения запроса необходимо вызвать соответствующий метод класса `SbpClient`, принимающий в качестве аргумента объект класса `QRInfo`:

~~~ java
Response response = client.registerQR(exampleQR);
~~~

#### Получение данных по зарегистрированному ранее QR-коду

Необходимо создать объект класса `QRId`, передав в конструкторе идентификатор QR-кода, и вызвать метод `getQRInfo`:

~~~ java
String qrIdString = "...";

QRId id = QRId.creator().qrId(qrIdString).create();

Response response = client.getQRInfo(id);
~~~

#### Получение информации по платежу

Данный запрос аналогичен предыдущему, нужно лишь вызвать метод `getPaymentInfo`:

~~~ java
String qrIdString = "...";

QRId id = QRId.creator().qrId(qrIdString).create();

Response response = client.getPaymentInfo(id);
~~~

#### Оформление возврата по платежу

Для возврата средств необходимо создать объект класса `RefundInfo`, заполнив необходимые поля. Подробности об обязательных полях в [документации](https://e-commerce.raiffeisen.ru/api/doc/sbp.html#operation/registerUsingPOST_1 "Документация к API").

~~~ java
BigDecimal moneyAmount = new BigDecimal(...)
String orderInfo = "...";
String refundId = "...";
long transactionId = ...;

RefundInfo refundInfo = RefundInfo.creator().
          			  amount(moneyAmount).
          			  order(orderInfo).
          			  refundId(refundInfo).
          			  transactionId(transactionId).
          			  create();

Response response = client.refundPayment(refundInfo);
~~~

#### Получение информации по возврату

Для выполнения данного запроса необходимо указать уникальный идентификатор запроса на возврат `refundId` при вызове метода `getRefundInfo`:

~~~ java
String refundId = "...";

Response response = client.getRefundInfo(refundId);
~~~

