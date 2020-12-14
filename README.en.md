# Java SDK for SBP API integration
## Table of contents 
- [Connection](#connection)
- [Usage](#usage)
- [QR code registration](#qr-code-registration)
- [Get info for registered QR code](#get-info-for-qr-code-registered)
- [Get payment info](#get-payment-info)
- [Create refund for payment](#create-refund-for-payment)
- [Get refund info](#get-refund-info)
- [Notifications processing](#notifications-processing)
- [Alternative http client usage](#alternative-http-client-usage)
- [Annex](#annex)

## Connection

Requirements:
- Java 11+
- Apache Maven

Please do the following to connect SDK:
- Create `dependencies` folder in the project root directory  
- Put .jar and [pom.xml](/docs/dependencies/pom.xml) to the created folder
- Put the following into __your__ project pom.xml:
~~~
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>raiffeisen</groupId>
            <artifactId>sbp-sdk-java</artifactId>
            <version>1.0.0</version>
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
                    <version>1.0.0</version>
                    <packaging>jar</packaging>
                    <file>dependencies/sbp-sdk-java-1.0.0.jar</file>
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
- Run the following maven commands:
   + validate(`mvn validate`)
   + install(`mvn install`)

## Usage

To use the SDK, the instance of `SbpClient` class with the following constructor parameters should be created:
- URL where requests will be sent (`SbpClient.PRODUCTION_URL` or `SbpClient.TEST_URL`)
- Merchant id in the SBP system
- Authorisation secret key

Created `SbpClient` class will be used for all further API interactions, please refer to [annex](#annex) for return values examples.

The client returns the following exceptions:
- `IOException` - network error
- `SbpException` - logical error during request processing attempt
- `ContractViolationException` - error processing response from server

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
            qrCode.setAdditionalInfo("Additional info");
            qrCode.setPaymentDetails("Payment details");
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

## QR code registration

To register codes you should create either `QRStatic` or `QRDynamic` class instance. The required fields set for each class is different.

Required fields:
- `order(String)` - order number in partner system
- `amount(BigDecimal)` - required for `QRDynamic`, optional for `QRStatic`

Optional parameters can be set by invoking setter methods.

In order to execute the request, corresponding method of `SbpClient` class should be invoked with `QR` class object as an argument:
~~~ java
String order = QRUtils.generateOrderNumber();

// save order in a database;

QRDynamic qrCode = new QRDynamic(order, new BigDecimal(100));
qrCode.setAccount("40700000000000000000");
qrCode.setAdditionalInfo("Additional info");
qrCode.setPaymentDetails("Payment details");
qrCode.setQrExpirationDate("2023-07-22T09:14:38.107227+03:00");

QRUrl response = client.registerQR(qrCode);

// place your code here
~~~

Response:

~~~
{
  "qrId": "AD100004BAL7227F9BNP6KNE007J9B3K",
  "payload": "https://qr.nspk.ru/AD100004BAL7227F9BNP6KNE007J9B3K?type=02&bank=100000000007&sum=1&cur=RUB&crc=AB75",
  "qrUrl": "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/AD100004BAL7227F9BNP6KNE007J9B3K/image"
}
~~~

Minimal data example:

~~~ java
String order = QRUtils.generateOrderNumber(); // UUID_v4

// save order in a database;

// QRStatic qrStatic = new QRStatic(order);
//
// or
//
// QRDynamic qrDynamic = new QRDynamic(order, new BigDecimal(100));
~~~

Also it is possible to set optional `qrExpirationDate` parameter by using creation date offset. To do so, string like `"+<number1><letter1>"` in the `qrExpirationDate` should be specified.

- 'M' - months
- 'd' - days
- 'H' - hours
- 'm' - minutes
- 's' - seconds

Here is the example:

~~~ java
String order = QRUtils.generateOrderNumber(); // UUID_v4

// save order in a database;

QRStatic qrStatic = new QRStatic(order);
qrStatic.setQrExpirationDate("+5m"); // + 5 minutes

QRDynamic qrDynamic = new QRDynamic(order, new BigDecimal(100));
qrDynamic.setQrExpirationDate("+1d5m"); // + 1 day 5 minutes
~~~

## Get info for QR code registered

To get info about QR code you should create `QRId` class instance passing QR identifier to its constructor, then invoke `getQRInfo(QRId)` method:
~~~ java
String qrIdString = "...";

QRId id = new QRId(qrIdString);

QRUrl response = client.getQRInfo(id);

// place your code here
~~~

Response

~~~
{
  "qrId": "AD100004BAL7227F9BNP6KNE007J9B3K",
  "payload": "https://qr.nspk.ru/AD100004BAL7227F9BNP6KNE007J9B3K?type=02&bank=100000000007&sum=1&cur=RUB&crc=AB75",
  "qrUrl": "https://e-commerce.raiffeisen.ru/api/sbp/v1/qr/AD100004BAL7227F9BNP6KNE007J9B3K/image"
}
~~~

## Get payment info

To get payment info you should create `QRId` class instance passing QR identifier to its constructor, then invoke `getPaymentInfo(QRId)` method:
~~~ java
String qrIdString = "...";

QRId id = new QRId(qrIdString);

PaymentInfo response = client.getPaymentInfo(id);

// place your code here
~~~

Response:

~~~
{
  "additionalInfo": "Additional info",
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

## Create refund for payment

To refund payment you should create the instance of `RefundInfo` class with required fields, then invoke `refundPayment(RefundInfo)` method. Detailed information about required fields is available at our [russian documentation page](https://e-commerce.raiffeisen.ru/api/doc/sbp.html#operation/registerUsingPOST_1). 

Required parameters:
- `amount(BigDecimal)` - refund amount
- `order(String)` - order identifier for payment in Raiffeisenbank, used for dynamic QR refunds
- `refundId(String)` - unique refund identifier 

~~~ java
BigDecimal moneyAmount = new BigDecimal(150);
String orderInfo = "...";
String refundId = "...";
long transactionId = ...;

RefundInfo refundInfo = new RefundInfo(moneyAmount, orderInfo, refundId);

RefundStatus response = client.refundPayment(refundInfo);

// place your code here
~~~

Response:

~~~
{
  "amount": 150,
  "refundStatus": "IN_PROGRESS"
}
~~~

## Get refund info

For executing this request you should specify unique refund identifier `refundId` invoking method `getRefundInfo(refundId)`:

~~~ java
String refundIdString = "...";
RefundId refundId = new RefundId(refundIdString);

RefundStatus response = client.getRefundInfo(refundId);

// place your code here
~~~

Response:

~~~
{
  "amount": 150,
  "refundStatus": "IN_PROGRESS"
}
~~~

## Notifications processing

The `PaymentNotification` class is used for operating notifications. 

You can get its instance by invoking static `SbpUtil.parseJson(String)` method.

Static overloaded `SbpUtil.checkNotificationSignature` methods are used for notifications authentication. Below are the examples:
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

## Alternative http client usage

Apache http client (`ApacheClient` class) is used by default. To change the client you should implement `WebClient` interface:
~~~ java
public interface WebClient extends Closeable {
    Response request(String method, 
			String url, 
			Map<String, String> headers, 
			String entity) throws IOException;
}

~~~

Usage example:

~~~ java
CustomWebClient customClient = ...;
SbpClient client = new SbpClient(SbpClient.PRODUCTION_URL, sbpMerchantId, secretKey, customClient); 
~~~

## Annex

| Request type | Method invoked | Class accepted | Class returned|
| --- |---|---| ---|
| QR code registration |`registerQR`| `QR` |`QRUrl`  |
|Get info for registered QR code|`getQRInfo`|`QRId`|`QRUrl`|
|Get payment info|`getPaymentInfo`|`QRId`|`PaymentInfo`|
|Create refund for payment|`refundPayment`|`RefundInfo`|`RefundStatus`|
|Get refund info|`getRefundInfo`|`RefundId`|`RefundStatus`|