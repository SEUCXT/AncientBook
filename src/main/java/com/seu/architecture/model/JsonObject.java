package com.seu.architecture.model;

import net.sf.json.JSONObject;

import java.util.Set;

/**
 * Created by 17858 on 2017/5/18.
 */
public class JsonObject {

    public JSONObject createUserJSONObject(Long id, String username, Set<String> roles, Set<String> permissions, String status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("username", username);
        jsonObject.put("roles", roles);
        jsonObject.put("permissions", permissions);
        jsonObject.put("status", status);
        return jsonObject;
    }

    public JSONObject createPermissionJSONObject(Long id, String permissionName, String displayName, String discription) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("permissionName", permissionName);
        jsonObject.put("displayName", displayName);
        jsonObject.put("discription", discription);
        return jsonObject;
    }

    public JSONObject createRoleJSONObject(Long id, String roleName, String displayName, String discription, Set<String> permissions) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("roleName", roleName);
        jsonObject.put("displayName", displayName);
        jsonObject.put("discription", discription);
        jsonObject.put("permissions", permissions);
        return jsonObject;
    }
}
