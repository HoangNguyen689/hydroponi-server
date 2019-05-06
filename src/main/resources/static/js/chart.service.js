(function() {
    'use strict';

    angular
        .module('RealTimeReporting')
        .factory('RealTimeReportingChartService', RealTimeReportingChartService);

    RealTimeReportingChartService.$inject = [ 'ChartController' ];

    function RealTimeReportingChartService(ChartController) {
        let service = {};

        service.populateStackedBarChart = function() {
            // let tempValue = chart.xAxis.categories[chart.xAxis.categories.length - 1];
            let chart = new Highcharts.Chart({
                chart: {
                    type: 'spline',
                    renderTo : 'realTimeDataBarChart',
                    events : {
                        load : function() {
                            setInterval(function() {
                                let firstSeries = chart.series[0];
                                let categories = chart.xAxis[0].categories;
                                let categoriesLength = categories.length;
                                let yAxisValue;

                                ChartController.getData().then(function(response) {
                                    let data = response.data;
                                    yAxisValue = data.yaxis;
                                    firstSeries.addPoint([categoriesLength, yAxisValue], false, true);

                                    let dateNow = new Date(data.xaxis);

                                    let dateString = dateNow.getHours() + ':'
                                        + (dateNow.getMinutes() <= 9 ? '0' +dateNow.getMinutes() : dateNow.getMinutes())
                                        + ':' +dateNow.getSeconds();

                                    categories.push(dateString);
                                    chart.xAxis[0].setCategories(categories, false);
                                    chart.redraw();

                                });

                            }, 2 * 60 * 1000);
                        }
                    }
                },
                title: {
                    text : ''
                },
                xAxis: {
                    title: {
                        text: 'Timestamp'
                    },
                    categories: ['11:33:40', '11:33:41', '11:33:42', '11:33:43', '11:33:44',
                        '11:33:45', '11:33:46', '11:33:47', '11:33:48', '11:33:49']
                },
                yAxis : {
                    title : {
                        text : 'Temperature in C'
                    }
                },
                series: [{
                    name: 'Temperature',
                    data: [29, 24, 26, 26, 25, 27, 28, 21, 26, 26]
                }]
            })
        };

        return service;
    }
})();