var vehicleTypeArr = [];
var vehicleExistedArr= [];
var locationArr = [];
var locationExistedArr= [];
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
                    var chk = '<input type="checkbox" class="locations" name="locationChk" id="location-' + i + '" value="' + data[i].id + '"><label>' + data[i].location + '</label>';
                    $('#locationArr').append(chk);
                    if (containsObject(data,locationArr)) {
                        if (!containsObject(data, locationExistedArr)) {
                            locationExistedArr.push(data);
                        }
                    }
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

function parseTimeToLong(clockPicker, type) {
    var time = $('.' + clockPicker + ' #' + type).val();
    var temp = time.split(":")
    var hour = temp[0];
    var minute = temp[1];
    var ms = parseInt(hour * 3600000) + parseInt(minute * 60000);
    // console.log(ms);
    $('#allowed' + type).val(ms);
}


function savePolicyVehicle() {
    $('#save-policy-vehicle').on('click', function () {
        vehicleTypeArr = [];
        var vehicleTypes = $('input[name=chk]:checked').map(function (i) {
            var vehicleType = {
                id: this.value,
                name: $(this).next('label').text()
            }
            vehicleTypeArr.push(vehicleType);
            return this;
        }).get();
        let vehicleArr = [];
        if (vehicleExistedArr.length === 0) {
            vehicleArr = vehicleTypeArr;
        } else {
            for (let i = 0; i <vehicleTypeArr.length; i++) {
                for (let j = 0; j < vehicleExistedArr.length; j++) {
                    var checkedVehicle = vehicleTypeArr[i];
                    var vehicleExisted = vehicleExistedArr[j];
                    if (!containsObject(vehicleExisted, vehicleTypeArr)) {
                        var temp = {
                            id: vehicleExisted.id,
                            name: vehicleExisted.name,
                            isDelete: "true"

                        }
                        if(!containsObject(temp, vehicleArr)) {
                            vehicleArr.push(temp);
                        }
                    } else {
                        var temp = {
                            id: checkedVehicle.id,
                            name: checkedVehicle.name,
                            isDelete: "false"
                        }
                        if(!containsObject(temp, vehicleArr)) {
                            vehicleArr.push(temp);
                        }
                    }
                }
            }
        }

        var policyJson = {
            id: policyId,
            allowedParkingFrom: $('#allowedParkingFrom').val(),
            allowedParkingTo: $('#allowedParkingTo').val()
        }
        var json = {
            locationId: locationId,
            policy: policyJson,
            vehicleTypes: vehicleArr
        }
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(json),
            url: 'http://localhost:8080/policy/create',
            success: function (data) {
                console.log("Save successfully");
                console.log(data);
                $('#policyId').val(data.id);
                $('.pricing-container').show();
                $('.pricing-container').empty();
                $('.button-wrapper').show();

                policyId = data.id;
                createPricingTabs(data.id);
                $('#vehicleTypeArr').empty();
                loadVehiclesCheckedBoxes();
            }, error: function (data) {
                console.log("Could not save policy vehicle")
            }
        });
    });
}

function containsObject(obj, list) {
    var i;
    for (i = 0; i < list.length; i++) {
        if (parseInt(list[i].id) === obj.id) {
            return true;
        }
    }
    return false;
}
