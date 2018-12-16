'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
    'ui.router',
    'ui.bootstrap',
    'myApp.home',

    // 'myApp.index',
    'myApp.admin',
    'myApp.version',
    'myApp.service',
    'myApp.user',
    'ui.bootstrap'
]).config(['$urlRouterProvider', '$stateProvider', function ($urlRouterProvider, $stateProvider) {
    $stateProvider
        .state("home", {
            url: "/home",

            views:{
                "":{
                    templateUrl: "home/home.html",
                    controller: "View1Ctrl as vm"
                },
                "carousel@home":{
                    templateUrl:"home/home.carousel.html"
                }
            }
        })
        .state("home.simple-search-result",{
            url:"/simpleSearchResult",
            views:{
                "":{
                    templateUrl: "home/home.simple-search-result.html"
                },
                "head-small@home.simple-search-result":{
                    templateUrl: "home/home.head-small.html"
                },
                "result@home.simple-search-result":{
                    templateUrl:"home/home.result.html"
                }
            }
        })
        .state("home.multi-search-result",{
            url:"/multiSearchResult",
            views:{
                "":{
                    templateUrl: "home/home.multi-search-result.html"
                },
                "head-small@home.multi-search-result":{
                    templateUrl: "home/home.head-small.html"
                },
                "result@home.multi-search-result":{
                    templateUrl:"home/home.result.html"
                },
                "multi-result@home.multi-search-result":{
                    templateUrl: "home/home.multi-properties-search.html"
                }
            }
        })
        .state("home.multi-properties-search", {
            url: "/multiPropertiesSearch",
            templateUrl: "home/home.multi-properties-search.html"
        })
        .state("home.book", {
            url: "/book/:bookId",
            params: {"bookId":null, "userId":null, "authorityInfo":null, "logStateView":null},
            views:{
                "":{
                    templateUrl: "home/home.book.html",
                    controller: "View2Ctrl as vm",
                },
                "head-small@home.book":{
                    templateUrl: "home/home.head-small.html"
                }
            }
        })
        .state("user",{
            url:"/user",

            views:{
                "":{
                    templateUrl:"user/user.html",
                    controller:"UserCtrl as vm"
                }
            }
        })
        .state("user.center",{
            url:"/center",

            views:{
                "":{
                    templateUrl:"user/user.center.html"
                },
                "history@user.center":{
                    templateUrl:"user/user.history.html"
                },
                "recommend@user.center":{
                    templateUrl:"user/user.recommend.html"
                },
                "collection@user.center":{
                    templateUrl:"user/user.collection.html"
                },
                "profile@user.center":{
                    templateUrl:"user/user.profile.html"
                },

            }
        })
        .state("admin",{
            url:"/admin",

            views:{
                "":{
                    templateUrl:"admin/admin.html",
                    controller:"AdminCtrl as vm"
                }
            }
        })
        .state("admin.management",{
            url:"/management",

            views:{
                "":{
                    templateUrl:"admin/admin.management.html"
                },
                "user-manage@admin.management":{
                    templateUrl:"admin/admin.user-manage.html"
                },
                "role-manage@admin.management":{
                    templateUrl:"admin/admin.role-manage.html"
                },
                "resource-manage@admin.management":{
                    templateUrl:"admin/admin.resource-manage.html"
                },
                "book-manage@admin.management":{
                    templateUrl:"admin/admin.book-manage.html"
                },

            }
        })
        .state("admin.login",{
        url:"/login",
        templateUrl:"admin/admin.user-manage.html",
    });

    // $urlRouterProvider.otherwise('/home');
    $urlRouterProvider.when('', '/home');
}]);
