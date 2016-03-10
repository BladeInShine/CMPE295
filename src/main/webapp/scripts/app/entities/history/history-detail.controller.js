'use strict';

angular.module('cmpe295App')
    .controller('HistoryDetailController', function ($scope, $rootScope, $stateParams, entity, History, User) {
        $scope.history = entity;
        $scope.load = function (id) {
            History.get({id: id}, function(result) {
                $scope.history = result;
            });
        };
        var unsubscribe = $rootScope.$on('cmpe295App:historyUpdate', function(event, result) {
            $scope.history = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
