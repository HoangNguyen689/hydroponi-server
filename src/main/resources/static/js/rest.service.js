(function() {
    'use strict';

    angular
        .module('RealTimeReporting')
        .factory("ChartController", ChartController);

    ChartController.$inject = ['$http'];

    function ChartController($http) {

        let service = {};

        service.getData = function() {
            return $http({
                method : 'GET',
                url : '/data'
            });
        };

        return service;
    }

})();