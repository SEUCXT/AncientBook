package com.seu.architecture.controller;


import com.seu.architecture.model.*;
import com.seu.architecture.repository.PermissionRepository;
import com.seu.architecture.repository.RoleRepository;
import com.seu.architecture.service.IndexingService;
import com.seu.architecture.service.MetadataParsingService;
import com.seu.architecture.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 17858 on 2017/5/16.
 */
@Controller
@RequestMapping("/userManagement")
public class UserManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);

    @Autowired
    UserService userService;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MetadataParsingService metadataParsingService;

    @Autowired
    IndexingService indexingService;

    @RequestMapping(value = "/modifyUserProfile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ViewObject> modifyUserProfile(@RequestBody UserProfile userProfile) {

        ViewObject vo = userService.modifyUserProfile(userProfile);
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/getUserProfile")
    @ResponseBody
    public ResponseEntity<ViewObject> getUserProfile(@RequestParam("username") String username) {

        ViewObject vo = userService.getUserProfileByUsername(username);
        return ResponseEntity.ok(vo);
    }


    /**
     * 添加用户
     *
     * @param username
     * @param password
     * @param roleName
     * @return
     */
    @RequestMapping("/userAdd")
    @ResponseBody
    public ResponseEntity<ViewObject> userAdd(@RequestParam("username") String username,
                                              @RequestParam("password") String password,
                                              @RequestParam("roleName") String roleName) {

        ViewObject vo = userService.addUser(username, password, roleName);
        return ResponseEntity.ok(vo);
    }

    /**
     * 修改密码
     *
     * @param username
     * @param newPassword
     * @return
     */
    @RequestMapping("/passwordModify")
    @ResponseBody
    public ResponseEntity<ViewObject> passwordModify(@RequestParam("username") String username,
                                                     @RequestParam("newPassword") String newPassword) {

        ViewObject vo = userService.passwordModify(username, newPassword);
        return ResponseEntity.ok(vo);
    }

    /**
     * 修改用户的状态
     *
     * @param username
     * @return
     */
    @RequestMapping("/userStatusModify")
    @ResponseBody
    public ResponseEntity<ViewObject> userStatusModify(@RequestParam("username") String username,
                                                       @RequestParam("status") String status) {

        ViewObject vo = userService.modifyUserStatus(username, status);
        return ResponseEntity.ok(vo);
    }

    /**
     * 得到所有的用户信息
     *
     * @return
     */
    @RequestMapping("/list_user")
    @ResponseBody
    @RequiresPermissions("user_query")
    public ResponseEntity<ViewObject> listUsers() {

        ViewObject vo = userService.listUser();
        return ResponseEntity.ok(vo);
    }


    /**
     * 批量添加用户信息
     *
     * @param file
     * @param request
     * @param model
     */
    @RequestMapping("/users_add")
    @ResponseBody
    @RequiresPermissions("user_add")
    public ResponseEntity<ViewObject> addUsers(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {
        ViewObject vo = new ViewObject();
        try {
            List<User> users = metadataParsingService.parseUsersXssf(file.getInputStream());
            userService.insertUsers(users);
            vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "用户添加成功！！");
        } catch (Exception e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return ResponseEntity.ok(vo);
    }


    /**
     * 修改角色
     *
     * @param username
     * @param rolename
     * @return
     */
    @RequestMapping("/roleModify")
    @ResponseBody
    @RequiresPermissions("role_modify")
    public ResponseEntity<ViewObject> roleModify(@RequestParam("username") String username,
                                                 @RequestParam("rolename") String rolename) {

        ViewObject vo = userService.setRolesforUser(username, rolename);
        return ResponseEntity.ok(vo);
    }

    /**
     * 添加管理员
     *
     * @param username
     * @return
     */
    @RequestMapping("/admin_add")
    @ResponseBody
    @RequiresPermissions("admin_add")
    public ResponseEntity<ViewObject> adminAdd(@RequestParam("username") String username) {

        ViewObject vo = userService.addAdmin(username);
        return ResponseEntity.ok(vo);
    }

    /**
     * 删除管理员
     *
     * @param username
     * @return
     */
    @RequestMapping("/admin_remove")
    @ResponseBody
    @RequiresPermissions("admin_delete")
    public ResponseEntity<ViewObject> adminDelete(@RequestParam("username") String username) {

        ViewObject vo = userService.deleteAdmin(username);
        return ResponseEntity.ok(vo);
    }


    /**
     * 得到所有的资源信息
     *
     * @return
     */
    @RequestMapping("/list_permissions")
    @ResponseBody
    public ResponseEntity<ViewObject> listPermissions() {

        ViewObject vo = userService.listPermissions();
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/add_permissions")
    @ResponseBody
    @RequiresRoles("permission_add")
    public ResponseEntity<ViewObject> addPermissions(@RequestParam("permissionName") String permissionName,
                                                     @RequestParam("permissionDescription") String permissionDescription,
                                                     @RequestParam("permissionDisplayName") String permissionDisplayName) {

        Permission permission = new Permission();
        permission.setName(permissionName);
        permission.setDisplayName(permissionDisplayName);
        permission.setDescription(permissionDescription);
        ViewObject vo = userService.addPermissions(permission);
        return ResponseEntity.ok(vo);
    }


    /**
     * 得到所有的角色信息
     *
     * @return
     */
    @RequestMapping("/list_roles")
    @ResponseBody
    public ResponseEntity<ViewObject> listRoles() {

        ViewObject vo = userService.listRoles();
        return ResponseEntity.ok(vo);
    }


    @RequestMapping("/add_Role")
    @ResponseBody
    public ResponseEntity<ViewObject> addRole(@RequestParam("roleName") String roleName,
                                              @RequestParam("roleDescription") String roleDescription,
                                              @RequestParam("roleDisplayName") String roleDisplayName,
                                              @RequestParam("rolePermissions") String rolePermissions) {
        Role role = new Role();
        role.setName(roleName);
        role.setDescription(roleDescription);
        role.setDisplayName(roleDisplayName);
        Set<Permission> set = new HashSet<>();
        String[] permissions = rolePermissions.split(",");
        for (String str : permissions) {
            set.add(permissionRepository.findByname(str));
        }
        role.setPermissions(set);
        ViewObject vo = userService.addRole(role);
        return ResponseEntity.ok(vo);
    }

    @RequestMapping("/permission_allocate")
    @ResponseBody
    public ResponseEntity<ViewObject> allocatePermissionsForRole(@RequestParam("roleName") String roleName,
                                                                 @RequestParam("rolePermissions") String rolePermissions) {
        ViewObject vo = userService.allocatePermissionsForRole(roleName, rolePermissions);
        return ResponseEntity.ok(vo);
    }

}
