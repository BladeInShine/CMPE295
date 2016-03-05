'use strict';

angular.module('cmpe295App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


