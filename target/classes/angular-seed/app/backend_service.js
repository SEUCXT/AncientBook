"use strict";

var module = angular.module("myApp.service", []);
module.service("BackendService", function ($http, $log) {

    var SEPARATOR = ";";
    //搜索
    this.getBook = getBook;
    this.simpleSearch = simpleSearch;
    this.multiPropertiesSearch = multiPropertiesSearch;
    //登录和退出
    this.login = login;
    this.logout = logout;
    //用户管理
    this.roleModify = roleModify;
    this.userAdd = userAdd;
    this.userDelete = userDelete;
    this.adminAdd = adminAdd;
    this.adminRemove = adminRemove;
    this.usersAdd = usersAdd;
    this.listUsers = listUsers;
    this.listPermissions = listPermissions;
    this.listRoles = listRoles;
    this.addPermissions = addPermissions;
    this.addRole = addRole;
    this.passwordModify = passwordModify;
    this.userStatusModify = userStatusModify;
    //导入书籍信息
    this.importDataCompressed = importDataCompressed;
    this.importData = importData;
    this.importLocalImage = importLocalImage;
    this.addBrowsingHistory = addBrowsingHistory;
    this.getBrowsingHistory = getBrowsingHistory;
    this.deleteBrowsingHistory = deleteBrowsingHistory;
    this.getCollection = getCollection;
    this.deleteCollection = deleteCollection;
    this.addCollection = addCollection;
    this.register = register;
    this.getPopular = getPopular;
    this.getNewBook = getNewBook;
    this.getLike = getLike;
    this.getUserProfile = getUserProfile;
    this.modifyUserProfile = modifyUserProfile;

    // var host = "http://127.0.0.1:7963";

    //搜索
    function getBook(params) {
        // return $http.get(host + "/searchBook/" + params.bookId, {});
        return $http.get("/searchBook/" + params.bookId, {});
    }

    function register(params) {
        return $http.post("/loginManagement/register",
            {
                "username": params.username,
                "password":params.password,
                "email":params.email,
                "phone":params.phone,
                "gender":params.gender,
                "career":params.career,
                "age":params.age,
                "code":params.code});
    }



    function simpleSearch(params) {
        return $http.get("/searchBook/simpleSearch", {
            // return $http.get(host + "/searchBook/simpleSearch", {
            params: {
                "searchTerm": params.searchTerm,
                "pageNumber": params.pageNumber,
                "pageSize": params.pageSize,
                "type": params.type,
                "sortDirections": params.sortDirections.join(SEPARATOR),
                "sortProperties": params.sortProperties.join(SEPARATOR),
                "refinedAuthorFacets": params.refinedAuthorFacets.join(SEPARATOR),
                "refinedChubanFacets": params.refinedChubanFacets.join(SEPARATOR),
            }
        });
    }

    function multiPropertiesSearch(params) {
        return $http.get("/searchBook/multiPropertiesSearch", {
            // return $http.get(host + "/searchBook/multiPropertiesSearch", {
            params: {
                "properties": params.properties.join(SEPARATOR),
                "values": params.values.join(SEPARATOR),
                "pageNumber": params.pageNumber,
                "pageSize": params.pageSize,
                "type": params.type,
                "sortDirections": params.sortDirections.join(SEPARATOR),
                "sortProperties": params.sortProperties.join(SEPARATOR),
                "refinedAuthorFacets": params.refinedAuthorFacets.join(SEPARATOR),
                "refinedChubanFacets": params.refinedChubanFacets.join(SEPARATOR),
            }
        });
    }

    //登录和退出
    function login(params) {
        return $http.get("/loginManagement/login", {
            params: {
                "username": params.username,
                "password": params.password,
                "remember": params.remember
            }
        });
    }

    function logout(params) {
        return $http.get("/loginManagement/logout", {
            params: {
                "username": params.username,
            }
        });
    }

    //用户管理
    //修改角色
    function roleModify(params) {
        return $http.get("/userManagement/roleModify", {
            params: {
                "username": params.username,
                "rolename": params.rolename
            }
        });
    }

    //修改密码
    function passwordModify(params) {
        return $http.get("/userManagement/passwordModify", {
            params: {
                "username": params.username,
                "newPassword": params.newPassword
            }
        });
    }

    //修改用户状态
    function userStatusModify(params) {
        return $http.get("/userManagement/userStatusModify", {
            params: {
                "username": params.username,
                "status": params.status
            }
        });
    }

    //添加用户
    function userAdd(params) {
        return $http.get("/userManagement/userAdd", {
            params: {
                "username": params.username,
                "password": params.password,
                "roleName": params.roleName
            }
        });
    }

    //删除用户
    function userDelete(params) {
        return $http.get("/userManagement/userDelete", {
            params: {
                "username": params.username
            }
        });
    }

    //添加管理员
    function adminAdd(params) {
        return $http.get("/userManagement/admin_add", {
            params: {
                "username": params.username
            }
        });
    }

    // 删除管理员
    function adminRemove(params) {
        return $http.get("/userManagement/admin_remove", {
            params: {
                "username": params.username
            }
        });
    }

    //得到所有的用户信息
    function listUsers(params) {
        return $http.get("/userManagement/list_user", {});
    }

    //得到所有资源信息
    function listPermissions(params) {
        return $http.get("/userManagement/list_permissions", {});
    }

    //得到所有角色信息
    function listRoles(params) {
        return $http.get("/userManagement/list_roles", {});
    }

    //批量添加用户信息
    function usersAdd(params) {
        return $http.get("/userManagement/users_add", {
            params: {
                "file": params.file
            }
        });
    }

    //添加权限
    function addPermissions(params) {
        return $http.get("/userManagement/add_permissions", {
            params: {
                "permissionName": params.permissionName,
                "permissionDescription": params.permissionDescription,
                "permissionDisplayName": params.permissionDisplayName
            }
        });
    }

    //添加角色
    function addRole(params) {
        return $http.get("/userManagement/add_Role", {
            params: {
                "roleName": params.roleName,
                "roleDescription": params.roleDescription,
                "roleDisplayName": params.roleDisplayName,
                "rolePermissions": params.rolePermissions
            }
        });
    }

    //导入书籍信息
    //上传压缩的图书元信息
    function importDataCompressed(params) {
        return $http.get("/import/importDataCompressed", {
            params: {
                "type": params.type,
                "file": params.file,
                request: params.request,
                model: params.model
            }
        });
    }

    //上传单个图书信息的元数据
    function importData(params) {
        return $http.get("/import/importData", {
            params: {
                "type": params.type,
                "file": params.file
            }
        });
    }

    //上传压缩的图片信息
    function importLocalImage(params) {
        return $http.get("/import/importLoalImage", {
            params: {
                "type": params.type,
                "file": params.file,
                request: params.request
            }
        });
    }

    function addBrowsingHistory(params) {
        return $http.get("/browsingHistory/add", {
            params: {
                "userId": params.userId,
                "bookId": params.bookId
            }
        })
    }

    function getBrowsingHistory(params) {
        return $http.get("/browsingHistory/get", {
            params: {
                "userId":params.userId
            }
        });
    }

    function deleteBrowsingHistory(params) {
        return $http.get("/browsingHistory/delete", {
            params: {
                "userId":params.userId,
                "historyId":params.historyId
            }
        });
    }

    function addCollection(params) {
        return $http.get("/collection/add", {
            params: {
                "userId":params.userId,
                "bookId":params.bookId
            }
        });
    }

    function getCollection(params) {
        return $http.get("/collection/get", {
            params: {
                "userId":params.userId
            }
        });
    }

    function deleteCollection(params) {
        return $http.get("/collection/delete", {
            params: {
                "userId":params.userId,
                "bookId":params.bookId
            }
        });
    }


    function getPopular(params) {
        return $http.get("/recommendation/popular", {
            params: {
                "pageNum": params.pageNum,
                "pageSize": params.pageSize
            }
        });
    }

    function getNewBook(params) {
        return $http.get("/recommendation/newBook", {
            params: {
                "pageNum": params.pageNum,
                "pageSize": params.pageSize
            }
        });
    }

    function getLike(params) {
        return $http.get("/recommendation/like", {
            params: {
                "pageNum": params.pageNum,
                "pageSize": params.pageSize
            }
        });
    }

    function modifyUserProfile(params) {
        return $http.post("/userManagement/modifyUserProfile",
            {
                "id":params.userId,
                "username": params.username,
                "email":params.email,
                "phone":params.phone,
                "gender":params.gender,
                "career":params.career,
                "age":params.age});
    }

    function getUserProfile(params) {
        return $http.get("/userManagement/getUserProfile", {
            params: {
                "username": params.username
            }
        });
    }
});

