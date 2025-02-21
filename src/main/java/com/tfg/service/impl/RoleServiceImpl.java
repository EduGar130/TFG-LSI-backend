package com.tfg.service.impl;

import com.tfg.dto.RoleDTO;
import com.tfg.entity.Role;
import com.tfg.mapper.InventoryMapper;
import com.tfg.mapper.RoleMapper;
import com.tfg.repository.RoleRepository;
import com.tfg.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        return RoleMapper.toDto(roleRepository.findById(id).get());
    }

    @Override
    public RoleDTO createRole(Role role) {
        return RoleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public RoleDTO updateRole(Role role) {
        return RoleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public void deleteRoleById(Long id) {
        roleRepository.findById(id).ifPresent(roleRepository::delete);
    }
}
