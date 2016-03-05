 'use strict';

angular.module('cmpe295App')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-cmpe295App-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-cmpe295App-params')});
                }
                return response;
            }
        };
    });
