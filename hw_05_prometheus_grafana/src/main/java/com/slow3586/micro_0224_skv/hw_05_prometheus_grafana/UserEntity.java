package com.slow3586.micro_0224_skv.hw_05_prometheus_grafana;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
@NoArgsConstructor
public class UserEntity {
    @Id Long id;
    String username;
    String firstName;
    String lastName;
    String email;
    String phone;
}
