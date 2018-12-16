
angular.module('myApp.admin', [])
    .controller('AdminCtrl',function ( $state, $scope, $location, $http, BackendService) {
        var vm = this;

        //登录
        vm.username = "";
        vm.password = "";
        vm.remember = "false";
        vm.message = "";
        vm.roles = [];
        vm.permissions = [];
        vm.dataNow = [];

        vm.users = [];

        vm.inputUsername = "";
        vm.inputPassword = "";

        vm.usernameAddRole = "";
        vm.passwordAddRole = "";
        vm.rolenamesAddRole = [];
        vm.adminNameAdd = "";
        vm.usernameDeleteRole = "";
        vm.adminNameDelete = "";

        vm.usersAddFile = "";
        vm.usersDeleteIds = [];

        vm.permissionAddName = "";
        vm.permissionAddDisp = "";
        vm.permissionAddDescrib = "";

        vm.roleModifyUserName = "";
        vm.roleModifyRoleName = [];

        vm.allRolesData = [];
        vm.allPermissionsData = [];

        vm.newPassword = "";
        vm.newStatus = "";
        vm.userNamePre = "";
        vm.userStatusPre = "";

        vm.roleNameAdd = "";
        vm.roleDescriptionAdd = "";
        vm.roleDisplayNameAdd = "";
        vm.rolePermissionsAdd = [];

        vm.importDataType = "republic_book";

        vm.login = login;
        vm.logout = logout;
        //用户管理
        vm.roleModify = roleModify;
        vm.userAdd = userAdd;
        vm.userDelete = userDelete;
        vm.adminAdd = adminAdd;
        vm.adminRemove = adminRemove;
        vm.usersAdd = usersAdd;
        vm.listUsers = listUsers;
        vm.listRoles = listRoles;
        vm.listPermissions = listPermissions;
        vm.addPermissions = addPermissions;
        vm.addRole = addRole;
        vm.passwordModify = passwordModify;
        vm.userStatusModify = userStatusModify;

        //导入书籍信息
        vm.importDataCompressed = importDataCompressed;
        vm.importData = importData;
        vm.importLocalImage = importLocalImage;

        vm.hasAdmin = hasAdmin;
        vm.hasPermission = hasPermission;
        vm.showUsers = showUsers;
        vm.userAddClear = userAddClear;
        vm.verifyPermission = verifyPermission;
        vm.usersAddClear =usersAddClear;
        vm.usersAddFile = usersAddFile;
        vm.upload = upload;
        vm.usersDelete = usersDelete;
        vm.addChecked = addChecked;
        vm.permissionAddClear = permissionAddClear;
        vm.roleModifyClear = roleModifyClear;
        vm.userInfoModifyClear = userInfoModifyClear;
        vm.userInfoModify = userInfoModify;
        vm.userStatusChange = userStatusChange;
        vm.getUserModifyPre = getUserModifyPre;
        vm.checkPermissionInput = checkPermissionInput;
        vm.checkUserInput = checkUserInput;
        vm.roleAddClear = roleAddClear;
        vm.checkRoleAddInput = checkRoleAddInput;
        vm.bookTypeChange = bookTypeChange;
        vm.setBookRatioState = setBookRatioState;
        
        

        function hasAdmin(role, superRole, roles) {
            var roleState = $.inArray(role, roles);
            var superRoleState = $.inArray(superRole, roles);
            return (roleState >=0 || superRoleState >= 0);
        }

        function hasPermission( superRole, roles) {
            var superRoleState = $.inArray(superRole, roles);
            return ( superRoleState >= 0);
        }

        function verifyPermission(permission, href) {
            if (hasPermission(permission, vm.permissions)){
                return href;
            }
            else{
                alert("缺少权限！");
                return "";
            }
        }

        function showUsers(role, roles, data) {
            if(hasPermission(role, roles)){
                listUsers();
            }
            else {
                vm.users = data;
            }
        }

        function userAddClear() {
            vm.usernameAddRole = "";
            vm.passwordAddRole = "";
            vm.rolenamesAddRole = [];
            for(var i = 0; i< vm.allRolesData.length; ++i){
                document.getElementById(vm.allRolesData[i].roleName).checked = false;
            }
        }
        
        function roleAddClear() {
            vm.roleNameAdd = "";
            vm.roleDescriptionAdd = "";
            vm.roleDisplayNameAdd = "";
            vm.rolePermissionsAdd = [];
            for(var i = 0; i< vm.allPermissionsData.length; ++i){
                document.getElementById(vm.allPermissionsData[i].permissionName+"role-add").checked = false;
            }
        }

        function permissionAddClear() {
            vm.permissionAddName = "";
            vm.permissionAddDisp = "";
            vm.permissionAddDescrib = "";
        }

        function usersAddClear() {
            vm.usersAddFile = "";
        }

        function roleModifyClear() {
            vm.roleModifyRoleName = [];
            vm.userNamePre = "";
            for(var i = 0; i< vm.allRolesData.length; ++i){
                document.getElementById(vm.allRolesData[i].roleName + 'modify').checked = false;
            }
        }

        function userInfoModifyClear() {
            vm.newPassword = "";
            vm.newStatus = vm.userStatusPre;
            roleModifyClear();
        }

        function usersAddFile(id) {
            var target = document.getElementById(id);
            vm.usersAddFile = target.files[0].name;
        }

        function upload(id) {
            var form = new FormData();
            var file = document.getElementById(id).files[0];
            // vm.usersAddFile = file;
            form.append('file', file);
            $http({
                method: 'POST',
                url: '/userManagement/users_add',
                data: form,
                headers:{'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (resp) {
                console.log('upload success');

            }).error(function (response) {
                alert("fail");
                console.log('upload fail');
            })
        }

        function addChecked(id, group) {
            var target = document.getElementById(id);
            var checked = target.checked;
            if(checked){
                group.unshift(target.value);
            }
            else {
                if($.inArray(target.value, group) >= 0){
                    group.splice($.inArray(target.value, group), 1);
                }
            }
        }

        function usersDelete(ids) {
            if(hasPermission('user_delete', vm.permissions)){
                for(var id in ids){
                    var target = document.getElementById(ids[id]);
                    vm.usernameDeleteRole = target.value;
                    userDelete();
                }
                vm.usersDeleteIds = [];
                vm.usernameDeleteRole = "";
            }
            else {
                alert("缺少权限！");
                vm.usersDeleteIds = [];
                vm.usernameDeleteRole = "";
            }
        }

        function getUserModifyPre(user) {
            vm.userNamePre = user.username;
            vm.userStatusPre = user.status;
            vm.newStatus = vm.userStatusPre;
            if(user.status == "open"){
                document.getElementById("open").checked = true;
                document.getElementById("locked").checked = false;
            }
            else{
                document.getElementById("open").checked = false;
                document.getElementById("locked").checked = true;
            }
        }

        function setBookRatioState() {
            if (vm.importDataType == "republic_book"){
                document.getElementById("republic_book").checked = true;
                document.getElementById("ancient_book").checked = false;
                document.getElementById("characteristic_book").checked = false;
            }
            else if (vm.importDataType == "ancient_book"){
                document.getElementById("ancient_book").checked = true;
                document.getElementById("republic_book").checked = false;
                document.getElementById("characteristic_book").checked = false;
            }
            else if (vm.importDataType == "characteristic_book"){
                document.getElementById("characteristic_book").checked = true;
                document.getElementById("republic_book").checked = false;
                document.getElementById("ancient_book").checked = false;
            }

        }

        function userStatusChange(id) {
            var target = document.getElementById(id);
            var checked = target.checked;
            if(checked){
                vm.newStatus = target.value;
            }
            else {
                vm.newStatus = vm.userStatusPre;
            }
        }

        function bookTypeChange(id) {
            var target = document.getElementById(id);
            var checked = target.checked;
            if(checked){
                vm.importDataType = target.value;
            }
            else {
                vm.importDataType = "republic_book";
            }
        }

        function userInfoModify() {
            userStatusModify();
            passwordModify();
            roleModify();
            userInfoModifyClear();
        }

        function checkPermissionInput() {
            if(vm.permissionAddName == ""){
                alert("资源名称不能为空！请重新输入")
            }
            else {
                addPermissions();
            }
        }

        function checkUserInput() {
            if(vm.usernameAddRole == "" || vm.passwordAddRole == "" || vm.rolenamesAddRole.length == 0){
                alert("输入信息不完整！");
            }
            else {
                for(var i=0; i<vm.users.length; ++i){
                    if(vm.usernameAddRole == vm.users[i].username){
                        alert("用户已存在！")
                    }
                }
                userAdd();
            }
        }
        
        function checkRoleAddInput() {
            if(vm.roleNameAdd == ""){
                alert("角色名不能为空！");
            }
            else {
                addRole();
            }
        }

        function login() {
            BackendService.login({
                "username": vm.username,
                "password": vm.password,
                "remember": vm.remember
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                vm.dataNow = respBody;
                if (error === 0) {
                    vm.roles = respBody.role;
                    vm.permissions = respBody.permission;
                    vm.message = respBody.message;
                    if (hasAdmin('admin', 'super_admin', vm.roles)){
                        showUsers('user_query', vm.permissions, vm.dataNow);
                        listRoles();
                        listPermissions();
                        $state.go('admin.management',{})
                    }
                    else{
                        alert("非管理员，不能登录！")
                    }
                }else{
                    alert("登陆失败！");
                }
            });
        }

        function logout() {
            BackendService.logout({
                "username": vm.username,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {

                }
            });
        }

        //用户管理
        //修改角色
        function roleModify() {
            BackendService.roleModify({
                "username": vm.userNamePre,
                "rolename": vm.roleModifyRoleName
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    showUsers('user_query', vm.permissions, vm.dataNow);
                }
            });
        }
        //密码修改
        function passwordModify() {
            BackendService.passwordModify({
                "username": vm.userNamePre,
                "newPassword": vm.newPassword
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    showUsers('user_query', vm.permissions, vm.dataNow);
                    console.log("密码修改成功！");
                }
            });
        }
        //用户状态修改
        function userStatusModify() {
            BackendService.userStatusModify({
                "username": vm.userNamePre,
                "rolename": vm.newStatus
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    showUsers('user_query', vm.permissions, vm.dataNow);
                }
            });
        }
        //添加用户
        function userAdd() {
            BackendService.userAdd({
                "username": vm.usernameAddRole,
                "password": vm.passwordAddRole,
                "roleName": vm.rolenamesAddRole
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    showUsers('user_query', vm.permissions, vm.dataNow);
                    userAddClear();
                }

            });
        }
        //删除用户
        function userDelete() {
            BackendService.userDelete({
                "username": vm.usernameDeleteRole,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    showUsers('user_query', vm.permissions, vm.dataNow);
                    console.log("删除成功");
                }
            });
        }
        //添加管理员
        function adminAdd() {
            BackendService.adminAdd({
                "username": vm.adminNameAdd
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {

                }
            });
        }
        //删除管理员
        function adminRemove() {
            BackendService.adminRemove({
                "username": vm.adminNameDelete,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {

                }
            });
        }
        //得到所有的用户信息
        function listUsers() {
            BackendService.listUsers({}).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.users = respBody.data;
                }
            });
        }
        //得到所有角色信息
        function listRoles() {
            BackendService.listRoles({}).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.allRolesData = respBody.data;
                }
            });
        }
        //得到所有资源信息
        function listPermissions() {
            BackendService.listPermissions({}).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.allPermissionsData = respBody.data;
                }
            });
        }

        //批量添加用户信息
        function usersAdd() {
                var form = new FormData();
                var file = document.getElementById("input-file").files[0];
                form.append('file', file);
                $http({
                    method: 'POST',
                    url: '/userManagement/users_add',
                    data: form,
                    headers:{'Content-Type': undefined},
                    transformRequest: angular.identity
                }).success(function (resp) {
                    showUsers('user_query', vm.permissions, vm.dataNow);
                    alert("添加成功！");
                    console.log('upload success');
                }).error(function (response) {
                    showUsers('user_query', vm.permissions, vm.dataNow);
                    alert("添加失败！");
                    console.log('upload fail');
                })

        }
        //添加权限
        function addPermissions() {
            BackendService.addPermissions({
                "permissionName": vm.permissionAddName,
                "permissionDescription": vm.permissionAddDisp,
                "permissionDisplayName": vm.permissionAddDescrib
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    listPermissions();
                }
                permissionAddClear();
            });
        }
        //添加角色
        function addRole() {
            BackendService.addRole({
                "roleName": vm.roleNameAdd,
                "roleDescription": vm.roleDescriptionAdd,
                "roleDisplayName": vm.roleDisplayNameAdd,
                "rolePermissions": vm.rolePermissionsAdd
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    listRoles();
                }
                roleAddClear();
            });
        }

        //导入书籍信息
        //上传压缩的图书元信息
        function importDataCompressed() {
            var form = new FormData();
            var file = document.getElementById("import-data-compressed-file").files[0];
            form.append('file', file);
            form.append("type",vm.importDataType);
            $http({
                method: 'POST',
                url: '/import/importDataCompressed',
                data: form,
                headers:{'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (resp) {
                alert("添加成功！");
                console.log('upload success');
            }).error(function (response) {
                alert("添加失败！");
                console.log('upload fail');
            })
        }
        //上传单个图书信息的元数据
        function importData() {
            var form = new FormData();
            var file = document.getElementById("import-data-file").files[0];
            form.append('file', file);
            form.append("type",vm.importDataType);
            $http({
                method: 'POST',
                url: '/import/importData',
                data: form,
                headers:{'Content-Type': undefined},
                transformRequest: angular.identity
            }).success(function (resp) {
                alert("添加成功！");
                console.log('upload success');
            }).error(function (response) {
                alert("添加失败！");
                console.log('upload fail');
            })
        }
        //上传压缩的图片信息
        function importLocalImage() {
            BackendService.importLocalImage({
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {

                }
            });
        }


        $(function() {
            $.contextMenu({
                selector: '.context-menu-one',
                callback: function(key, options) {
                    console.log(key);
                },
                items: {
                    "edit": {
                        name: "修改角色",
                        icon: "edit",
                        callback: function(itemKey, opt, rootMenu, originalEvent) {
                            var m = "edit was clicked";
                            console.log(m);
                            window.location.href = verifyPermission('role_modify', '#modal-container-userAddSingle');
                            console.log(window.location.href);
                        }
                    }
                }
            });
            //
            // $('.context-menu-one').on('click', function(e){
            //     console.log('clicked', this);
            // })
        });

        return vm;
    });

function deleteClass(classId1, classId2, classId3)
{
    var element1 = angular.element(classId1);
    var element2 = angular.element(classId2);
    var element3 = angular.element(classId3);
    element1.removeClass("active");
    element2.removeClass("active");
    element3.removeClass("active");
}

function deleteImportClass(classId1)
{
    var element1 = angular.element(classId1);
    element1.removeClass("active");
}
