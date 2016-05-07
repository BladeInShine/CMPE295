'use strict';

angular.module('cmpe295App')
    .controller('RecHistoryController', function ($scope, $state, RecHistory) {
        $scope.rating = 5;
        $scope.saveRatingToServer = function(rating) {
            console.log('Rating selected - ' + rating);
        };

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
    })
    .directive('fundooRating', function () {
        return {
            restrict: 'A',
            template: '<ul class="rating">' +
            '<li ng-repeat="star in stars" ng-class="star" ng-click="toggle($index)">' +
            '\u2605' +
            '</li>' +
            '</ul>',
            scope: {
                ratingValue: '=',
                max: '=',
                readonly: '@',
                onRatingSelected: '&'
            },
            link: function (scope, elem, attrs) {

                var updateStars = function() {
                    scope.stars = [];
                    for (var  i = 0; i < scope.max; i++) {
                        scope.stars.push({filled: i < scope.ratingValue});
                    }
                };

                scope.toggle = function(index) {
                    if (scope.readonly && scope.readonly === 'true') {
                        return;
                    }
                    scope.ratingValue = index + 1;
                    scope.onRatingSelected({rating: index + 1});
                };

                scope.$watch('ratingValue', function(oldVal, newVal) {
                    if (newVal) {
                        updateStars();
                    }
                });
            }
        }
    })
;
