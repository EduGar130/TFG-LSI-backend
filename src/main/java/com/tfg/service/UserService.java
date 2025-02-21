package com.tfg.service;

import com.tfg.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);

    Long getWarehouseIdByUsername(String username);

    User createUser(User user);

    User updateUser(User user);

    void deleteUserById(Long id);

    String getPermissionsByUsername(String username);
}
