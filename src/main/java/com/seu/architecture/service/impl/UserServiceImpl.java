package com.seu.architecture.service.impl;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.seu.architecture.config.Constants;
import com.seu.architecture.model.*;
import com.seu.architecture.repository.PermissionRepository;
import com.seu.architecture.repository.RoleRepository;
import com.seu.architecture.repository.UserProfileRepository;
import com.seu.architecture.repository.UserRepository;
import com.seu.architecture.service.IndexingService;
import com.seu.architecture.service.MetadataParsingService;
import com.seu.architecture.service.UserService;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    LoginTicketService loginTicketService;

    @Autowired
    MetadataParsingService metadataParsingService;

    @Autowired
    IndexingService indexingService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param rememberMe
     * @return
     */
    @Override
    public ViewObject login(String username, String password, boolean rememberMe) {
        ViewObject vo = new ViewObject();
        if (Strings.isNullOrEmpty(username)) {
            vo.set(ViewObject.ERROR, 1);
            vo.set(ViewObject.MESSAGE, "用户名不能为空");
            return vo;
        }

        if (Strings.isNullOrEmpty(password)) {
            vo.set(ViewObject.ERROR, 2);
            vo.set(ViewObject.MESSAGE, "密码不能为空");
            return vo;
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            vo.set(ViewObject.ERROR, 3);
            vo.set(ViewObject.MESSAGE, "用户名不存在");
            return vo;
        }
        if (user.getStatus().equals(Constants.LOCKED_USER_STATUS)) {
            vo.set(ViewObject.ERROR, 4);
            vo.set(ViewObject.MESSAGE, "用户被锁定，请联系管理员！");
            return vo;
        }

        String hashedPassword = Hashing.md5().hashUnencodedChars(password + user.getSalt()).toString();
        if (!Objects.equals(hashedPassword, user.getPassword())) {
            vo.set(ViewObject.ERROR, 5);
            vo.set(ViewObject.MESSAGE, "密码不正确");
            return vo;
        }

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, hashedPassword);
        token.setRememberMe(rememberMe);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            vo.set(ViewObject.ERROR, 6).
                    set(ViewObject.MESSAGE, "您的账号或密码输入错误！");
            e.printStackTrace();
        }

        vo.set(ViewObject.ERROR, 0)
                .set("userId", user.getId())
                .set("username", user.getUsername())
                .set("ticket", loginTicketService.createLoginTicket(user.getUsername()));

        Set<String> roleSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();
        for (Role role : user.getRoles()) {
            roleSet.add(role.getName());
            for (Permission permission : role.getPermissions()) {
                permissionSet.add(permission.getName());
            }
        }

        vo.set("role", roleSet)
                .set("permission", permissionSet)
                .set(ViewObject.MESSAGE, "登录成功！");

        return vo;
    }


    @Override
    public ViewObject register(UserRegisterRequest request) {
        ViewObject vo = new ViewObject();
        if (Strings.isNullOrEmpty(request.getUsername())) {
            vo.set(ViewObject.ERROR, 1);
            vo.set(ViewObject.MESSAGE, "用户名不能为空");
            return vo;
        }

        if (Strings.isNullOrEmpty(request.getPassword())) {
            vo.set(ViewObject.ERROR, 2);
            vo.set(ViewObject.MESSAGE, "密码不能为空");
            return vo;
        }

        long count = userRepository.countByUsername(request.getUsername());
        if (count != 0) {
            vo.set(ViewObject.ERROR, 3);
            vo.set(ViewObject.MESSAGE, "用户名已经被注册");
            return vo;
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(Hashing.md5().hashUnencodedChars(request.getPassword() + user.getSalt()).toString());
        Set<Role> defaultRole = new HashSet<>();
        defaultRole.add(roleRepository.findByName("primary_user"));
        user.setRoles(defaultRole);
        user.setStatus(Constants.OPEN_USER_STATUS);
        userRepository.save(user);

        UserProfile profile = new UserProfile();
        profile.setUsername(request.getUsername());
        profile.setEmail(request.getEmail());
        profile.setPhone(request.getPhone());
        profile.setAge(request.getAge());
        profile.setCareer(request.getCareer());
        profile.setGender(request.getGender());

        User newUser = userRepository.findByUsername(user.getUsername());
        profile.setId(newUser.getId());
        profile.setTime(new Date());
        userProfileRepository.save(profile);

        String ticket = loginTicketService.createLoginTicket(request.getUsername());
        vo.set(ViewObject.ERROR, 0);
        vo.set("ticket", ticket);
        return vo;
    }

    /**
     * 添加一个用户
     *
     * @param username
     * @param password
     * @param roleName
     * @return
     */
    @Override
    public ViewObject addUser(String username, String password, String roleName) {
        ViewObject vo = new ViewObject();
        if (Strings.isNullOrEmpty(username)) {
            vo.set(ViewObject.ERROR, 1);
            vo.set(ViewObject.MESSAGE, "用户名不能为空");
            return vo;
        }
        if (Strings.isNullOrEmpty(password)) {
            vo.set(ViewObject.ERROR, 2);
            vo.set(ViewObject.MESSAGE, "密码不能为空");
            return vo;
        }
        long count = userRepository.countByUsername(username);
        if (count != 0) {
            vo.set(ViewObject.ERROR, 3)
                    .set(ViewObject.MESSAGE, "用户名已经被注册");
            return vo;
        }
        User user = new User();
        user.setUsername(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(Hashing.md5().hashUnencodedChars(password + user.getSalt()).toString());
        Set<Role> defaultRole = new HashSet<>();
        String[] roles = roleName.split(",");
        for (String str : roles) {
            defaultRole.add(roleRepository.findByName(str));
        }
        user.setRoles(defaultRole);
        user.setStatus(Constants.OPEN_USER_STATUS);
        userRepository.save(user);
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.MESSAGE, "添加用户成功|");
        return vo;
    }

    /**
     * 修改密码
     *
     * @param username
     * @param newPassword
     * @return
     */
    @Override
    public ViewObject passwordModify(String username, String newPassword) {
        ViewObject vo = new ViewObject();
        User user = userRepository.findByUsername(username);
        if (Strings.isNullOrEmpty(newPassword)) {
            vo.set(ViewObject.ERROR, 1);
            vo.set(ViewObject.MESSAGE, "密码不能为空");
            return vo;
        }
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(Hashing.md5().hashUnencodedChars(newPassword + user.getSalt()).toString());
        userRepository.save(user);
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.MESSAGE, "修改密码成功！");
        return vo;
    }

    /**
     * 退出登录
     *
     * @param username
     * @return
     */
    @Override
    public ViewObject logout(String username) {
        ViewObject vo = new ViewObject();
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
        }
        vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "用户退出登录！");
        return vo;
    }

    /**
     * 为用户分配角色
     *
     * @param username
     * @param roleNames
     * @return
     */
    @Override
    public ViewObject setRolesforUser(String username, String roleNames) {
        ViewObject vo = new ViewObject();
        try {
            User user = userRepository.findByUsername(username);
            Set<Role> roles = user.getRoles();
            roles.clear();
            for (String rolename : roleNames.split(",")) {
                roles.add(roleRepository.findByName(rolename));
            }
            user.setRoles(roles);
            userRepository.save(user);
            vo.set(ViewObject.ERROR, 0)
                    .set(ViewObject.MESSAGE, "用户角色修改成功！");
            return vo;
        } catch (Exception e) {
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, "用户角色修改失败！");
            e.printStackTrace();
            return vo;
        }
    }

    /**
     * 修改用户的状态
     *
     * @param username
     * @param status
     * @return
     */
    @Override
    public ViewObject modifyUserStatus(String username, String status) {
        ViewObject vo = new ViewObject();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, "找不到该用户！");
            return vo;
        }
        user.setStatus(status);
        userRepository.save(user);
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.MESSAGE, "修改用户状态成功！");
        return vo;
    }

    /**
     * 添加管理员
     *
     * @param username
     * @return
     */
    @Override
    public ViewObject addAdmin(String username) {
        ViewObject vo = new ViewObject();
        User user = userRepository.findByUsername(username);
        Set<Role> roles = user.getRoles();
        roles.add(roleRepository.findByName(Constants.ADMIN_ROLE));
        user.setRoles(roles);
        userRepository.save(user);
        vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "成功添加管理员!");
        return vo;
    }

    /**
     * 删除管理员
     *
     * @param username
     * @return
     */
    @Override
    public ViewObject deleteAdmin(String username) {
        ViewObject vo = new ViewObject();
        User user = userRepository.findByUsername(username);
        Set<Role> roles = user.getRoles();
        Role adminRole = roleRepository.findByName(Constants.ADMIN_ROLE);
        if (!roles.contains(adminRole)) {
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, "该用户不是管理员");
        }
        roles.remove(adminRole);
        user.setRoles(roles);
        userRepository.save(user);
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.MESSAGE, "管理员删除成功!");
        return vo;
    }

    /**
     * 得到所有用户
     *
     * @return
     */
    @Override
    public ViewObject listUser() {
        ViewObject vo = new ViewObject();

        Iterable<User> users = userRepository.findAll();
        Iterator<User> it = users.iterator();
        List<JSONObject> userList = new ArrayList<>();

        while (it.hasNext()) {
            User current = it.next();
            String username = current.getUsername();
            Long id = current.getId();
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();
            String status = current.getStatus();
            for (Role role : current.getRoles()) {
                roles.add(role.getName());
                for (Permission permission : role.getPermissions()) {
                    permissions.add(permission.getName());
                }
            }
            JsonObject js = new JsonObject();
            JSONObject jsonObject = js.createUserJSONObject(id, username, roles, permissions, status);
            userList.add(jsonObject);
        }
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.DATA, userList);
        return vo;
    }

    @Override
    public ViewObject addPermissions(Permission permission) {
        ViewObject vo = new ViewObject();
        try {
            permissionRepository.save(permission);
            vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "用户添加成功！！");
        } catch (Exception e) {
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return vo;
    }

    @Override
    public ViewObject addRole(Role role) {
        ViewObject vo = new ViewObject();
        try {
            roleRepository.save(role);
            vo.set(ViewObject.ERROR, 0).set(ViewObject.MESSAGE, "用户添加成功！！");
        } catch (Exception e) {
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return vo;
    }

    @Override
    public ViewObject listPermissions() {
        ViewObject vo = new ViewObject();
        Iterable<Permission> permissions = permissionRepository.findAll();
        Iterator<Permission> it = permissions.iterator();
        List<JSONObject> permissionList = new ArrayList<>();
        while (it.hasNext()) {
            Permission current = it.next();
            String permissionName = current.getName();
            Long id = current.getId();
            String displayName = current.getDisplayName();
            String description = current.getDescription();
            JsonObject js = new JsonObject();
            JSONObject jsonObject = js.createPermissionJSONObject(id, permissionName, displayName, description);
            permissionList.add(jsonObject);
        }
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.DATA, permissionList);
        return vo;
    }

    @Override
    public ViewObject listRoles() {
        ViewObject vo = new ViewObject();

        Iterable<Role> roles = roleRepository.findAll();
        Iterator<Role> it = roles.iterator();
        List<JSONObject> roleList = new ArrayList<>();

        while (it.hasNext()) {
            Role current = it.next();
            String roleName = current.getName();
            Long id = current.getId();
            String description = current.getDescription();
            String displayName = current.getDisplayName();
            Set<String> permissions = new HashSet<>();
            for (Permission permission : current.getPermissions()) {
                permissions.add(permission.getName());
            }

            JsonObject js = new JsonObject();
            JSONObject jsonObject = js.createRoleJSONObject(id, roleName, displayName, description, permissions);
            roleList.add(jsonObject);
        }
        vo.set(ViewObject.ERROR, 0)
                .set(ViewObject.DATA, roleList);
        return vo;
    }

    @Override
    public ViewObject allocatePermissionsForRole(String roleName, String permissions) {
        ViewObject vo = new ViewObject();
        vo.set(ViewObject.ERROR, 0);
        Role role = roleRepository.findByName(roleName);
        String[] permissionArr = permissions.split(",");
        Set<Permission> permissionSet = new HashSet<>();
        for (String str : permissionArr) {
            Permission permission = permissionRepository.findByname(str);
            if (permission == null) {
                vo.set(ViewObject.ERROR, 1).set(ViewObject.MESSAGE, "资源名称有错误！");
            } else {
                permissionSet.add(permissionRepository.findByname(str));
            }
        }
        role.setPermissions(permissionSet);
        roleRepository.save(role);
        return vo;
    }

    @Override
    public Set<Role> getRolesByUsername(String username) {
        return userRepository.findByUsername(username).getRoles();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> insertUsers(List<User> users) {
        userRepository.save(users);
        indexingService.submitIndexingUsersJob(users);
        return users;
    }

    public ViewObject parseAndInsertUsers(InputStream inputStream) {
        ViewObject vo = new ViewObject();
        try {
            List<User> users = metadataParsingService.parseUsersXssf(inputStream);
            userRepository.save(users);
            indexingService.submitIndexingUsersJob(users);
            vo.set(ViewObject.ERROR, 0)
                    .set(ViewObject.DATA, users);
        } catch (IOException e) {
            e.printStackTrace();
            vo.set(ViewObject.ERROR, 1)
                    .set(ViewObject.MESSAGE, e.getMessage());
        }
        return vo;
    }
}
