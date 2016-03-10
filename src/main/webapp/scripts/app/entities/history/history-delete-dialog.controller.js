'use strict';

angular.module('cmpe295App')
	.controller('HistoryDeleteController', function($scope, $uibModalInstance, entity, History) {

        $scope.history = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            History.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
