package com.seu.architecture.service.impl;

import com.seu.architecture.model.Permission;
import com.seu.architecture.model.Role;
import com.seu.architecture.repository.RoleRepository;
import com.seu.architecture.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;

/**
 * Created by 17858 on 2017/5/11.
 */
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void createRole(String name, String display_name, String description) {

        Role role = new Role();
        role.setName(name);
        role.setDisplayName(display_name);
        role.setDescription(description);
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(String name){
        Role role = roleRepository.findByName(name);
        roleRepository.delete(role);
    }

    @Override
    public void correlationgPermissions(Role role, Set<Permission> permissionSet){
        Set<Permission> permissions = role.getPermissions();
        for(Permission permission:permissionSet){
            permissions.add(permission);
        }
        role.setPermissions(permissions);
    }

    @Override
    public void uncorrelationPermisssions(Role role, Set<Permission> permissionSet){
        Set<Permission> permissions = role.getPermissions();
        for(Permission permission:permissionSet){
            permissions.remove(permission);
        }
        role.setPermissions(permissions);
    }

    @Override
    public Role getRoleByRolename(String name){
        return roleRepository.findByName(name);
    }
}
