'use strict';

angular.module('myApp.home', ['ui.bootstrap'])
    .controller('View1Ctrl', function ($scope, $http, BackendService,$state) {
        var vm = this;

        vm.active = 0;
        vm.navViewState = "hidden";
        vm.test = "test in book";


        vm.searchMethod = "simple"; // multip

        vm.type = ""; //ancient_book, republic_book, characteristic_book
        vm.bookType = "";


        vm.author = "";
        vm.title = "";
        vm.chuban = "";
        vm.niandai = "";

        vm.books = [];
        // 排序
        vm.sortPropertyDescriptions = {
            "title": "标题",
            "author": "作者",
            "chuban": "出版",
        };
        vm.sortDirectionDescriptions = {
            "asc": "升序",
            "desc": "降序",
        };
        vm.sortProperty = "title";
        vm.sortDirection = "desc";

        // Facets
        vm.authorFacets = [];
        vm.chubanFacets = [];
        vm.refinedAuthorFacets = [];
        vm.refinedChubanFacets = [];

        // 分页
        vm.totalPages = 0;
        vm.first = false;
        vm.last = false;
        vm.number = 1;
        vm.size = 10;
        vm.totalItems = 0;

        //登录和用户管理
        vm.username = "";
        vm.password = "";
        vm.remember = "false";
        vm.message = "";
        vm.roles = [];
        vm.permissions = [];
        vm.userId = "";

        vm.loginState = false; //未登录
        vm.loginWrong = true;  //登陆失败
        vm.logStateView = "用户登录";
        vm.logoutStateView = "登录";
        vm.bookReadStateH = "";
        vm.bookInfoState = "";
        vm.logTitle = "登录";


        vm.test = "test";


        vm.name = "";
        vm.pwd = "";
        vm.phone = "";
        vm.email = "";
        vm.career = "";
        vm.gender = "";
        vm.code = "";


        vm.search = search;
        vm.reSearch = reSearch;
        vm.setSortProperty = setSortProperty;
        vm.pageChanged = pageChanged;
        vm.btn0Active = btn0Active;
        vm.btn1Active = btn1Active;
        vm.btn2Active = btn2Active;
        vm.btn3Active = btn3Active;
        // vm.viewChanged = viewChanged;
        vm.activeChanged = activeChanged;
        vm.refineFacetOnAuthor = refineFacetOnAuthor;
        vm.refineFacetOnChuban = refineFacetOnChuban;
        vm.reset = reset;
        vm._ = _;
        vm.login = login;
        vm.logout = logout;
        vm.contain = contain;
        vm.logChanged = logChanged;
        vm.bookReadJudge = bookReadJudge;
        vm.bookAllInfoJudge = bookAllInfoJudge;
        vm.register = register;


        return vm;

        function reset() {

        }

        function register() {
            BackendService.register({
                "username": vm.name,
                "password":vm.pwd,
                "email":vm.email,
                "phone":vm.phone,
                "gender":vm.gender,
                "career":vm.career,
                "age":vm.age,
                "code":vm.code
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    alert("注册成功！");
                    $state.go('home',{})

                }else{
                    alert("注册失败！");
                }
            });
        }

        function login() {
            BackendService.login({
                "username": vm.username,
                "password": vm.password,
                "remember": vm.remember
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.roles = respBody.role;
                    vm.permissions = respBody.permission;
                    vm.message = respBody.message;

                    vm.loginState = true;
                    vm.loginWrong = false;
                    vm.logStateView = vm.username + " 注销";
                    vm.logoutStateView = "确认注销";

                    vm.userId = respBody.userId;

                    // $state.go('admin.management',{})
                    // $state.go('admin.management',{})

                }else{
                    alert("登陆失败asdasd！");
                    vm.loginState = false;
                    vm.loginWrong = true;
                    vm.logStateView = "登录";
                    vm.logoutStateView = "登录";
                    vm.logTitle = "登录";
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
                    vm.loginState = false;
                    vm.loginWrong = true;
                    vm.logStateView = "登录";
                    vm.logoutStateView = "登录";
                    vm.message = "";
                    vm.logTitle = "登录";
                }
            });
        }

        function logChanged(state) {
            if(state)
            {
                logout();
            }
            else {
                login();
                vm.logTitle = "注销";
            }
        }

        function contain(value, permissions) {
            return $.inArray(value, permissions);
        }

        function bookReadJudge(value,  valueInfo,permissions,bookId) {
            var tempPermissions = permissions;
            var authority = $.inArray(value, permissions);
            var authorityInfo = $.inArray(valueInfo, permissions);
            if (authority >= 0){
                vm.bookReadStateH = "";
                vm.test = tempPermissions;
                BackendService.addBrowsingHistory({
                    "userId": vm.userId,
                    "bookId": bookId,
                }).then(function (resp) {});
                $state.go("home.book", { bookId:bookId, userId:vm.userId, authorityInfo:authorityInfo, logStateView:vm.logStateView });
            }
            else {
                vm.bookReadStateH = "#no-book-read";
            }
        }

        function bookAllInfoJudge(value, permissions) {
            var authority = $.inArray(value, permissions);
            if (authority >= 0){
                vm.bookInfoState = "";
            }
            else {
                vm.bookInfoState = "hidden";
            }
        }

        function refineFacetOnAuthor(facet) {
            if (vm.refinedAuthorFacets.indexOf(facet) === -1) {
                vm.refinedAuthorFacets.push(facet.value);
                search();
            }
        }

        function refineFacetOnChuban(facet) {
            if (vm.refinedChubanFacets.indexOf(facet) === -1) {
                vm.refinedChubanFacets.push(facet.value);
                search();
            }
        }

        function setSortProperty(sortProperty) {
            vm.sortProperty = sortProperty;
        }

        function pageChanged(number) {
            vm.number = number;
            reSearch();
        }

        function activeChanged(active) {
            vm.active = active;
        }


        function search() {
            if (vm.active === 0) {
                searchSimple();
            } else if (vm.active === 1) {
                searchMultipProperties();
            }
        }

        function reSearch() {
            vm.navViewState = "";
            if (vm.active === 0) {
                reSearchSimple();
            } else if (vm.active === 1) {
                reSearchMultiProperties();
            }
        }

        function searchMultipProperties() {
            BackendService.multiPropertiesSearch({
                "properties": ["author", "title", "chuban"],
                "values": [vm.author, vm.title, vm.chuban],
                "pageNumber": vm.number - 1,
                "pageSize": vm.size,
                "type": vm.type,
                "chubanNosplit": vm.chubanNosplit,
                "authorNosplit": vm.authorNosplit,
                "sortDirections": [vm.sortDirection],
                "sortProperties": [vm.sortProperty],
                "refinedAuthorFacets": vm.refinedAuthorFacets,
                "refinedChubanFacets": vm.refinedChubanFacets,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.books = respBody.data.content;

                    vm.bookType = respBody.data.type;

                    // 分页
                    vm.totalPages = respBody.data.totalPages;
                    vm.first = respBody.data.first;
                    vm.last = respBody.data.last;
                    vm.number = respBody.data.number + 1;
                    // vm.size = respBody.data.size;
                    vm.totalItems = respBody.data.totalElements;
                    // vm.totalElement = respBody.data.totalElement;

                    vm.authorFacets = respBody.data.allFacets[0].content;
                    vm.chubanFacets = respBody.data.allFacets[1].content;
                }
            });
        }

        function reSearchMultiProperties() {
            vm.refinedAuthorFacets = [];
            vm.refinedChubanFacets = [];
            searchMultipProperties();
        }

        function searchSimple() {
            BackendService.simpleSearch({
                "searchTerm": vm.searchTerm,
                "pageNumber": vm.number - 1,
                "pageSize": vm.size,
                "type": vm.type,
                "chubanNosplit": vm.chubanNosplit,
                "authorNosplit": vm.authorNosplit,
                "sortDirections": [vm.sortDirection],
                "sortProperties": [vm.sortProperty],
                "refinedAuthorFacets": vm.refinedAuthorFacets,
                "refinedChubanFacets": vm.refinedChubanFacets,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.books = respBody.data.content;

                    // 分页
                    vm.totalPages = respBody.data.totalPages;
                    vm.first = respBody.data.first;
                    vm.last = respBody.data.last;
                    vm.number = respBody.data.number + 1;
                    vm.totalItems = respBody.data.totalElements;
                    // vm.totalBookNum = respBody.data.totalItems;

                    vm.authorFacets = respBody.data.allFacets[0].content;
                    vm.chubanFacets = respBody.data.allFacets[1].content;
                }
            });
        }

        function reSearchSimple() {
            vm.refinedAuthorFacets = [];
            vm.refinedChubanFacets = [];
            searchSimple();
        }

        function btn1Active(type) {
            var state = "";
            if(type == "republic_book"){
                state = "active";
            }
            else{
                state = "";
            }
            return state;
        }

        function btn2Active(type) {
            var state = "";
            if(type == "ancient_book"){
                state = "active";
            }
            else{
                state = "";
            }
            return state;
        }

        function btn3Active(type) {
            var state = "";
            if(type == "characteristic_book"){
                state = "active";
            }
            else{
                state = "";
            }
            return state;
        }

        function btn0Active(type) {
            var state = "";
            if(type == ""){
                state = "active";
            }
            else{
                state = "";
            }
            return state;
        }

    }) .controller('View2Ctrl', function ($scope, $http, $stateParams, $timeout, $document, BackendService) {
    var vm = this;
    var options = {"url": "data-original"};
    var viewer;
    var viewerList;

    vm.state = $stateParams;
    vm.bookId = $stateParams.bookId;
    vm.active = 0;
    vm.book = {zhaopianbianhao: []};
    vm.book = {thumbnailList: []};
    vm.type = "";
    vm.infoViewState = "";
    // vm.test = "test in book";
    vm.infoView = "metadata";
    vm.view = view;

    vm.imgNum = 0;
    vm.imgChange = imgChange;
    vm.infoViewChanged = infoViewChanged;

    // 分页
    vm.totalPages = 0;
    vm.first = false;
    vm.last = false;
    vm.number = 1;
    vm.size = 12;
    vm.totalItems = 0;
    vm.pageStart = 1;
    vm.pageEnd = 12;

    //用户信息
    vm.authorityInfo = $stateParams.authorityInfo;
    vm.logStateView = $stateParams.logStateView;

    vm.pageChanged = pageChanged;
    vm.pageIndexChanged = pageIndexChanged;
    vm.infoViewStateChanged = infoViewStateChanged;
    vm.authorityInfoStateChanged = authorityInfoStateChanged;


    function infoViewChanged(value) {
        if(value>0){
            vm.infoView = "metadata";
        }
        else {
            vm.infoView = "directory";
        }
    }

    function pageIndexChanged(index, number, size){
        return index + size * (number - 1);
    }


    function pageChanged(number, size) {
        vm.number = number;
        vm.pageStart = size *(number - 1) +1;
        if(size*number > vm.totalItems){
            vm.pageEnd = vm.totalItems;
        }
        else{
            vm.pageEnd = size * number;
        }
    }

    function imgChange(number) {
        vm.imgNum = number;

    }


    BackendService.getBook({"bookId": vm.bookId}).then(function (resp) {
        var respBody = resp.data;
        if (respBody.error === 0) {
            vm.book = respBody.data;

            // vm.type = respBody.data.type;

            // 分页
            // vm.totalPages = respBody.data.thumbnailList.length / vm.size;
            vm.first = respBody.data.thumbnailList.first;
            vm.last = respBody.data.thumbnailList.last;
            vm.totalItems = respBody.data.thumbnailList.length;
            // vm.size = respBody.data.thumbnailList.size;
            vm.testnum = respBody.data.thumbnailList.length;
        }

        $timeout(function () {
            if (!viewer) {
                viewer = new Viewer(document.getElementById("gallery"), options);
            } else {
                viewer.update();
            }
        }, 50);
    });

    $document.ready(function () {
    });

    function view(imgId) {
        viewerList = new Viewer(document.getElementById(imgId), options);
        viewerList.show();
    }

    function infoViewStateChanged(info, length) {
        var state = "";
        if (info == null || info == "" ||length <= 0){
            state = "hidden";
        }
        else {
            state = "";
        }
        return state;
    }

    function authorityInfoStateChanged() {
        if (vm.authorityInfo < 0 ){
            vm.infoViewState = "hidden";
        }
        else {
            vm.infoViewState = "";
        }
    }

    return vm;
}) .controller('View2Ctrl', function ($scope, $http, $stateParams, $timeout, $document, BackendService) {
    var vm = this;
    var options = {"url": "data-original"};
    var viewer;
    var viewerList;

    vm.userId = $stateParams.userId;
    vm.bookId = $stateParams.bookId;
    vm.active = 0;
    vm.book = {zhaopianbianhao: []};
    vm.book = {thumbnailList: []};
    vm.type = "";
    vm.infoViewState = "";
    vm.test1 = "test2";
    vm.view = view;

    vm.imgNum = 0;
    vm.imgChange = imgChange;

    // 分页
    vm.totalPages = 0;
    vm.first = false;
    vm.last = false;
    vm.number = 1;
    vm.size = 12;
    vm.totalItems = 0;
    vm.pageStart = 1;
    vm.pageEnd = 12;

    //用户信息
    vm.authorityInfo = $stateParams.authorityInfo;
    vm.logStateView = $stateParams.logStateView;

    vm.pageChanged = pageChanged;
    vm.pageIndexChanged = pageIndexChanged;
    vm.infoViewStateChanged = infoViewStateChanged;
    vm.authorityInfoStateChanged = authorityInfoStateChanged;
    vm.addCollection = addCollection;

    function addCollection(userId, bookId) {
        BackendService.addCollection({
            "userId": userId,
            "bookId": bookId
        }).then(function (resp) {
            const respBody = resp.data;
            const error = respBody.error;
            if (error === 0) {
                var message = respBody.message;
                alert(message);
            } else {
                var message = respBody.message;
                alert(message);
            }
        });
    }

    function pageIndexChanged(index, number, size){
        return index + size * (number - 1);
    }


    function pageChanged(number, size) {
        vm.number = number;
        vm.pageStart = size *(number - 1) +1;
        if(size*number > vm.totalItems){
            vm.pageEnd = vm.totalItems;
        }
        else{
            vm.pageEnd = size * number;
        }
    }

    function imgChange(number) {
        vm.imgNum = number;
    }


    BackendService.getBook({"bookId": vm.bookId}).then(function (resp) {
        var respBody = resp.data;
        if (respBody.error === 0) {
            vm.book = respBody.data;

            // 分页
            vm.first = respBody.data.thumbnailList.first;
            vm.last = respBody.data.thumbnailList.last;
            vm.totalItems = respBody.data.thumbnailList.length;
            // vm.size = respBody.data.thumbnailList.size;
            vm.testnum = respBody.data.thumbnailList.length;
        }

        $timeout(function () {
            if (!viewer) {
                viewer = new Viewer(document.getElementById("gallery"), options);
            } else {
                viewer.update();
            }
        }, 50);
    });


    function view(imgId) {
        viewerList = new Viewer(document.getElementById(imgId), options);
        viewerList.show();
    }

    function infoViewStateChanged(info, length) {
        var state = "";
        if (info == null || info == "" ||length <= 0){
            state = "hidden";
        }
        else {
            state = "";
        }
        return state;
    }

    function authorityInfoStateChanged() {
        if (vm.authorityInfo < 0 ){
            vm.infoViewState = "hidden";
        }
        else {
            vm.infoViewState = "";
        }
    }

    return vm;
});
function deleteClass(classId)
{
    var element = angular.element(classId);
    element.removeClass("active");
}
