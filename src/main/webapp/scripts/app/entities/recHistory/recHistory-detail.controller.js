'use strict';

angular.module('cmpe295App')
    .controller('RecHistoryDetailController', function ($scope, $rootScope, $stateParams, entity, RecHistory, User) {
        $scope.recHistory = entity;
        $scope.load = function (id) {
            RecHistory.get({id: id}, function(result) {
                $scope.recHistory = result;
            });
        };
        var unsubscribe = $rootScope.$on('cmpe295App:recHistoryUpdate', function(event, result) {
            $scope.recHistory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
