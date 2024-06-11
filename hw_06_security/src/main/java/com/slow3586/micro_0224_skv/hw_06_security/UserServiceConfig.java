package com.slow3586.micro_0224_skv.hw_06_security;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@Configuration
@EnableWebFlux
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@Slf4j
@SecurityScheme(
    name = "BearerAuth",
    scheme = "bearer",
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER
)
public class UserServiceConfig {
    public void config() {
        Hooks.onNextError((err, obj) -> {
            log.error("#hook: {}", obj, err);
            return err;
        });
    }
}
