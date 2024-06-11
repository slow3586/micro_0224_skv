package com.slow3586.micro_0224_skv.hw_05_prometheus_grafana;

import com.slow3586.micro_0224_skv.api.UserApiDelegate;
import com.slow3586.micro_0224_skv.api.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RestService implements UserApiDelegate {
    UserRepository userRepository;
    UserMapper userMapper;
    static Random random = new Random();

    @Override
    public ResponseEntity<Void> createUser(User user) {
        sleepRandom();
        if (user == null) {
            throw new IllegalArgumentException("Illegal user");
        }
        if (user.getId() != null) {
            throw new IllegalArgumentException("ID should be null");
        }
        final UserEntity save = userRepository.save(userMapper.userToUserEntity(user));
        log.info("User created: {}", save.getId());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
        sleepRandom();
        if (userId == null || userId < 1) {
            throw new IllegalArgumentException("Illegal ID");
        }
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Does not exist");
        }
        userRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<User> findUserById(Long userId) {
        sleepRandom();
        if (userId == null || userId < 1) {
            throw new IllegalArgumentException("Illegal ID");
        }
        return ResponseEntity.ok(
            userMapper.userEntityToUser(
                userRepository.findById(userId)
                    .orElseThrow(() ->
                        new IllegalArgumentException("Not found"))));
    }

    @Override
    public ResponseEntity<Void> updateUser(Long userId, User user) {
        sleepRandom();
        if (userId == null || userId < 1) {
            throw new IllegalArgumentException("Illegal ID");
        }
        if (user == null) {
            throw new IllegalArgumentException("Illegal user");
        }
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Does not exist");
        }
        userRepository.save(userMapper.userToUserEntity(user));
        return ResponseEntity.ok().build();
    }

    private void sleepRandom() {
        try {
            Thread.sleep(1 + random.nextInt(100));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
