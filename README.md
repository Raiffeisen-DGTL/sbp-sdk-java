## Примеры
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

Чтобы получить ответ от сервера, требуется вызвать метод `SbpClient.registerQR(String, QRInfo)`.
Первый параметр - URL :
- для реального использования `SbpClient.PRODUCTION_DOMAIN`
- для тестирования `SbpClient.TEST_DOMAIN`

Второй параметр - информация о QR-коде.
~~~ java
//test
Response testResponse = SbpClient.registerQR(SbpClient.TEST_DOMAIN, exampleQR);

//prod
Response prodResponse = SbpClient.registerQR(SbpClient.PRODUCTION_DOMAIN, exampleQR);
~~~
