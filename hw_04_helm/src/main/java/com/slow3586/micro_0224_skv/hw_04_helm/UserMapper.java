package com.slow3586.micro_0224_skv.hw_04_helm;

import com.slow3586.micro_0224_skv.api.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userEntityToUser(UserEntity userEntity);
    UserEntity userToUserEntity(User user);
}
