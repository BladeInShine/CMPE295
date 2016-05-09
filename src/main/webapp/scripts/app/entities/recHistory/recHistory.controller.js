'use strict';

angular.module('cmpe295App')
    .controller('RecHistoryController', function ($scope, $http, RecHistory) {
        $scope.rating = 2;
        $scope.saveRatingToServer = function (rating) {
            console.log('Rating selected - ' + rating);
        };

        $scope.recHistorys = [];
        $scope.loadAll = function () {
            RecHistory.query(function (result) {
                $scope.recHistorys = result;
                for (var k in result) {
                    console.log("key k is " + k + ", value is " + result[k]);
                    console.log(typeof result[k]);
                    var resultY = result[k];
                    // if('rating' in resultY) {
                    for (var y in resultY) {
                        if (y === 'rating') {
                            console.log("key y is " + y + ", value is " + resultY[y]);
                            $scope.rating=resultY[y];
                            // if($scope.rating == 3)
                            //     $scope.rating=0;
                            $scope.btnRated= $scope.rating>0;
                            break;
                        }
                    }
                    if($scope.rating!=null)
                         break;
                }
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
                id: null,
                rating: 0
            };
        };
        $scope.updateRating = function(rating_val, re_id){
            if(rating_val==$scope.rating)
                return;
            $scope.rating=rating_val;
            var req = {
                method: 'POST',
                url: 'api/recHistorys/rating',
                headers: {'Content-Type': 'application/json'},
                data: {"id": re_id.id, "rating":rating_val}
            }
            $http(req).then(function(){
                $scope.btnRated=true;
                console.log('Send rating success');
            }, function(){
                console.log('Send rating error');
            }).then(function(){
                $scope.btnRated=true;
                console.log('finally');
            });


        }
    })
    .directive('fundooRating', function () {
        return {
            restrict: 'A',
            template: '<ul class="rating"> ' +
            '<li ng-repeat="star in stars" ng-class="star" ng-click="toggle($index)"> \u2605 </li>' +
            ' </ul>',
            scope: {
                ratingValue: '=',
                max: '=',
                readonly: '@',
                onRatingSelected: '&'
            },
            link: function (scope, elem, attrs) {

                var updateStars = function () {
                    scope.stars = [];
                    for (var i = 0; i < scope.max; i++) {
                        scope.stars.push({filled: i < scope.ratingValue});
                    }
                };

                scope.toggle = function (index) {
                    if (scope.readonly && scope.readonly === 'true') {
                        return;
                    }
                    scope.ratingValue = index + 1;
                    scope.onRatingSelected({rating: index + 1});
                };

                scope.$watch('ratingValue', function (newVal, oldVal) {
                    if (newVal || oldVal==0) {
                        updateStars();
                    }
                });
            }
        }
    })
;
