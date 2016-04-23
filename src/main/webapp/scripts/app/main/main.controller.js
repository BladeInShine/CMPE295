'use strict';

angular.module('cmpe295App')
    .controller('MainController', function ($scope, Principal) {
        console.log("12======="+$scope.$parent.usingRecommender);
        Principal.identity().then(function(account) {
            $scope.$parent.usingRecommender = account!=null;
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
