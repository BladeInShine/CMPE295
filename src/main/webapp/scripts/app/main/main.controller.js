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

        $http.get('/api/historys/graph', {
            withCredentials: true,
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(response){
            console.log('/api/historys/graph');
            console.log(response);
            var data = {intake: [], consumed: []};
            for (var i = 0; i < response.length; i++) {
                var r = response[i];
                var date_str = r.date;

                var event_date = new Date(
                    date_str.substr(4),
                    date_str.substr(2, 2) - 1,
                    date_str.substr(0, 2));
                var intake = {x: event_date, y: r.intake};
                data.intake.push(intake);
                var consumed = {x: event_date, y: r.consumed};
                data.consumed.push(consumed);
            }
            drawGraph(data);
        })
        .error(function(response){
            console.log('error');
        });

    });
