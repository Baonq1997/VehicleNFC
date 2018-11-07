
$('#save-user-form').on('submit', function (e) {
    if ($('#id').val() === '') {
        $('#id').removeAttr('value');
    }
    e.preventDefault();
    $.ajax({
        type: 'post',
        url: 'create-user',
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: buildUserJSON(),
        success: function (data) {
            $('#save-user-form').trigger("reset");
        }, error: function (data) {
            alert("Cannot create User")
            console.log(data);
        }
    });
});

function buildUserJSON() {
    var string = {
        id: $('#id').val(),
        phoneNumber: $('#phoneNumber').val(),
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        password: $('#password').val(),
        vehicle: {
            "vehicleNumber": $('#vehicleNumber').val(),
            "licensePlateId": $('#licensePlateId').val()
        }
    };
    return JSON.stringify(string);
}




