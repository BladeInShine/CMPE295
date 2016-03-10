'use strict';

angular.module('cmpe295App')
    .controller('HistoryController', function ($scope, $state, History) {

        $scope.historys = [];
        $scope.loadAll = function() {
            History.query(function(result) {
               $scope.historys = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.history = {
                image: null,
                description: null,
                calorie: null,
                time: null,
                id: null
            };
        };
    });
