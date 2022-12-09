# Java SDK для интеграции с API СБП
[Link to English version](/README.en.md)
## Содержание
- [Документация](#документация)
- [Подключение](#подключение)
- [Использование](#использование)
- [Регистрация QR-кода](#регистрация-qr-кода)
- [Получение данных по зарегистрированному ранее QR-коду](#получение-данных-по-зарегистрированному-ранее-qr-коду)
- [Получение информации по платежу](#получение-информации-по-платежу)
- [Оформление возврата по платежу](#оформление-возврата-по-платежу)
- [Получение информации по возврату](#получение-информации-по-возврату)
- [Обработка уведомлений](#обработка-уведомлений)
- [Использование альтернативного HTTP-клиента](#использование-альтернативного-http-клиента)
- [Шпаргалка](#шпаргалка)

## Документация

**API**: [https://e-commerce.raiffeisen.ru/api/doc/sbp.html](Документация)

## Подключение

Требования:
- Java 11+
- Apache Maven

Для подключения SDK требуется:
- Создать в корне проекта каталог с названием "dependencies".
- Поместить в созданный каталог файл .jar и [pom.xml по ссылке](/docs/dependencies/pom.xml).
- pom.xml __своего__ проекта поместить следующее:
~~~
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>raiffeisen</groupId>
            <artifactId>sbp-sdk-java</artifactId>
            <version>1.0.2</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-install-plugin</artifactId>
                <version>2.5.2</version>
                <configuration>
                    <groupId>raiffeisen</groupId>
                    <artifactId>sbp-sdk-java</artifactId>
                    <version>1.0.2</version>
                    <packaging>jar</packaging>
                    <file>dependencies/sbp-sdk-java-1.0.2.jar</file>
                    <generatePom>false</generatePom>
                    <pomFile>dependencies/pom.xml</pomFile>
                </configuration>
                <executions>
                    <execution>
                        <id>install-jar-lib</id>
                        <goals>
                            <goal>install-file</goal>
                        </goals>
                        <phase>validate</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
~~~
- Выполнить команды в maven:
   + validate(`mvn validate`)
   + install(`mvn install`)

## Использование

Для использования SDK необходимо создать объект класса `SbpClient`, указав в конструкторе URL, на который будут отправляться запросы (`SbpClient.PRODUCTION_URL` или `SbpClient.TEST_URL`), id мерчанта в системе СБП и секретный ключ для авторизации.
Этот клиент используется для всех взаимодействий с API.

Все запросы осуществляются классом `SbpClient`. Посмотреть возвращаемые значения можно по [ссылке](#шпаргалка).

Клиент может вернуть следующие типы исключений:
- `IOException` - ошибка сетевого взаимодействия
- `SbpException` - логическая ошибка при попытке обработать запрос
- `ContractViolationException` - ошибка в обработке ответа от сервера

 ~~~ java
import raiffeisen.sbp.sdk.client.SbpClient;
import raiffeisen.sbp.sdk.exception.ContractViolationException;
import raiffeisen.sbp.sdk.exception.SbpException;
import raiffeisen.sbp.sdk.model.in.QRUrl;
import raiffeisen.sbp.sdk.model.out.QR;
import raiffeisen.sbp.sdk.model.out.QRDynamic;
import raiffeisen.sbp.sdk.util.QRUtil;

import java.io.IOException;
import java.math.BigDecimal;

public class AppExample {
    public static void main(String[] args) {
        String secretKey = "..."; // change this to your secretKey
        String sbpMerchantId = "..."; // change this to your sbpMerchantId
        SbpClient client = new SbpClient(SbpClient.TEST_URL, sbpMerchantId, secretKey);
        try {
            String order = QRUtil.generateOrderNumber();
            // save order in a database;
            QR qrCode = new QRDynamic(order, new BigDecimal(100));
            qrCode.setAccount("40700000000000000000");
            qrCode.setAdditionalInfo("Доп информация");
            qrCode.setPaymentDetails("Назначение платежа");
            qrCode.setQrExpirationDate(ZonedDateTime.now().plusDays(1));
            QRUrl response = client.registerQR(qrCode);
            response.getQrId();
            response.getOrUrl();
            response.getPayload();
        }
        catch (IOException networkException) {
            networkException.getMessage();
        }
        catch (SbpException sbpException) {
            sbpException.getCode(); // Error id
            sbpException.getMessage();
        }
        catch (ContractViolationException contractException) {
            contractException.getHttpCode();
            contractException.getMessage();
        }
    }
}
 ~~~

## Регистрация QR-кода

Для регистрации кода необходимо создать экземпляр класса `QRStatic`, `QRDynamic` или `QRVariable`. Для разных типов QR-кодов обязательные параметры отличаются.

Обязательные параметры:
- номер заказа в системе партнера `order(String)`
- (*`QRDynamic`*) сумма в рублях `amount(BigDecimal)`

Опциональные параметры могут быть заполнены с помощью set методов.

Для выполнения запроса необходимо вызвать соответствующий метод класса `SbpClient`, принимающий в качестве аргумента объект класса `QR`:

~~~ java
String order = QRUtil.generateOrderNumber();

// save order in a database;

QRDynamic qrCode = new QRDynamic(order, new BigDecimal(100));
qrCode.setAccount("40700000000000000000");
qrCode.setAdditionalInfo("Доп информация");
qrCode.setPaymentDetails("Назначение платежа");
qrCode.setQrExpirationDate("2023-07-22T09:14:38.107227+03:00");

QRUrl response = client.registerQR(qrCode);

// place your code here
~~~

Ответ:

~~~
{
  "qrId": "AD100004BAL7227F9BNP6KNE007J9B3K",
  "payload": "https://qr.nspk.ru/AD100004BAL7227F9BNP6KNE007J9B3K?type=02&bank=100000000007&sum=1&cur=RUB&crc=AB75",
  "qrUrl": "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/AD100004BAL7227F9BNP6KNE007J9B3K/image"
}
~~~

Пример с минимальными данными:

~~~ java
String order = QRUtil.generateOrderNumber(); // UUID_v4

// save order in a database;

// QRStatic qrStatic = new QRStatic(order);
//
// or
//
// QRDynamic qrDynamic = new QRDynamic(order, new BigDecimal(100));
~~~

Также существует возможность заполнить необязательный параметр `qrExpirationDate` с помощью сдвига относительно даты создания. Для этого в поле `qrExpirationDate` следует указать строку вида `"+<число1><буква1>"`.

- 'M' - месяц
- 'd' - день
- 'H' - час
- 'm' - минута
- 's' - секунда

Пример:

~~~ java
String order = QRUtil.generateOrderNumber(); // UUID_v4

// save order in a database;

QRStatic qrStatic = new QRStatic(order);
qrStatic.setQrExpirationDate("+5m"); // + 5 minutes

QRDynamic qrDynamic = new QRDynamic(order, new BigDecimal(100));
qrDynamic.setQrExpirationDate("+1d5m"); // + 1 day 5 minutes
~~~

## Получение данных по зарегистрированному ранее QR-коду

Необходимо создать объект класса `QRId`, передав в конструкторе идентификатор QR-кода, и вызвать метод `getQRInfo(QRId)`:

~~~ java
String qrIdString = "...";

QRId id = new QRId(qrIdString);

QRUrl response = client.getQRInfo(id);

// place your code here
~~~

Ответ

~~~
{
  "qrId": "AD100004BAL7227F9BNP6KNE007J9B3K",
  "payload": "https://qr.nspk.ru/AD100004BAL7227F9BNP6KNE007J9B3K?type=02&bank=100000000007&sum=1&cur=RUB&crc=AB75",
  "qrUrl": "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/AD100004BAL7227F9BNP6KNE007J9B3K/image"
}
~~~

## Получение информации по платежу


Необходимо создать объект класса `QRId`, передав в конструкторе идентификатор QR-кода, и вызвать метод `getPaymentInfo(QRId)`:

~~~ java
String qrIdString = "...";

QRId id = new QRId(qrIdString);

PaymentInfo response = client.getPaymentInfo(id);

// place your code here
~~~

Ответ:

~~~
{
  "additionalInfo": "Доп информация",
  "amount": 12399,
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

## Оформление возврата по платежу

Для возврата средств необходимо создать объект класса `RefundInfo`, заполнив необходимые поля, и вызвать метод `refundPayment(RefundInfo)`. Подробности об обязательных полях в [документации](https://e-commerce.raiffeisen.ru/api/doc/sbp.html#operation/registerUsingPOST_1 "Документация к API").

Обязательные параметры:
- сумма возврата в рублях `amount(BigDecimal)`
- идентификатор заказа платежа в Райффайзенбанке, используется для возвратов по динамическому QR `order(String)`
- уникальный идентификатор запроса на возврат`refundId(String)`

~~~ java
BigDecimal moneyAmount = new BigDecimal(150);
String orderInfo = "...";
String refundId = "...";
long transactionId = ...;

RefundInfo refundInfo = new RefundInfo(moneyAmount, orderInfo, refundId);

RefundStatus response = client.refundPayment(refundInfo);

// place your code here
~~~

Ответ:

~~~
{
  "amount": 150,
  "refundStatus": "IN_PROGRESS"
}
~~~

## Получение информации по возврату

Для выполнения данного запроса необходимо указать уникальный идентификатор запроса на возврат `refundId` при вызове метода `getRefundInfo(refundId)`:

~~~ java
String refundIdString = "...";
RefundId refundId = new RefundId(refundIdString);

RefundStatus response = client.getRefundInfo(refundId);

// place your code here
~~~

Ответ:

~~~
{
  "amount": 150,
  "refundStatus": "IN_PROGRESS"
}
~~~

## Создание заказа

Для создания заказа необходимо создать экземпляр класса `Order`

Обязательные параметры:
- Сумма в рублях `amount`

Опциональные параметры могут быть заполнены с помощью set методов.

Для выполнения запроса необходимо вызвать соответствующий метод класса `SbpClient`, принимающий в качестве аргумента объект класса `Order`:

~~~ java

Order order = Order.builder().amount(BigDecimal.ZERO).build();
OrderInfo response = TestUtils.CLIENT.createOrder(order);

~~~


Также существует возможность заполнить необязательный параметр `qrExpirationDate` с помощью сдвига относительно даты создания. Для этого в поле `qrExpirationDate` следует указать строку вида `"+<число1><буква1>"`.

- 'M' - месяц
- 'd' - день
- 'H' - час
- 'm' - минута
- 's' - секунда

Пример:

~~~ java
Order order = Order.builder().amount(BigDecimal.ZERO).expirationDate("+5m").build(); // + 5 minutes

Order order = Order.builder().amount(BigDecimal.ZERO).expirationDate("+1d5m").build(); // + 1 day 5 minutes

~~~

Также существует возможность заполнить необязательный параметр `extra`:
Для этого нужно создать экземпляр класса OrderExtra c параметрами apiClient и apiClientVersion

Пример:

~~~ java
OrderExtra orderExtra = new OrderExtra("apiClient", "1.0.2");
Order order = Order.builder().amount(BigDecimal.ZERO).extra(orderExtra).build();
~~~

Также существует возможность заполнить необязательный параметр `qr`:
Для этого нужно создать экземпляр класса OrderQr

OrderQr:
- id (required, Параметр должен быть заполнен идентификатором QR, который был получен в ответе на запрос регистрации QR-кода)
- additionalInfo
- paymentDetails


Пример:

~~~ java
QRVariable qrVariable = new QRVariable();
QRUrl qr = TestUtils.CLIENT.registerQR(qrVariable);
OrderQr orderQr = new OrderQr(qr.getQrId());

Order order = Order.builder().amount(new BigDecimal(314)).qr(orderQr).build();
OrderInfo response = TestUtils.CLIENT.createOrder(order);
~~~

Также существует возможность заполнить необязательный параметр `id`:
Это уникальный идентификатор заказа. Рекомендуется использовать формат, не допускающий возможность перебора, например, UUID v4
Если параметр не передан, то идентификатор присвоится заказу автоматически

Пример:

~~~ java
Order order = Order.builder().amount(new BigDecimal(314)).id("QUAaOlCRU0Bdub8J4TeEpddUacwZmCIv221208").build();
~~~

Также существует возможность заполнить необязательный параметр `comment`:
Комментарий к заказу

Пример:

~~~ java
Order order = Order.builder().amount(new BigDecimal(314)).comment("Комментарий к заказу")).build();
~~~



## Обработка уведомлений

Для хранения и использования уведомлений существует класс `PaymentNotification`, экземпляр которого можно получить с помощью статического метода `SbpUtil.parseJson(String)`.

Для проверки подлинности уведомления существуют перегруженные статические методы `SbpUtil.checkNotificationSignature`. Примеры использования:

~~~ java
String jsonString = "...";
String apiSignature = "...";
String secretKey = "...";

boolean success = SbpUtil.checkNotificationSignature(jsonString, apiSignature, secretKey);
~~~

~~~ java
PaymentNotification notification = SbpUtil.parseJson(jsonString);
String apiSignature = "...";
String secretKey = "...";

boolean success = SbpUtil.checkNotificationSignature(notification, apiSignature, secretKey);
~~~

~~~ java
BigDecimal amount = ...;
String sbpMerchantId = "...";
String order = "...";
String paymentStatus = "...";
String transactionDate = "...";

String apiSignature = "...";
String secretKey = "...";

boolean success = SbpUtil.checkNotificationSignature(amount, 
                 	sbpMerchantId, 
                 	order,
                 	paymentStatus,
                 	transactionDate,
                 	apiSignature,
                 	secretKey);
~~~

## Использование альтернативного HTTP-клиента

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
SbpClient client = new SbpClient(SbpClient.PRODUCTION_URL, sbpMerchantId, secretKey, customClient); 
~~~

## Шпаргалка

| Тип запроса | Вызываемый метод | Принимаемый класс | Возвращаемый класс|
| --- |---|---| ---|
| Регистрация QR-кода |`registerQR`| `QR` |`QRUrl`  |
|Получение данных по зарегистрированному ранее QR-коду|`getQRInfo`|`QRId`|`QRUrl`|
|Получение информации по платежу|`getPaymentInfo`|`QRId`|`PaymentInfo`|
|Оформление возврата по платежу|`refundPayment`|`RefundInfo`|`RefundStatus`|
|Получение информации по возврату|`getRefundInfo`|`RefundId`|`RefundStatus`|