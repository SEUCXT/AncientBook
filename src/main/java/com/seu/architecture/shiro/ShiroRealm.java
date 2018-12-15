package com.seu.architecture.shiro;

import com.seu.architecture.model.Permission;
import com.seu.architecture.model.Role;
import com.seu.architecture.model.User;
import com.seu.architecture.service.UserService;
import com.seu.architecture.service.impl.LocalServiceImpl;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * Created by 17858 on 2017/5/13.
 */

public class ShiroRealm extends AuthorizingRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    UserService userService;

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
        // TODO Auto-generated method stub
        String username = (String)arg0.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<Role> roles = userService.getRolesByUsername(username);
        for(Role role:roles){
            authorizationInfo.addRole(role.getName());
            LOGGER.info("Role:"+role.getName());
            Set<Permission> permissions = role.getPermissions();
            for(Permission permission:permissions){
                authorizationInfo.addStringPermission(permission.getName());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        // TODO Auto-generated method stub
        System.out.println("doGetAuthenticationInfo");
        String username = (String)arg0.getPrincipal();
        User user = userService.getUserByUsername(username);
        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }
        LOGGER.info("id: " + user.getId() + " " + "username: " + user.getUsername() + " " + "登陆成功！");
//        if(Boolean.TRUE.equals(user.isUser_locked())) {
//            throw new LockedAccountException(); //帐号锁定
//        }
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                getName()  //realm name
        );
        return authenticationInfo;
    }
}
