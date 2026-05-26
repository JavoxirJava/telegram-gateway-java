package io.github.javoxir.telegram.gateway.spring;

import io.github.javoxir.telegram.gateway.DefaultTelegramGatewayClient;
import io.github.javoxir.telegram.gateway.TelegramGatewayClient;
import io.github.javoxir.telegram.gateway.TelegramGatewayConfig;
import io.github.javoxir.telegram.gateway.TelegramGatewaySignatureVerifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(TelegramGatewayClient.class)
@ConditionalOnProperty(prefix = "telegram.gateway", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(TelegramGatewayProperties.class)
public class TelegramGatewayAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "telegram.gateway", name = "token")
    public TelegramGatewayClient telegramGatewayClient(TelegramGatewayProperties properties) {
        TelegramGatewayConfig config = TelegramGatewayConfig.builder()
            .token(properties.getToken())
            .baseUrl(properties.getBaseUrl())
            .timeout(properties.getTimeout())
            .build();
        return new DefaultTelegramGatewayClient(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public TelegramGatewaySignatureVerifier telegramGatewaySignatureVerifier() {
        return new TelegramGatewaySignatureVerifier();
    }
}
