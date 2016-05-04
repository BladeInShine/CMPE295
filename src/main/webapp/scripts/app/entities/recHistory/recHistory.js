'use strict';

angular.module('cmpe295App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('recHistory', {
                parent: 'entity',
                url: '/recHistorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'cmpe295App.recHistory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recHistory/recHistorys.html',
                        controller: 'RecHistoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('recHistory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('recHistory.detail', {
                parent: 'entity',
                url: '/recHistory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'cmpe295App.recHistory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recHistory/recHistory-detail.html',
                        controller: 'RecHistoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('recHistory');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'RecHistory', function($stateParams, RecHistory) {
                        return RecHistory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('recHistory.new', {
                parent: 'recHistory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/recHistory/recHistory-dialog.html',
                        controller: 'RecHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    foodId: null,
                                    foodName: null,
                                    brandId: null,
                                    brandName: null,
                                    timestamp: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('recHistory', null, { reload: true });
                    }, function() {
                        $state.go('recHistory');
                    })
                }]
            })
            .state('recHistory.edit', {
                parent: 'recHistory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/recHistory/recHistory-dialog.html',
                        controller: 'RecHistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['RecHistory', function(RecHistory) {
                                return RecHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('recHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('recHistory.delete', {
                parent: 'recHistory',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/recHistory/recHistory-delete-dialog.html',
                        controller: 'RecHistoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['RecHistory', function(RecHistory) {
                                return RecHistory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('recHistory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
