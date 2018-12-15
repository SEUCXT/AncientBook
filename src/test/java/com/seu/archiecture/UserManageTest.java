package com.seu.archiecture;

import com.seu.architecture.ArchitectureApplication;
import com.seu.architecture.model.Permission;
import com.seu.architecture.model.Role;
import com.seu.architecture.model.User;
import com.seu.architecture.repository.PermissionRepository;
import com.seu.architecture.repository.RoleRepository;
import com.seu.architecture.repository.UserRepository;
import com.seu.architecture.service.PermissionService;
import com.seu.architecture.service.RoleService;
import com.seu.architecture.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 17858 on 2017/5/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ArchitectureApplication.class)
public class UserManageTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    PermissionService permissionService;

    @Test
    public void RoleServiceTest(){
        //角色分为管理员和用户
        //管理员: name:admin display_name:管理员 description:最高权限
        //用户：name:user display_name:普通用户 descrtiption:普通阅读权限

        roleService.createRole("super_admin", "超级管理员","可以对管理员和用户进行管理");
        roleService.createRole("admin", "管理员","管理用户");
        roleService.createRole("advanced_user", "高级用户","以检索图书的更多信息（书名、作者、出版社之外的其他信息），可以阅览书的内容");
        roleService.createRole("middle_user","中级用户","检索图书（书名、作者、出版社、年代），并阅览书的内容");
        roleService.createRole("primary_user","初级用户","浏览目录，不能查看每本书的内容");
       // roleService.deleteRole("primary_user");
       // Role role = roleRepository.findByName("primary_user");
       // roleRepository.delete(role);
       // System.out.println(role.getName() + " " + role.getDisplayName() + " " + role.getDescription());
    }

    @Test
    public void UserRoleServiceTest(){

//        User user = userRepository.findByUsername("cxt");
//        Set<Role> roles = new HashSet<>();
//        roles.add(roleRepository.findByName("primary_user"));
//        user.setRoles(roles);
//        userRepository.save(user);

        User user = userRepository.findByUsername("seucxt");
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("admin"));
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Test
    public void RemoveUserTest(){
        User user = userRepository.findByUsername("213121464");
        userRepository.delete(user);

    }

    @Test
    public void PermissionServiceTest(){
        //用户管理列表
        permissionService.createPermission("user-add","添加用户","管理员可以添加用户");
        permissionService.createPermission("user-remove","删除用户","管理员可以删除用户");
        permissionService.createPermission("admin-add","添加管理员","超级管理员可以添加管理员");
        permissionService.createPermission("admin-remove","取消管理员","超级管理员可以删除管理员");
        permissionService.createPermission("role-modify","修改角色","管理员可以对用户的角色进行修改");
        //资源管理列表
        permissionService.createPermission("book-read","阅读图书","只有中级以上用户可以阅读读书");
        permissionService.createPermission("book-search","检索图书","中级以上用户可以检索图书");
        permissionService.createPermission("book-allInfo","查看图书所有信息","高级以上用户可以查看图书的所有信息");
    }

    @Test
    public void allocatePermissionforRoleTest(){

        Role superAdmin = roleRepository.findByName("super_admin");
        Set<Permission> superAdminPermission = new HashSet<>();
        superAdminPermission.add(permissionRepository.findByname("admin-add"));
        superAdminPermission.add(permissionRepository.findByname("admin-remove"));
        superAdminPermission.add(permissionRepository.findByname("user-add"));
        superAdminPermission.add(permissionRepository.findByname("user-remove"));
        superAdminPermission.add(permissionRepository.findByname("role-modify"));
        superAdminPermission.add(permissionRepository.findByname("book-read"));
        superAdminPermission.add(permissionRepository.findByname("book-search"));
        superAdminPermission.add(permissionRepository.findByname("book-allInfo"));
        superAdmin.setPermissions(superAdminPermission);
        roleRepository.save(superAdmin);

        Role admin = roleRepository.findByName("admin");
        Set<Permission> adminPermission = new HashSet<>();
        adminPermission.add(permissionRepository.findByname("user-add"));
        adminPermission.add(permissionRepository.findByname("user-remove"));
        adminPermission.add(permissionRepository.findByname("role-modify"));
        adminPermission.add(permissionRepository.findByname("book-read"));
        adminPermission.add(permissionRepository.findByname("book-search"));
        adminPermission.add(permissionRepository.findByname("book-allInfo"));
        admin.setPermissions(adminPermission);
        roleRepository.save(admin);

        Role advancedUser = roleRepository.findByName("advanced_user");
        Set<Permission> advancedUserPermission = new HashSet<>();
        advancedUserPermission.add(permissionRepository.findByname("book-read"));
        advancedUserPermission.add(permissionRepository.findByname("book-search"));
        advancedUserPermission.add(permissionRepository.findByname("book-allInfo"));
        advancedUser.setPermissions(advancedUserPermission);
        roleRepository.save(advancedUser);

        Role middleUser = roleRepository.findByName("middle_user");
        Set<Permission> middleUserPermission = new HashSet<>();
        middleUserPermission.add(permissionRepository.findByname("book-read"));
        middleUserPermission.add(permissionRepository.findByname("book-search"));
        middleUser.setPermissions(middleUserPermission);
        roleRepository.save(middleUser);

        Role primaryUser = roleRepository.findByName("primary_user");
        Set<Permission> primaryUserPermission = new HashSet<>();
        primaryUserPermission.add(permissionRepository.findByname("book-search"));
        primaryUser.setPermissions(primaryUserPermission);
        roleRepository.save(primaryUser);
    }
}
