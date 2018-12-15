package com.seu.architecture.service.impl;

import com.seu.architecture.model.Permission;
import com.seu.architecture.model.Role;
import com.seu.architecture.repository.PermissionRepository;
import com.seu.architecture.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 17858 on 2017/5/11.
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public void createPermission(String name, String display_name, String description){

        Permission permission = new Permission();
        permission.setName(name);
        permission.setDisplayName(display_name);
        permission.setDescription(description);
        permissionRepository.save(permission);
    }

    @Override
    public void deletePermission(String name){

        Permission permission = permissionRepository.findByname(name);
        permissionRepository.delete(permission);
    }

}
