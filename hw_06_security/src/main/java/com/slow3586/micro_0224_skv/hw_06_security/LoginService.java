package com.slow3586.micro_0224_skv.hw_06_security;

import com.slow3586.micro_0224_skv.api.LoginApiDelegate;
import com.slow3586.micro_0224_skv.api.model.LoginRequest;
import com.slow3586.micro_0224_skv.hw_06_security.security.JwtComponent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class LoginService implements LoginApiDelegate {
    UserRepository userRepository;
    JwtComponent jwtComponent;
    PasswordEncoder passwordEncoder;

    @Override
    public Mono<ResponseEntity<String>> login(
        final Mono<LoginRequest> loginRequest,
        final ServerWebExchange exchange
    ) {
        return loginRequest
            .publishOn(Schedulers.boundedElastic())
            .map(request -> userRepository.findByUsername(request.getUsername())
                .filter(user -> passwordEncoder.matches(
                    request.getPassword(),
                    user.getPassword())))
            .flatMap(Mono::justOrEmpty)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Incorrect login data")))
            .flatMap(user -> jwtComponent.generateTokenForSubject(user.getId()))
            .mapNotNull(ResponseEntity::ok)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Could not log in")));
    }
}
