'use strict';

angular.module('cmpe295App').controller('HistoryDialogController',
    ['$scope', '$http', '$stateParams', '$uibModalInstance', 'entity', 'History', 'User',
        function($scope, $http, $stateParams, $uibModalInstance, entity, History, User) {

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

        $scope.uploadFile = function(files) {
            var uploadUrl = 'http://127.0.0.1:8080/api/historys/photo'
            var f = files[0], r = new FileReader();
            r.onloadend = function(e) {
                var data = e.target.result;
                var fd = new FormData();
                fd.append('file', data);
                fd.append('file_name', f.name);
                $http.post(uploadUrl, fd, {
                    withCredentials: true,
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(function(){
                    console.log('success');
                })
                .error(function(){
                    console.log('error');
                });
            };
            r.readAsDataURL(f);
        };
}]);
