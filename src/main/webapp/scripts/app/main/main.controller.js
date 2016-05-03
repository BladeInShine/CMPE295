'use strict';

angular.module('cmpe295App')
    .controller('MainController', function ($scope, Principal, $http) {
        Principal.identity().then(function(account) {
            $scope.$parent.usingRecommender = account!=null;
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            console.log("12======="+ $scope.$parent.usingRecommender);
        });
        $http.get('/api/historys/table', {
            withCredentials: true,
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(response){
            console.log(response);
            for (var i = 0; i < response.length; i++) {
                drawRow(response[i]);
            }
        })
        .error(function(response){
            console.log('error');
        });

    });
