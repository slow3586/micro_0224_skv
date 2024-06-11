package com.slow3586.micro_0224_skv.hw_06_security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class JwtComponent {
    JwtProperties jwtProperties;

    public Mono<String> generateTokenForSubject(@NonNull Object id) {
        return Mono.just(
            Jwts.builder()
                .subject(id.toString())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * jwtProperties.getExpirationMinutes()))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getToken())))
                .compact());
    }

    public Mono<String> getTokenSubject(String token) {
        return Mono.just(token)
            .filter(StringUtils::isNotBlank)
            .map(t -> Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getToken())))
                .build()
                .parseSignedClaims(token)
                .getPayload())
            .filter(claims -> claims.getExpiration().after(new Date()))
            .map(Claims::getSubject);
    }
}
