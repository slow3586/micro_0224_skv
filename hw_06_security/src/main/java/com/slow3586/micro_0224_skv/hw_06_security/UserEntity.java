package com.slow3586.micro_0224_skv.hw_06_security;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
@NoArgsConstructor
public class UserEntity {
    @Id Long id;
    String password;
    String username;
    String firstName;
    String lastName;
    String email;
    String phone;
}
