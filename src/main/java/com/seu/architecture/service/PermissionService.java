package com.seu.architecture.service;

import com.seu.architecture.model.Permission;

/**
 * Created by 17858 on 2017/5/11.
 */
public interface PermissionService {

    void createPermission(String name, String display_name, String description);

    void deletePermission(String name);
}
