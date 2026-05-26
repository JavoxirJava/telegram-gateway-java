package io.github.javoxir.telegram.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.github.javoxir.telegram.gateway.dto.request.CheckSendAbilityRequest;
import io.github.javoxir.telegram.gateway.dto.request.SendVerificationRequest;
import io.github.javoxir.telegram.gateway.dto.response.SendVerificationResponse;
import io.github.javoxir.telegram.gateway.exception.TelegramGatewayApiException;
import io.github.javoxir.telegram.gateway.exception.TelegramGatewayHttpException;
import io.github.javoxir.telegram.gateway.exception.TelegramGatewayValidationException;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultTelegramGatewayClientTest {

    @Test
    void shouldRejectInvalidPhoneNumber() {
        TelegramGatewayClient client = new DefaultTelegramGatewayClient(
            TelegramGatewayConfig.builder().token("test-token").build()
        );

        assertThrows(
            TelegramGatewayValidationException.class,
            () -> client.checkSendAbility(
                CheckSendAbilityRequest.builder().phoneNumber("998901234567").build()
            )
        );
    }

    @Test
    void shouldRejectEmptyToken() {
        assertThrows(
            TelegramGatewayValidationException.class,
            () -> TelegramGatewayConfig.builder().token(" ").build()
        );
    }

    @Test
    void shouldSerializeSnakeCaseFields() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SendVerificationRequest request = SendVerificationRequest.builder()
            .phoneNumber("+998901234567")
            .requestId("req-1")
            .senderUsername("sender")
            .codeLength(6)
            .callbackUrl("https://example.com/callback")
            .payload("payload")
            .ttl(300)
            .build();

        String json = mapper.writeValueAsString(request);

        assertTrue(json.contains("\"phone_number\""));
        assertTrue(json.contains("\"request_id\""));
        assertTrue(json.contains("\"sender_username\""));
        assertTrue(json.contains("\"code_length\""));
        assertTrue(json.contains("\"callback_url\""));
    }

    @Test
    void shouldVerifySignature() throws Exception {
        String token = "secret";
        String rawBody = "{\"request_id\":\"123\"}";

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(token.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        String signature = HexFormat.of().formatHex(mac.doFinal(rawBody.getBytes(StandardCharsets.UTF_8)));

        TelegramGatewaySignatureVerifier verifier = new TelegramGatewaySignatureVerifier();

        assertTrue(verifier.verify(token, rawBody, signature));
        assertTrue(verifier.verify(token, rawBody, "sha256=" + signature));
        assertTrue(verifier.verify(token, rawBody, "sha256=" + signature.toUpperCase()));
        assertFalse(verifier.verify(token, rawBody, "sha256=invalid_hex"));
    }

    @Test
    void shouldSendRequestWithExpectedHeadersAndPath() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        AtomicReference<String> authHeader = new AtomicReference<>();
        AtomicReference<String> contentTypeHeader = new AtomicReference<>();
        AtomicReference<String> userAgentHeader = new AtomicReference<>();
        AtomicReference<String> requestBody = new AtomicReference<>();

        server.createContext("/sendVerificationMessage", exchange -> {
            captureHeaders(exchange, authHeader, contentTypeHeader, userAgentHeader);
            requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));

            String body = """
                {
                  "ok": true,
                  "result": {
                    "request_id": "req-123",
                    "phone_number": "+998901234567",
                    "request_cost": 0.01,
                    "remaining_balance": 12.34,
                    "delivery_status": "sent",
                    "verification_status": "pending",
                    "payload": "ctx"
                  }
                }
                """;

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, body.getBytes(StandardCharsets.UTF_8).length);
            exchange.getResponseBody().write(body.getBytes(StandardCharsets.UTF_8));
            exchange.close();
        });

        try {
            server.start();
            String baseUrl = "http://localhost:" + server.getAddress().getPort();

            TelegramGatewayClient client = new DefaultTelegramGatewayClient(
                TelegramGatewayConfig.builder()
                    .token("my-token")
                    .baseUrl(baseUrl)
                    .build()
            );

            SendVerificationResponse response = client.sendVerificationMessage(
                SendVerificationRequest.builder()
                    .phoneNumber("+998901234567")
                    .codeLength(6)
                    .ttl(300)
                    .build()
            );

            assertEquals("Bearer my-token", authHeader.get());
            assertEquals("application/json", contentTypeHeader.get());
            assertTrue(userAgentHeader.get().startsWith("telegram-gateway-java/"));
            assertTrue(requestBody.get().contains("phone_number"));
            assertEquals("req-123", response.getRequestId());
            assertEquals("+998901234567", response.getPhoneNumber());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void shouldThrowApiExceptionWhenOkIsFalse() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/checkSendAbility", exchange -> {
            String body = "{\"ok\": false, \"error\": \"rate_limited\"}";
            exchange.sendResponseHeaders(200, body.getBytes(StandardCharsets.UTF_8).length);
            exchange.getResponseBody().write(body.getBytes(StandardCharsets.UTF_8));
            exchange.close();
        });

        try {
            server.start();
            String baseUrl = "http://localhost:" + server.getAddress().getPort();
            TelegramGatewayClient client = new DefaultTelegramGatewayClient(
                TelegramGatewayConfig.builder()
                    .token("my-token")
                    .baseUrl(baseUrl)
                    .build()
            );

            TelegramGatewayApiException ex = assertThrows(
                TelegramGatewayApiException.class,
                () -> client.checkSendAbility(CheckSendAbilityRequest.builder().phoneNumber("+998901234567").build())
            );
            assertEquals("rate_limited", ex.getMessage());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void shouldThrowHttpExceptionOnNon2xxStatus() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/checkSendAbility", exchange -> {
            String body = "{\"error\": \"bad_request\"}";
            exchange.sendResponseHeaders(400, body.getBytes(StandardCharsets.UTF_8).length);
            exchange.getResponseBody().write(body.getBytes(StandardCharsets.UTF_8));
            exchange.close();
        });

        try {
            server.start();
            String baseUrl = "http://localhost:" + server.getAddress().getPort();
            TelegramGatewayClient client = new DefaultTelegramGatewayClient(
                TelegramGatewayConfig.builder()
                    .token("my-token")
                    .baseUrl(baseUrl)
                    .build()
            );

            TelegramGatewayHttpException ex = assertThrows(
                TelegramGatewayHttpException.class,
                () -> client.checkSendAbility(CheckSendAbilityRequest.builder().phoneNumber("+998901234567").build())
            );
            assertEquals(400, ex.getStatusCode());
            assertTrue(ex.getResponseBody().contains("bad_request"));
        } finally {
            server.stop(0);
        }
    }

    private static void captureHeaders(
        HttpExchange exchange,
        AtomicReference<String> authHeader,
        AtomicReference<String> contentTypeHeader,
        AtomicReference<String> userAgentHeader
    ) throws IOException {
        authHeader.set(exchange.getRequestHeaders().getFirst("Authorization"));
        contentTypeHeader.set(exchange.getRequestHeaders().getFirst("Content-Type"));
        userAgentHeader.set(exchange.getRequestHeaders().getFirst("User-Agent"));
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            throw new IOException("Expected POST request");
        }
    }
}
