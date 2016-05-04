'use strict';

angular.module('cmpe295App')
	.controller('RecHistoryDeleteController', function($scope, $uibModalInstance, entity, RecHistory) {

        $scope.recHistory = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            RecHistory.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
