package com.slow3586.micro_0224_skv.hw_06_security;

import com.slow3586.micro_0224_skv.api.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDto(UserEntity userEntity);
    UserEntity toEntity(User user);
}
