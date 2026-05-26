# telegram-gateway-java

[![Maven Central](https://img.shields.io/maven-central/v/io.github.javoxirjava/telegram-gateway-spring-boot-starter.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.github.javoxirjava/telegram-gateway-spring-boot-starter/1.0.0/jar)

Production-ready Java SDK for **Telegram Gateway API** (phone verification / OTP), with:
- `telegram-gateway-core` for plain Java usage
- `telegram-gateway-spring-boot-starter` for Spring Boot 3.x auto-configuration

> This library targets **Telegram Gateway API**, not Telegram Bot API.

## What is Telegram Gateway API?
Telegram Gateway API is designed for phone number verification workflows. This SDK wraps the main verification endpoints and callback signature verification.

Supported methods:
- `sendVerificationMessage`
- `checkSendAbility`
- `checkVerificationStatus`
- `revokeVerificationMessage`

Base URL:
- `https://gatewayapi.telegram.org`

## Installation (Maven Central)
This library is available from Maven Central.

No JitPack repository is required anymore.

### Maven dependency: core module

```xml
<dependency>
  <groupId>io.github.javoxirjava</groupId>
  <artifactId>telegram-gateway-core</artifactId>
  <version>1.0.0</version>
</dependency>
```

### Maven dependency: Spring Boot starter

```xml
<dependency>
  <groupId>io.github.javoxirjava</groupId>
  <artifactId>telegram-gateway-spring-boot-starter</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Quick usage (Spring Boot)

`application.yml`

```yaml
telegram:
  gateway:
    token: ${TELEGRAM_GATEWAY_TOKEN}
    timeout: 10s
```

Service example:

```java
package com.example.auth;

import io.github.javoxir.telegram.gateway.TelegramGatewayClient;
import io.github.javoxir.telegram.gateway.dto.request.CheckVerificationStatusRequest;
import io.github.javoxir.telegram.gateway.dto.request.SendVerificationRequest;
import io.github.javoxir.telegram.gateway.dto.response.CheckVerificationStatusResponse;
import io.github.javoxir.telegram.gateway.dto.response.SendVerificationResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthVerificationService {

    private final TelegramGatewayClient telegramGatewayClient;

    public AuthVerificationService(TelegramGatewayClient telegramGatewayClient) {
        this.telegramGatewayClient = telegramGatewayClient;
    }

    public String sendCode(String phoneNumber) {
        SendVerificationResponse response = telegramGatewayClient.sendVerificationMessage(
            SendVerificationRequest.builder()
                .phoneNumber(phoneNumber)
                .codeLength(6)
                .ttl(300)
                .build()
        );

        return response.getRequestId();
    }

    public boolean isVerified(String requestId, String code) {
        CheckVerificationStatusResponse response = telegramGatewayClient.checkVerificationStatus(
            CheckVerificationStatusRequest.builder()
                .requestId(requestId)
                .code(code)
                .build()
        );

        return response.isVerified();
    }
}
```

## Plain Java usage

```java
import io.github.javoxir.telegram.gateway.DefaultTelegramGatewayClient;
import io.github.javoxir.telegram.gateway.TelegramGatewayClient;
import io.github.javoxir.telegram.gateway.TelegramGatewayConfig;
import io.github.javoxir.telegram.gateway.dto.request.SendVerificationRequest;

TelegramGatewayConfig config = TelegramGatewayConfig.builder()
    .token(System.getenv("TELEGRAM_GATEWAY_TOKEN"))
    .build();

TelegramGatewayClient client = new DefaultTelegramGatewayClient(config);

client.sendVerificationMessage(
    SendVerificationRequest.builder()
        .phoneNumber("+998901234567")
        .codeLength(6)
        .ttl(300)
        .build()
);
```

## Callback signature verification example

Use raw request body exactly as received.

```java
import io.github.javoxir.telegram.gateway.TelegramGatewaySignatureVerifier;

TelegramGatewaySignatureVerifier verifier = new TelegramGatewaySignatureVerifier();
boolean valid = verifier.verify(token, rawBody, signatureHeader);

// or throw on invalid
verifier.verifyOrThrow(token, rawBody, signatureHeader);
```

## Configuration properties (Spring Boot starter)

| Property | Type | Default | Description |
|---|---|---|---|
| `telegram.gateway.enabled` | `boolean` | `true` | Enable/disable auto-configuration |
| `telegram.gateway.token` | `String` | - | Telegram Gateway API token |
| `telegram.gateway.base-url` | `String` | `https://gatewayapi.telegram.org` | API base URL |
| `telegram.gateway.timeout` | `Duration` | `10s` | HTTP timeout |

`TelegramGatewayClient` auto-configures only when `telegram.gateway.token` is provided and `telegram.gateway.enabled=true`.

## Error handling example

```java
import io.github.javoxir.telegram.gateway.exception.TelegramGatewayApiException;
import io.github.javoxir.telegram.gateway.exception.TelegramGatewayHttpException;
import io.github.javoxir.telegram.gateway.exception.TelegramGatewayValidationException;

try {
    client.sendVerificationMessage(request);
} catch (TelegramGatewayValidationException ex) {
    // input validation issue
} catch (TelegramGatewayApiException ex) {
    // API returned ok=false
} catch (TelegramGatewayHttpException ex) {
    // transport / status code issue
}
```

## License

MIT
