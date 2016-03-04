'use strict';

angular.module('cMPE295App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


