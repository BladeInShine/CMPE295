'use strict';

angular.module('cmpe295App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('descrip', {
                parent: 'account',
                url: '/descrip',
                data: {
                    authorities: [],
                    pageTitle: 'descrip.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/descrip/descrip.html',
                        controller: 'DescripController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('descrip');
                        return $translate.refresh();
                    }]
                }
            });
    });
