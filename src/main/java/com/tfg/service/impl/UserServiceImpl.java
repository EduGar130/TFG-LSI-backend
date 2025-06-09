package com.tfg.service.impl;

import com.tfg.entity.User;
import com.tfg.repository.RoleRepository;
import com.tfg.repository.UserRepository;
import com.tfg.repository.WarehouseRepository;
import com.tfg.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, WarehouseRepository warehouseRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findByValido(true);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Long getWarehouseIdByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null && user.getWarehouse() != null) {
            return user.getWarehouse().getId();
        }
        return null;
    }

    @Override
    public User createUser(User user) {
        if(user.getWarehouse() != null) {
            user.setWarehouse(warehouseRepository.findById(user.getWarehouse().getId()).orElse(null));
        }
        Long roleId = user.getRole().getId();
        user.setId(null);
        user.setRole(roleRepository.findById(roleId).orElse(null));
        String passwordHash = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        Long warehouseId = user.getWarehouse().getId();
        Long roleId = user.getRole().getId();
        user.setWarehouse(warehouseRepository.findById(warehouseId).orElse(null));
        user.setRole(roleRepository.findById(roleId).orElse(null));
        String passwordHash = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.setUserInvalidoById(id);
    }

    @Override
    public String getPermissionsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            return user.getRole().getPermissions();
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
