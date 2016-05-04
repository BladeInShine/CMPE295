'use strict';

angular.module('cmpe295App')
    .controller('RecHistoryController', function ($scope, $state, RecHistory) {

        $scope.recHistorys = [];
        $scope.loadAll = function() {
            RecHistory.query(function(result) {
               $scope.recHistorys = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.recHistory = {
                foodId: null,
                foodName: null,
                brandId: null,
                brandName: null,
                timestamp: null,
                id: null
            };
        };
    });
