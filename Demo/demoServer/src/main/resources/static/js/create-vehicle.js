function setLongFromExpireDate() {
    var time = $('#datepicker').val().split("-");
    var date = new Date(time[1] + "-" + time[0] + "-" + time[2]);
    $('#expireDate').val(date.getTime());
}

$('#datepicker').datepicker({
    weekStart: 1,
    autoclose: true,
    format: "dd-mm-yyyy",
    todayHighlight: true,
});

$('#create-vehicle-form').on('submit', function (e) {
    let vehicleNumber = $('#vehicleNumberShow').val();
    $('#vehicleNumber').val(vehicleNumber);

    let licensePlate = $('#licenseIdShow').val();
    $('#licenseId').val(licensePlate);
    $.ajax({
        type: 'post',
        url: 'save-vehicle',
        data: $('#create-vehicle-form').serialize(),
        success: function (data) {
            if (data) {
                location.reload(true);
            }
        }
    });
    e.preventDefault();
});

function setUpVehicleType(list) {
    $("#vehicle-list").empty();
    for (var i = 0; i < list.length; i++) {
        var option = "<option value='" + list[i].id + "'>" + list[i].name + "</option>";
        $('#vehicle-list').append(option);
    }
}
$(document).ready(function () {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: '/vehicle-type/get-all',
        success: function (data) {
            setUpVehicleType(data)
        }, error: function () {
            alert("Can't load data")
        }
    });
});
