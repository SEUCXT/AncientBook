package com.seu.architecture.service;

import com.seu.architecture.model.Permission;
import com.seu.architecture.model.Role;

import java.util.Set;

/**
 * Created by 17858 on 2017/5/11.
 */
public interface RoleService {

    void createRole(String name, String display_name, String description);

    void deleteRole(String name);

    Role getRoleByRolename(String name);

    void correlationgPermissions(Role role, Set<Permission> permissionSet);

    void uncorrelationPermisssions(Role role, Set<Permission> permissionSet);

}
