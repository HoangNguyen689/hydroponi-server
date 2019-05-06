$(document).ready(function () {

    let socket = new SockJS('/stomp');
    let newValue;
    let stompClient = Stomp.over(socket);
    let message;

    stompClient.connect({}, function (frame) {
        console.log(frame);
        stompClient.subscribe("/topic/humid", function (data) {
            message = data.body;
            newValue = JSON.stringify(data.body);
            console.log("data = " + message);
            newValue = parseFloat(message);
            console.log("newValue = " + newValue);
        })
    });

    Highcharts.setOptions({
        global: {
            useUTC: false
        }
    });

    Highcharts.chart('humid', {
        chart: {
            type: 'spline',
            animation: Highcharts.svg,
            events: {
                load: function () {
                    let firstSeries = this.series[0];
                    setInterval(function () {
                        let x = (new Date()).getTime();
                        let y = newValue;
                        firstSeries.addPoint([x, y], true, true);
                    }, 20 * 1000);
                }
            }
        },
        title: {
            text: "Humid"
        },
        xAxis: {
            type: 'datetime',
            tickPixelInterval: 150,
            // tickInterval: 30 * 1000
        },
        yAxis: {
            title: {
                text: 'Value'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#191970'
            }]
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br/>' +
                    Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                    Highcharts.numberFormat(this.y, 2);
            }
        },
        legend: {
            enabled: false
        },
        exporting: {
            enabled: false
        },
        series: [{
            name: 'Humid data',
            data: (function () {
                let data = [];
                let time  = (new Date()).getTime();
                let i;

                for (i = -9; i <= 0; i += 1) {
                    data.push({
                        x: time + i * 2000,
                        y: newValue
                    });
                }
                return data;
            }())
        }]
    });
});
