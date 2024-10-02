package com.catalog.catalog.mapper;

import com.catalog.catalog.rest.dto.UserDto;
import com.catalog.catalog.model.User;

public interface UserMapper {

    UserDto toUserDto(User user);
}