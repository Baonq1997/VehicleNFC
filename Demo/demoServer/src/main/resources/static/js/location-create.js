$(document).ready(function () {
    createLocation();
});
function createLocation() {
    $('#btn-save-location').on('click', function (e) {
        var status = $("input[name='status']:checked").val();
        var isActivate = convertStatusToBoolean(status);
        var location = {
            location: $('#location').val(),
            description: $('#description').val(),
            isActivated: isActivate
        }
        $.ajax({
            type: "POST",
            data: JSON.stringify(location),
            url: 'http://localhost:8080/location/save',
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data) {
                console.log(data);
            }, error: function (data) {
                console.log(data);
            }
        })
    });

}
function convertStatusToBoolean(status) {
    if (status === "Available") {
        return true;
    } else {
        return false;
    }
}