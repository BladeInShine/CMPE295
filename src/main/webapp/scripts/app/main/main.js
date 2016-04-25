'use strict';

angular.module('cmpe295App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController'
                    },
                    'loginPanel@':{
                        templateUrl: 'scripts/app/account/login/login.html',
                        controller: 'LoginController'
                    },
                    'descripPanel@':{
                        templateUrl: 'scripts/app/account/descrip/descrip.html',
                        controller: 'DescripController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        $translatePartialLoader.addPart('login');
                        $translatePartialLoader.addPart('descrip');
                        return $translate.refresh();
                    }]
                }
            });
    });
