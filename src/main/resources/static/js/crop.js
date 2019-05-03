$(document).ready(function () {

    $("#update").hide();

    $("#save").click(function () {
        let jsonVar = {
            name: $("#name").val(),
            init: $("#init").val(),
            dev: $("#dev").val(),
            mid: $("#mid").val(),
            late: $("#late").val(),
            total: +$("#init").val() + +$("#dev").val() + +$("#mid").val() + +$("#late").val(),
            kcinit: $("#kcinit").val(),
            kcmid: $("#kcmid").val(),
            kcend: $("#kcend").val()
        };

        $.ajax({
            type:"POST",
            url:"http://localhost:8080/crop/all-crop",
            data: JSON.stringify(jsonVar),
            contentType: "application/json",
            success: function(data){

            },
            error: function(err) {
                console.log(err);
                alert(err);
            }
        });
    });

    $('table').on('click', 'button[id="delete"]', function(){
        let trTemp = $(this).closest('tr');
        let id = trTemp.children('td:first').text();

        let del = confirm("Are you sure ?");
        if (del == true) {
            $.ajax({
                type:"DELETE",
                url:"http://localhost:8080/crop/" + id,
                success: function(){
                    trTemp.remove();
                    alertUsing("Delete.", true);
                },
                error: function(err) {
                    console.log(err);
                    alert(err);
                }
            });
        }

    }).on('click', 'button[id="edit"]', function(){
        let id = $(this).closest('tr').children('td:first').text();
        let name = $(this).closest('tr').children('td:nth-child(2)').text();
        let init = $(this).closest('tr').children('td:nth-child(3)').text();
        let dev = $(this).closest('tr').children('td:nth-child(4)').text();
        let mid = $(this).closest('tr').children('td:nth-child(5)').text();
        let late = $(this).closest('tr').children('td:nth-child(6)').text();
        let total = $(this).closest('tr').children('td:nth-child(7)').text();
        let kcinit = $(this).closest('tr').children('td:nth-child(8)').text();
        let kcmid = $(this).closest('tr').children('td:nth-child(9)').text();
        let kcend = $(this).closest('tr').children('td:nth-child(10)').text();

        let row_col2 = $(this).closest('tr').children('td:nth-child(2)');
        let row_col3 = $(this).closest('tr').children('td:nth-child(3)');
        let row_col4 = $(this).closest('tr').children('td:nth-child(4)');
        let row_col5 = $(this).closest('tr').children('td:nth-child(5)');
        let row_col6 = $(this).closest('tr').children('td:nth-child(6)');
        let row_col7 = $(this).closest('tr').children('td:nth-child(7)');


        $("#name").val(name);
        $("#init").val(init);
        $("#dev").val(dev);
        $("#mid").val(mid);
        $("#late").val(late);
        $("#kcinit").val(kcinit);
        $("#kcmid").val(kcmid);
        $("#kcend").val(kcend);

        $("#save").hide();
        $("#update").show().click(function() {

            let initNum = parseInt($("#init").val());
            let devNum = parseInt($("#dev").val());
            let midNum = parseInt($("#mid").val());
            let lateNum = parseInt($("#late").val());
            let kcinitNum = parseFloat($("#kcinit").val());
            let kcmidNum = parseFloat($("#kcmid").val());
            let kcendNum = parseFloat($("#kcend").val());

            let jsonVar = {
                name: $("#name").val(),
                init: initNum,
                dev: devNum,
                mid: midNum,
                late: lateNum,
                kcinit: kcinitNum,
                kcmid: kcmidNum,
                kcend: kcendNum
            };

            console.log(jsonVar);

            $.ajax({
                type:"POST",
                data: JSON.stringify(jsonVar),
                contentType: "application/json",
                url:"http://localhost:8080/crop/" + id,
                success: function(){
                    alertUsing("Update", true);
                    $("#update").hide();
                    $("#save").show();

                    $("#name").val("");
                    $("#init").val("");
                    $("#dev").val("");
                    $("#mid").val("");
                    $("#late").val("");
                    $("#kcinit").val("");
                    $("#kcmid").val("");
                    $("#kcend").val("");

                    $.ajax({
                        type:"GET",
                        contentType: "application/json",
                        url:"http://localhost:8080/crop/" + id,
                        success: function (data) {
                            row_col2.text(data.name);
                            row_col3.text(data.init);
                            row_col4.text(data.dev);
                            row_col5.text(data.mid);
                            row_col6.text(data.late);
                            row_col7.text(data.total);
                        }
                    });
                },
                error: function(err) {
                    console.log(err);
                    alert(err);
                }

            });

        });

    });

    function alertUsing(text, flag) {

        let alert = $(".alert");

        if (flag) {
            alert.removeClass("alert-danger").addClass("alert-success");
        } else {
            alert.removeClass("alert-success").addClass("alert-danger");
        }

        alert.fadeIn(400);
        alert.css("display", "block");
        alert.text(text);
        setTimeout(function() {
            alert.fadeOut();
        }, 2000);
    }

});