## SBP API Java SDK

#### Документация

**API**: [https://e-commerce.raiffeisen.ru/api/doc/sbp.html](Документация)

#### Использование

Для использования SDK необходимо создать объект класса `SbpClient`, указав в конструкторе домен, на который будут отправляться запросы (`SbpClient.PRODUCTION_DOMAIN` или `SbpClient.TEST_DOMAIN`), и секретный ключ для авторизации:

 ~~~ java
String secretKey = "...";

SbpClient client = new SbpClient(SbpClient.PRODUCTION_DOMAIN, secretKey);
 ~~~

Все запросы осуществляются классаом `SbpClient` и возвращают объекты следующих классов:
- `QRURl` для информации, связанной с QR-кодом
- `RefundStatus` -- для возвратов
- `PaymentInfo` -- для платежей

Эти классы содержат в себе те же поля, что и ответ сервера.

В случае логической ошибки клиент вернёт исключение `SbpException` с сообщением об ошибке.

#### Регистрация QR-кода

Для регистрации кода необходимо создать экземпляр класса `QRInfo` и заполнить поля. Для разных типов QR-кодов обязательные параметры отличаются. Полную информацию о возможных параметрах можно посмотреть в [документации](https://e-commerce.raiffeisen.ru/api/doc/sbp.html#operation/registerUsingPOST_1 "Документация к API").

Обязательные параметры:
- (*для `QRDynamic`*) cумма в рублях `amount(BigDecimal)`
- (*для `QRDynamic`*) валюта платежа `currency("RUB")`
- уникальный идентификатор заказа в системе партнёра `order(String)`
- тип QR-кода `qrType(QRType.QRDynamic)`
- идентификатор зарегистрированного партнёра в СБП `sbpMerchantId(String)`
- *время формирования заявки `createDate(String <YYYY-MM-DD ТHH24:MM:SS±HH:MM>)`* (опционально при использовании SDK)

Для выполнения запроса необходимо вызвать соответствующий метод класса `SbpClient`, принимающий в качестве аргумента объект класса `QRInfo`:

~~~ java
QRInfo exampleQR = QRInfo.creator().
                account("40700000000000000000").
                additionalInfo("Доп информация").
                amount(new BigDecimal(1110)).
                createDate("2019-07-22T09:14:38.107227+03:00").
                currency("RUB").
                order("1-22-333").
                paymentDetails("Назначение платежа").
                qrType(QRType.QRStatic).
                qrExpirationDate("2023-07-22T09:14:38.107227+03:00").
                sbpMerchantId("MA0000000552").
                create();

QRUrl response = client.registerQR(exampleQR);
~~~

Ответ:

~~~
{
  "code": "SUCCESS",
  "qrId": "AD100004BAL7227F9BNP6KNE007J9B3K",
  "payload": "https://qr.nspk.ru/AD100004BAL7227F9BNP6KNE007J9B3K?type=02&bank=100000000007&sum=1&cur=RUB&crc=AB75",
  "qrUrl": "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/AD100004BAL7227F9BNP6KNE007J9B3K/image"
}
~~~

#### Получение данных по зарегистрированному ранее QR-коду

Необходимо создать объект класса `QRId`, передав в конструкторе идентификатор QR-кода, и вызвать метод `getQRInfo(QRId)`:

~~~ java
String qrIdString = "...";

QRId id = QRId.creator().qrId(qrIdString).create();

QRUrl response = client.getQRInfo(id);
~~~

Ответ

~~~
{
  "code": "SUCCESS",
  "qrId": "AD100004BAL7227F9BNP6KNE007J9B3K",
  "payload": "https://qr.nspk.ru/AD100004BAL7227F9BNP6KNE007J9B3K?type=02&bank=100000000007&sum=1&cur=RUB&crc=AB75",
  "qrUrl": "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/AD100004BAL7227F9BNP6KNE007J9B3K/image"
}
~~~

#### Получение информации по платежу

Необходимо создать объект класса `QRId`, передав в конструкторе идентификатор QR-кода, и вызвать метод `getPaymentInfo(QRId)`:

~~~ java
String qrIdString = "...";

QRId id = QRId.creator().qrId(qrIdString).create();

PaymentInfo response = client.getPaymentInfo(id);
~~~

Ответ:

~~~
{
  "additionalInfo": "Доп информация",
  "amount": 12399,
  "code": "SUCCESS",
  "createDate": "2020-01-31T09:14:38.107227+03:00",
  "currency": "RUB",
  "merchantId": 123,
  "order": "282a60f8-dd75-4286-bde0-af321dd081b3",
  "paymentStatus": "NO_INFO",
  "qrId": "AD100051KNSNR64I98CRUJUASC9M72QT",
  "sbpMerchantId": "MA0000000553",
  "transactionDate": "2019-07-11T17:45:13.109227+03:00",
  "transactionId": 23
}
~~~

#### Оформление возврата по платежу

Для возврата средств необходимо создать объект класса `RefundInfo`, заполнив необходимые поля, и вызвать метод `refundPayment(RefundInfo)`. Подробности об обязательных полях в [документации](https://e-commerce.raiffeisen.ru/api/doc/sbp.html#operation/registerUsingPOST_1 "Документация к API").

~~~ java
BigDecimal moneyAmount = new BigDecimal(150)
String orderInfo = "...";
String refundId = "...";
long transactionId = ...;

RefundInfo refundInfo = RefundInfo.creator().
          			  amount(moneyAmount).
          			  order(orderInfo).
          			  refundId(refundInfo).
          			  transactionId(transactionId).
          			  create();

RefundStatus response = client.refundPayment(refundInfo);
~~~

Ответ:

~~~
{
  "code": "SUCCESS",
  "amount": 150,
  "refundStatus": "IN_PROGRESS"
}
~~~

#### Получение информации по возврату

Для выполнения данного запроса необходимо указать уникальный идентификатор запроса на возврат `refundId` при вызове метода `getRefundInfo(refundId)`:

~~~ java
String refundId = "...";

RefundStatus response = client.getRefundInfo(refundId);
~~~

Ответ:

~~~
{
  "code": "SUCCESS",
  "amount": 150,
  "refundStatus": "IN_PROGRESS"
}
~~~

#### Обработка уведомлений

Для обработки уведомлений существует класс `PaymentNotification`. Инициализация происходит с помощью статического метода `PaymentNotification.fromJson(String)`:

~~~ java
String jsonString = "...";
PaymentNotification notification = PaymentNotification.fromJson(jsonString);
~~~

#### Проверка подписи

Для проверки подлинности уведомления существует класс `SbpUtils`. Проверка подписи осуществляется при помощи перегруженного статического метода `checkNotificationSignature`. Примеры использования:

~~~ java
String jsonString = "...";
String apiSignature = "...";
String secretKey = "...";

boolean success = SbpUtils.checkNotificationSignature(jsonString, apiSignature, secretKey);
~~~

~~~ java
PaymentNotification notification = ...;
String apiSignature = "...";
String secretKey = "...";

boolean success = SbpUtils.checkNotificationSignature(notification, apiSignature, secretKey);
~~~

~~~ java
BigDecimal amount = ...;
String sbpMerchantId = "...";
String order = "...";
String paymentStatus = "...";
String transactionDate = "...";

String apiSignature = "...";
String secretKey = "...";

boolean success = SbpUtils.checkNotificationSignature(amount, 
                 	sbpMerchantId, 
                 	order,
                 	paymentStatus,
                 	transactionDate,
                 	apiSignature,
                 	secretKey);
~~~

#### Использование альтернативного HTTP-клиента

По умолчанию для HTTP-запросов используется Apache (класс `ApacheClient`), но можно воспользоваться любым другим, реализовав интерфейс `WebClient`:

~~~ java
public interface WebClient extends Closeable {
    Response request(String method, 
			String url, 
			Map<String, String> headers, 
			String entity) throws IOException;
}
~~~

Примеры использования:

~~~ java
CustomWebClient customClient = ...;
SbpClient client = new SbpClient(SbpClient.PRODUCTION_DOMAIN, secretKey, customClient); 
~~~

~~~ java
SbpClient client = ...;

...

CustomWebClient customClient = ...;
client.setWebClient(customClient);
~~~
















