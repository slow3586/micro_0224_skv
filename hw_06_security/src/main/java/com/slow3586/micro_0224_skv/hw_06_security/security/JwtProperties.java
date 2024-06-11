package com.slow3586.micro_0224_skv.hw_06_security.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.security")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtProperties {
    String token;
    long expirationMinutes;
}
