
angular.module('myApp.user', [])
    .controller('UserCtrl',function ( $state, $scope, $location, $http, BackendService) {
        var vm = this;

        vm.username = "";
        vm.password = "";
        vm.remember = "false";
        vm.userId = 0;
        vm.historyList = [];
        vm.collectionList = [];
        vm.userProfile;
        vm.email = "";
        vm.age = 0;
        vm.career = "";
        vm.gender = "";
        vm.phone = "";

        vm.email = "";

        vm.pNum = 1
        vm.pSize = 5;
        vm.popularList = [];

        vm.lNum = 1
        vm.lSize = 5;
        vm.likeList = [];

        vm.nNum = 1
        vm.nSize = 5;
        vm.newBookList = [];

        vm.login = login;
        vm.getBrowsingHistory = getBrowsingHistory;
        vm.readHistoryBook = readHistoryBook;
        vm.deleteHistory = deleteHistory;
        vm.getCollection = getCollection;
        vm.deleteCollection = deleteCollection;
        vm.readCollectionBook = readCollectionBook;

        vm.getPopular = getPopular;
        vm.getLike = getLike;
        vm.getNewBook = getNewBook;
        vm.previousLike = previousLike;
        vm.previousNew = previousNew;
        vm.previousPopular = previousPopular;
        vm.nextNew = nextNew;
        vm.nextPopular = nextPopular;
        vm.nextLike = nextLike;

        vm.readRecommendBook = readRecommendBook;

        vm.getUserProfile = getUserProfile;
        vm.modifyUserProfile = modifyUserProfile;


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
                    vm.userId = respBody.userId;
                    getUserProfile();
                    getBrowsingHistory();
                    getCollection();
                    getPopular();
                    getNewBook();
                    getLike();
                    $state.go('user.center',{})
                }else{
                    alert("登陆失败！");
                }
            });
        }

        function getBrowsingHistory() {
            BackendService.getBrowsingHistory({
                "userId": vm.userId,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                vm.dataNow = respBody;
                if (error === 0) {
                    vm.historyList = respBody.data;
                }
            });
        }

        function readHistoryBook(bookId) {
            var authorityInfo = 1;
            var logStateView = "学生登录"
            $state.go("home.book", { bookId:bookId, userId:vm.userId, authorityInfo:authorityInfo, logStateView:logStateView });
        }

        function deleteHistory(userId, historyId) {
            BackendService.deleteBrowsingHistory({
                "userId": userId,
                "historyId": historyId,
            }).then(function (resp) {
                getBrowsingHistory();
                $state.go('user.center',{})
            });
        }

        function getCollection() {
            BackendService.getCollection({
                "userId": vm.userId,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                vm.dataNow = respBody;
                if (error === 0) {
                    vm.collectionList = respBody.data;
                }
            });
        }

        function deleteCollection(userId, bookId) {
            BackendService.deleteCollection({
                "userId": userId,
                "bookId": bookId,
            }).then(function (resp) {
                getCollection();
                $state.go('user.center',{})
            });
        }

        function readCollectionBook(bookId) {
            var authorityInfo = 1;
            var logStateView = "学生登录"
            $state.go("home.book", { bookId:bookId, userId:vm.userId, authorityInfo:authorityInfo, logStateView:logStateView });
        }

        function getPopular() {
            BackendService.getPopular({
                "pageNum": vm.pNum,
                "pageSize": vm.pSize,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.popularList = respBody.data;
                }
            });
        }

        function getLike() {
            BackendService.getLike({
                "pageNum": vm.lNum,
                "pageSize": vm.lSize,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.likeList = respBody.data;
                }
            });
        }

        function getNewBook() {
            BackendService.getNewBook({
                "pageNum": vm.nNum,
                "pageSize": vm.nSize,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.newBookList = respBody.data;
                }
            });
        }

        function previousLike() {
            if (vm.lNum > 1) {
                vm.lNum = vm.lNum - 1;
            }
            getLike();
            $state.go('user.center',{})
        }

        function nextLike() {
            vm.lNum = vm.lNum + 1;
            getLike();
            $state.go('user.center',{})

        }

        function previousPopular() {
            if (vm.pNum > 1) {
                vm.pNum = vm.pNum - 1;
            }
            getPopular();
            $state.go('user.center',{})
        }

        function nextPopular() {
            vm.pNum = vm.pNum + 1;
            getPopular();
            $state.go('user.center',{})
        }

        function previousNew() {
            if (vm.nNum > 1) {
                vm.nNum = vm.nNum - 1;
            }
            getNewBook();
            $state.go('user.center',{})
        }

        function nextNew() {
            vm.nNum = vm.nNum + 1;
            getNewBook();
            $state.go('user.center',{})
        }

        function readRecommendBook(bookId) {
            var authorityInfo = 1;
            var logStateView = "学生登录"
            $state.go("home.book", { bookId:bookId, userId:vm.userId, authorityInfo:authorityInfo, logStateView:logStateView });

        }

        function getUserProfile() {
            BackendService.getUserProfile({
                "username": vm.username
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    vm.userProfile = respBody.data;
                    vm.userId = vm.userProfile.id;
                    vm.email = vm.userProfile.email;
                    vm.age = vm.userProfile.age;
                    vm.career = vm.userProfile.career;
                    vm.gender = vm.userProfile.gender;
                    vm.phone = vm.userProfile.phone;

                }
            });
        }

        function modifyUserProfile() {
            BackendService.modifyUserProfile({
                "userId": vm.userId,
                "username": vm.username,
                "email":vm.email,
                "phone":vm.phone,
                "gender":vm.gender,
                "career":vm.career,
                "age":vm.age,
            }).then(function (resp) {
                const respBody = resp.data;
                const error = respBody.error;
                if (error === 0) {
                    alert(respBody.message);
                }
            });
        }


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