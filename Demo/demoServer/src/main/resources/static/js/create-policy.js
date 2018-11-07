var vehicleTypeArr = [];
var vehicleExistedArr= [];
$(document).ready(function () {

    loadVehicleTypes();
    loadLocations();

    $('.clockpickerFrom').clockpicker({
        placement: 'bottom',
        align: 'left',
        donetext: 'Done',
        afterDone: function () {
            console.log("after done");
            parseTimeToLong("clockpickerFrom", "ParkingFrom");
        }
    });
    $('.clockpickerTo').clockpicker({
        placement: 'bottom',
        align: 'left',
        donetext: 'Done',
        afterDone: function () {
            console.log("after done");
            parseTimeToLong("clockpickerTo", "ParkingTo");
        }
    });
});

function loadLocations() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/location/get-locations',
        success: function (res) {
            if (res != null) {
                var data = res.data;
                console.log("VehicleTypes: " + data);
                for (i = 0; i < data.length; i++) {
                    var chk = '<input type="checkbox" class="locations" name="chk" id="location-' + i + '" value="' + data[i].id + '"><label>' + data[i].location + '</label>';
                    $('#locationArr').append(chk);
                }
            }

        }, error: function (data) {
            console.log("Could not load vehicle types")
        }
    });
}
function loadVehicleTypes() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/vehicle-type/get-all',
        success: function (data) {
            console.log("VehicleTypes: " + data);
            for (i = 0; i < data.length; i++) {
                var chk = '<input type="checkbox" class="vehicles" name="chk" id="vehicleType-' + i + '" value="' + data[i].id + '"><label>' + data[i].name + '</label>';
                $('#vehicleTypeArr').append(chk);
            }
        }, error: function (data) {
            console.log("Could not load vehicle types")
        }
    });
}
function loadVehiclesCheckedBoxes() {
    // $('#vehicleTypeArr input').remove();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/vehicle-type/get-all',
        success: function (data) {
            console.log("VehicleTypes: " + data);
            for (i = 0; i < data.length; i++) {
                let vehicleType = data[i];
                console.log("ARR: "+vehicleTypeArr.length);
                // var checkedVehicles =
                console.log("OBJ: "+vehicleType);
                let isFound = false;
                for (let j = 0; j < vehicleTypeArr.length; j++) {
                    if (parseInt(vehicleTypeArr[j].id) === vehicleType.id) {
                        isFound = true;
                        let existedVehicle = {
                            id: data[i].id,
                            name: data[i].name
                            // existed: true
                        }
                        if (!containsObject(existedVehicle,vehicleExistedArr)) {
                            vehicleExistedArr.push(existedVehicle);
                        }

                    }
                }
                let chk = "";
                if (isFound) {
                    chk =  '<input type="checkbox" name="chk" id="vehicleType-' + i + '" value="' + data[i].id + '" checked><label>' + data[i].name + '</label>';

                } else {
                    chk = '<input type="checkbox" existed="false" name="chk" id="vehicleType-' + i + '" value="' + data[i].id + '"><label>' + data[i].name + '</label>';
                }
                $('#vehicleTypeArr').append(chk);

            }
        }, error: function (data) {
            console.log(data);
            console.log("Could not load vehicle types")
        }
    });
}