(function () {
    'use strict';

    let app = angular.module('RealTimeReporting', []);

    app.controller('RealTimeReportingController', RealTimeReportingController);

    RealTimeReportingController.$inject = [ 'RealTimeReportingChartService' ];

    function RealTimeReportingController(RealTimeReportingChartService) {
        RealTimeReportingChartService.populateStackedBarChart();
    }
})();
