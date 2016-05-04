'use strict';

angular.module('cmpe295App').controller('RecHistoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'RecHistory', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, RecHistory, User) {

        $scope.recHistory = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            RecHistory.get({id : id}, function(result) {
                $scope.recHistory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cmpe295App:recHistoryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.recHistory.id != null) {
                RecHistory.update($scope.recHistory, onSaveSuccess, onSaveError);
            } else {
                RecHistory.save($scope.recHistory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForTimestamp = {};

        $scope.datePickerForTimestamp.status = {
            opened: false
        };

        $scope.datePickerForTimestampOpen = function($event) {
            $scope.datePickerForTimestamp.status.opened = true;
        };
}]);
