package com.slow3586.micro_0224_skv.hw_04_helm;

import com.slow3586.micro_0224_skv.api.UserApiDelegate;
import com.slow3586.micro_0224_skv.api.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class RestService implements UserApiDelegate {
    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public ResponseEntity<Void> createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Illegal user");
        }
        if (user.getId() != null) {
            throw new IllegalArgumentException("ID should be null");
        }
        userRepository.save(userMapper.userToUserEntity(user));
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long userId) {
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
}
