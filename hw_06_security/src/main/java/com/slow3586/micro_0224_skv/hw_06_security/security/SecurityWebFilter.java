package com.slow3586.micro_0224_skv.hw_06_security.security;

import com.slow3586.micro_0224_skv.hw_06_security.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityWebFilter implements WebFilter {
    static String BEARER_PREFIX = "Bearer ";
    static String HEADER_NAME = "Authorization";
    JwtComponent jwtComponent;
    UserRepository userRepository;

    @Override
    @NonNull
    public Mono<Void> filter(
        final ServerWebExchange exchange,
        @NonNull final WebFilterChain chain
    ) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().get(HEADER_NAME))
            .flatMapMany(Flux::fromIterable)
            .singleOrEmpty()
            .filter(s -> StringUtils.startsWith(s, BEARER_PREFIX))
            .mapNotNull(s -> s.substring(BEARER_PREFIX.length()))
            .flatMap(jwtComponent::getTokenSubject)
            .map(Long::parseLong)
            .publishOn(Schedulers.boundedElastic())
            .filter(userRepository::existsById)
            .map(userId -> UsernamePasswordAuthenticationToken.authenticated(
                userId,
                null,
                AuthorityUtils.createAuthorityList("user")))
            .defaultIfEmpty(UsernamePasswordAuthenticationToken.unauthenticated(
                null,
                null))
            .map(SecurityContextImpl::new)
            .doOnError((err) -> log.error("#filter", err))
            .transform(securityContextMono -> chain
                .filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder
                    .withSecurityContext(securityContextMono)));
    }
}
