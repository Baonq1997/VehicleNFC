$(document).ready(function () {
    initPolicies();

});
var listPolicyJson = [];

function initPolicies() {
    var policies = [];
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/policy-instance/policies-instances",
        dataType: "json",
        success: function (data) {
            if (data != null) {
                for (let i = 0; i < data.length; i++) {
                    policies.push(data);
                    // getVehicleByPolicyId(data.id);
                }
                loadTable(data)
                listPolicyJson.push(policies);
            }

        }, error: function (data) {
            console.log(data);
        }
    })
}
function emptyLocationCheckboxes() {
    $('.control-group location').empty();
}
function loadTable(data) {
    if (data != null) {
        for (var i = 0; i < data.length; i++) {
            var vehicleList = data[i].policyInstanceHasTblVehicleTypes;
            row = '<tr>';
            row += '<td>' + data[i].id + '</td>';
            row += '<td>' + msToTime(data[i].allowedParkingFrom) + ' - ' + msToTime(data[i].allowedParkingTo) + '</td>';
            if (vehicleList != null || vehicleList.length != 0) {
                row += '<td class="vehicle-tags">';
                for (let j = 0; j < vehicleList.length; j++) {
                    row += '<span class="badge badge-success">' + vehicleList[j].vehicleTypeId.name + '</span>';
                }

            } else {
                row += '<td> N/A </td>'
            }
            row += '</td>'+ data[i].locationId + '</td>';
            row += '<td> <button class="btn btn-success" onclick="getExistedLocations(' + data[i].id + ')">Add To Location</button>';
            row += '<td> <button class="btn btn-primary" onclick="editPolicy(' + data[i].id + ')">Edit</button>';
            row += '<td> <button class="btn btn-danger" onclick="deletePolicy(' + data[i].id + ')">Delete</button>';
            row += '</tr>';
            $('#policies-table tbody').append(row);
        }
    } else {
        var row = '<td colspan="4"><strong> No data </strong></td>';
        $('#policies-table tbody').append(row);
    }
}
var existedLocations = [];
function getExistedLocations(policyId) {
    existedLocations = [];
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/location/locations?policyId=' + policyId,
        success: function (data) {
            if (data != null) {
                for (let i = 0; i < data.length; i++) {
                    existedLocations.push(data[i]);
                }
                loadLocations(policyId)
            }
        }, error: function (data) {
            console.log(data);
        }
    });
}

function loadLocations(policyId) {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/location/get-locations',
        success: function (data) {
            if (data != null) {
                var locations = data.data;
                emptyLocationCheckboxes();
                for (let i = 0; i < locations.length; i++) {
                    var item = "";
                    if (containsObject(locations[i], existedLocations)) {
                        item = ' <label class="control control--checkbox">\n' +
                            '                                <input type="checkbox" value="' + locations[i].id + '" name="chk" checked="checked"/> ' +
                            '<label>' + locations[i].location + '</label>\n' +
                            '                                <div class="control__indicator"></div>\n' +
                            '                            </label>';
                        $('#locations').append(item);
                    } else {
                        item = ' <label class="control control--checkbox">\n' +
                            '                                <input type="checkbox" value="' + locations[i].id + '" name="chk"/>' +
                            '<label>' + locations[i].location + '</label>\n' +
                            '                                <div class="control__indicator"></div>\n' +
                            '                            </label>';
                        $('#locations').append(item);
                    }
                }
            }

        }, error: function (data) {
            console.log(data);
        }
    });
    $('#addLocationModal').modal();
    addPolicyToLocation(policyId);
}

var locationArr = [];
function addPolicyToLocation(policyId) {
    $('#btn-save-locations').on('click', function () {
        var temp = $('input[name=chk]:checked').map(function (i) {
            var location = {
                id: this.value,
                location: $(this).next('label').text()
            }
            locationArr.push(location);
            return this;
        }).get();
        let locations = [];
        if (existedLocations.length === 0) {
            locations = locationArr;
        } else {
            for (let i = 0; i <locationArr.length; i++) {
                for (let j = 0; j < existedLocations.length; j++) {
                    var checkedLocation = locationArr[i];
                    var existedLocation = existedLocations[j];
                    if (!containsObject(existedLocation, locationArr)) {
                        var temp = {
                            id: existedLocation.id,
                            location: existedLocation.location,
                            isDelete: "true"

                        }
                        if(!containsObject(temp, locations)) {
                            locations.push(temp);
                        }
                    } else {
                        var temp = {
                            id: checkedLocation.id,
                            location: checkedLocation.location,
                            isDelete: "false"
                        }
                        if(!containsObject(temp, locations)) {
                            locations.push(temp);
                        }
                    }
                }
            }
        }

        var jsonObject = {
            policyId: policyId,
            locationArr: locations,
            currentLocationId: existedLocations
        }

        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(jsonObject),
            url: 'http://localhost:8080/location/add-policy',
            success: function (data) {
                $('#addLocationModal').modal('hide');
                location.reload(true);
            }, error: function (data) {
                console.log(data);
            }
        });
    });
}

function editPolicy(policyInstanceId) {
    let locationId = $('#locationId').val();
    let url = "http://localhost:8080/policy-instance/edit?policyInstanceId=" + policyInstanceId;
    window.location.href = url;
}

function deletePolicy(policyInstanceId) {
    let locationId = $('#locationId').val();
    $.ajax({
        type: "POST",
        // url: 'http://localhost:8080/policy/delete-by-location-policy?locationId=' + locationId + '&policyId=' + policyId,
        url: 'http://localhost:8080/policy-instance/delete-policy-instance?policyInstanceId=' + policyInstanceId,
        success: function (data) {
            console.log("Delete Successfully");
            location.reload(true);
        }, error: function (data) {
            console.log(data);
        }
    })
}

function emptyLocationCheckboxes() {
    $('#locations').empty();
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

function msToTime (ms) {
    var seconds = parseInt(ms/1000);
    var minutes = parseInt(seconds/60, 10);
    seconds = seconds%60;
    var hours = parseInt(minutes/60, 10);
    minutes = minutes%60;

    return hours + ':' + minutes;
}
