'use strict';

angular.module('cmpe295App').controller('HistoryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'History', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, History, User) {

        $scope.history = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            History.get({id : id}, function(result) {
                $scope.history = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('cmpe295App:historyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.history.id != null) {
                History.update($scope.history, onSaveSuccess, onSaveError);
            } else {
                History.save($scope.history, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForTime = {};

        $scope.datePickerForTime.status = {
            opened: false
        };

        $scope.datePickerForTimeOpen = function($event) {
            $scope.datePickerForTime.status.opened = true;
        };
}]);
