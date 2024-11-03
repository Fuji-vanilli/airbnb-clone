package com.fuji.airbnb_clone_backend.user.mapper;

import com.fuji.airbnb_clone_backend.user.application.dto.ReadUserDTO;
import com.fuji.airbnb_clone_backend.user.domain.Authority;
import com.fuji.airbnb_clone_backend.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO readUserToUserDTO(User user);

    default String mapAuthoritiesToString(Authority authority) {
        return authority.getName();
    }
}
