package com.catalog.catalog.service;

import com.catalog.catalog.dto.UserDto;
import com.catalog.catalog.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    User getCurrentUser();



}
