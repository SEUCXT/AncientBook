package com.seu.architecture.service;

import com.seu.architecture.model.*;

import java.util.List;
import java.util.Set;

public interface UserService {

    ViewObject register(UserRegisterRequest request);

    ViewObject login(String username, String password, boolean rememberMe);

    ViewObject setRolesforUser(String username, String roleName);

    ViewObject addUser(String username, String password, String roleName);

    ViewObject modifyUserStatus(String username, String status);

    ViewObject addAdmin(String username);

    ViewObject deleteAdmin(String username);

    ViewObject logout(String username);

    ViewObject listUser();

    ViewObject addPermissions(Permission permission);

    ViewObject addRole(Role role);

    Set<Role> getRolesByUsername(String username);

    User getUserByUsername(String username);

    List<User> insertUsers(List<User> users);

    ViewObject listPermissions();

    ViewObject listRoles();

    ViewObject allocatePermissionsForRole(String roleName, String permissions);

    ViewObject passwordModify(String username, String password);

}

