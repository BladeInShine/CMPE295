'use strict';

angular.module('cmpe295App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('history', {
                parent: 'entity',
                url: '/historys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'cmpe295App.history.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/history/historys.html',
                        controller: 'HistoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('history');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('history.detail', {
                parent: 'entity',
                url: '/history/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'cmpe295App.history.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/history/history-detail.html',
                        controller: 'HistoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('history');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'History', function($stateParams, History) {
                        return History.get({id : $stateParams.id});
                    }]
                }
            })
            .state('history.new', {
                parent: 'history',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/history/history-dialog.html',
                        controller: 'HistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    image: null,
                                    description: null,
                                    calorie: null,
                                    time: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('history', null, { reload: true });
                    }, function() {
                        $state.go('history');
                    })
                }]
            })
            .state('history.edit', {
                parent: 'history',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/history/history-dialog.html',
                        controller: 'HistoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['History', function(History) {
                                return History.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('history', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('history.delete', {
                parent: 'history',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/history/history-delete-dialog.html',
                        controller: 'HistoryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['History', function(History) {
                                return History.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('history', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
