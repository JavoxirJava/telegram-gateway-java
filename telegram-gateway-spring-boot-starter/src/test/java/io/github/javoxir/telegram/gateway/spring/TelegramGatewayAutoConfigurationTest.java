package io.github.javoxir.telegram.gateway.spring;

import io.github.javoxir.telegram.gateway.TelegramGatewayClient;
import io.github.javoxir.telegram.gateway.TelegramGatewaySignatureVerifier;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class TelegramGatewayAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(TelegramGatewayAutoConfiguration.class));

    @Test
    void shouldCreateClientBeanWhenTokenProvided() {
        contextRunner
            .withPropertyValues("telegram.gateway.token=test-token")
            .run(context -> {
                assertThat(context).hasSingleBean(TelegramGatewayClient.class);
                assertThat(context).hasSingleBean(TelegramGatewaySignatureVerifier.class);
            });
    }

    @Test
    void shouldNotOverrideCustomClientBean() {
        contextRunner
            .withPropertyValues("telegram.gateway.token=test-token")
            .withUserConfiguration(CustomClientConfiguration.class)
            .run(context -> {
                assertThat(context).hasSingleBean(TelegramGatewayClient.class);
                assertThat(context.getBean(TelegramGatewayClient.class)).isSameAs(context.getBean("customClient"));
            });
    }

    @Test
    void shouldNotCreateClientBeanWhenTokenMissing() {
        contextRunner
            .run(context -> assertThat(context).doesNotHaveBean(TelegramGatewayClient.class));
    }

    @Test
    void shouldDisableAutoConfigurationWhenExplicitlyDisabled() {
        contextRunner
            .withPropertyValues(
                "telegram.gateway.enabled=false",
                "telegram.gateway.token=test-token"
            )
            .run(context -> {
                assertThat(context).doesNotHaveBean(TelegramGatewayClient.class);
                assertThat(context).doesNotHaveBean(TelegramGatewaySignatureVerifier.class);
            });
    }

    @Configuration(proxyBeanMethods = false)
    static class CustomClientConfiguration {

        @Bean("customClient")
        TelegramGatewayClient customClient() {
            return new TelegramGatewayClient() {
                @Override
                public io.github.javoxir.telegram.gateway.dto.response.SendVerificationResponse sendVerificationMessage(
                    io.github.javoxir.telegram.gateway.dto.request.SendVerificationRequest request
                ) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public io.github.javoxir.telegram.gateway.dto.response.CheckSendAbilityResponse checkSendAbility(
                    io.github.javoxir.telegram.gateway.dto.request.CheckSendAbilityRequest request
                ) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public io.github.javoxir.telegram.gateway.dto.response.CheckVerificationStatusResponse checkVerificationStatus(
                    io.github.javoxir.telegram.gateway.dto.request.CheckVerificationStatusRequest request
                ) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public io.github.javoxir.telegram.gateway.dto.response.RevokeVerificationResponse revokeVerificationMessage(
                    io.github.javoxir.telegram.gateway.dto.request.RevokeVerificationRequest request
                ) {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }
}
