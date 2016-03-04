 'use strict';

angular.module('cMPE295App')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-cMPE295App-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-cMPE295App-params')});
                }
                return response;
            }
        };
    });
