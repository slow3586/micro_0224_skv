package com.slow3586.micro_0224_skv.hw_06_security;

import com.slow3586.micro_0224_skv.api.UserApiDelegate;
import com.slow3586.micro_0224_skv.api.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserApiDelegate {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("!isAuthenticated()")
    public Mono<ResponseEntity<Void>> createUser(
        final Mono<User> user,
        final ServerWebExchange exchange
    ) {
        return user
            .filter(Objects::nonNull)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Illegal user")))
            .filter(u -> u.getId() == null)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("ID should be null")))
            .map(userMapper::toEntity)
            .map(u -> {
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                return u;
            })
            .publishOn(Schedulers.boundedElastic())
            .map(userRepository::save)
            .doOnNext(u -> log.info("User created: {}", u.getId()))
            .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    @PreAuthorize("#userId.toString() == authentication.name || hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> deleteUser(
        final Long userId,
        final ServerWebExchange exchange
    ) {
        return Mono.justOrEmpty(userId)
            .filter(id -> userId != null && userId >= 1)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Illegal ID")))
            .publishOn(Schedulers.boundedElastic())
            .filter(id -> userRepository.existsById(userId))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Does not exist")))
            .doOnNext(userRepository::deleteById)
            .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("#userId.toString() == authentication.name || hasRole('ADMIN')")
    public Mono<ResponseEntity<User>> findUserById(
        final Long userId,
        final ServerWebExchange exchange
    ) {
        return Mono.justOrEmpty(userId)
            .filter(id -> userId != null && userId >= 1)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Illegal ID")))
            .publishOn(Schedulers.boundedElastic())
            .map(userRepository::findById)
            .flatMap(Mono::justOrEmpty)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Not found")))
            .mapNotNull(userMapper::toDto)
            .mapNotNull(ResponseEntity::ok);
    }

    @Override
    @PreAuthorize("#userId.toString() == authentication.name || hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> updateUser(
        final Long userId,
        final Mono<User> user,
        final ServerWebExchange exchange
    ) {
        return user
            .filter(Objects::nonNull)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Illegal user")))
            .map(u -> {
                u.setId(userId);
                return u;
            })
            .filter(u -> u.getId() != null && u.getId() >= 1)
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Illegal ID")))
            .publishOn(Schedulers.boundedElastic())
            .filter(u -> userRepository.existsById(u.getId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Not found")))
            .map(userMapper::toEntity)
            .map(u -> {
                u.setPassword(passwordEncoder.encode(u.getPassword()));
                return u;
            })
            .map(userRepository::save)
            .thenReturn(ResponseEntity.ok().build());
    }
}
