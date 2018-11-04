$(document).ready(function () {
    var url = window.location + "";
    console.log("url: " + url);
    var idIndex = url.lastIndexOf("/");
    var id = url.slice(idIndex + 1, url.length);
    console.log("ID: " + id);
    initData(id);
    // addLocations();
});

function initData(locationId) {
    var policies = [];
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/location/get/' + locationId,
        success: function (data) {
            if (data != null) {
                loadTable(data);
            }

            // emptyTable();
            // emptyLi();
            // loadPolicyTable(data);
        }, error: function () {
            alert("Can't load data")
        }
    });
    // console.log(policiesListJson);
}


function loadTable(data) {
    var policyList = data.policyInstanceList;
    $('#location').text(data.location);
    $('#locationId').val(data.id);
    if (data.isActivated === true) {
        $('#status').text("Active");

    } else {
        $('#status').text("De-active");
        // var h1 = 'This location is de-active.'
        // $('#policies').append();
    }
    for (let i = 0; i < policyList.length; i++) {
        var vehicleList = policyList[i].policyInstanceHasTblVehicleTypes;
        row = '<tr>';
        row += '<td>' + policyList[i].id + '</td>';
        row += '<td>' + msToTime(policyList[i].allowedParkingFrom) + ' - ' + msToTime(policyList[i].allowedParkingTo) + '</td>';
        if (vehicleList != null || vehicleList.length != 0) {
            // row += "<td><ul>";
            // for (let j = 0; j < vehicleList.length; j++) {
            //     if (j == vehicleList.length - 1 ) {
            //         row += '<li>' + vehicleList[j].vehicleTypeId.name + '</li>';
            //     } else {
            //         row += '<li>' + vehicleList[j].vehicleTypeId.name + ',   </li>';
            //     }
            //
            // }
            // row += "</ul></td>";

            row += '<td class="vehicle-tags">';
            for (let j = 0; j < vehicleList.length; j++) {
                row += '<span class="badge badge-success">' + vehicleList[j].vehicleTypeId.name + '</span>';
            }
            row += '</td>';
        } else {
            row += '<td> Empty </td>'
        }
        // row += '<td> <button class="editBtn" onclick="Edit('+ data[i].policyId +', ' + data[i].vehicleTypeId.id + ', '+ locationId +')">Edit</button>';
        row += '<td> <button class="btn btn-success" onclick="getExistedLocations(' + policyList[i].id + ')">Add To Location</button>';
        row += '<td> <button class="btn btn-primary" onclick="editPolicy(' + policyList[i].id + ')">Edit</button>';
        row += '<td> <button class="btn btn-danger" onclick="deletePolicy(' + policyList[i].id + ')">Delete</button>';
        row += '</tr>';
        $('#location-policies tbody').append(row);
    }

}

function editPolicy(policyInstanceId) {
    let locationId = $('#locationId').val();
    let url = "http://localhost:8080/policy-instance/edit?policyInstanceId=" + policyInstanceId + "&locationId=" + locationId;
    window.location.href = url;
}

function deletePolicy(policyInstanceId) {
    let locationId = $('#locationId').val();
    $.ajax({
        type: "POST",
        // url: 'http://localhost:8080/policy/delete-by-location-policy?locationId=' + locationId + '&policyId=' + policyId,
        url: 'http://localhost:8080/policy-instance/delete-by-location-policy?locationId=' + locationId + '&policyInstanceId=' + policyInstanceId,
        success: function (data) {
            console.log("Delete Successfully");
            location.reload(true);
        }, error: function (data) {
            console.log(data);
        }
    })
}

// function AddToLocation(policyId) {
//     let locationId = $('#locationId').val();
//     $.ajax({
//         type: "POST",
//         url: 'http://localhost:8080/location/add-policy?policyId=' + policyId + '&locationId=' + locationId,
//         success: function (data) {
//             console.log("Add Successfully");
//         }, error: function (data) {
//             console.log(data);
//         }
//     })
// }

function convertTime(dateTypeLong) {
    if (dateTypeLong === null) {
        return "Empty";
    }
    var dateStr = new Date(dateTypeLong),
        dformat =
            [dateStr.getHours(),
                dateStr.getMinutes()].join(':');
    return dformat;
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
    // getExistedLocations(policyId);
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
                        $('.control-group').append(item);
                    } else {
                        item = ' <label class="control control--checkbox">\n' +
                            '                                <input type="checkbox" value="' + locations[i].id + '" name="chk"/>' +
                            '<label>' + locations[i].location + '</label>\n' +
                            '                                <div class="control__indicator"></div>\n' +
                            '                            </label>';
                        $('.control-group').append(item);
                    }
                }
            }

        }, error: function (data) {
            console.log(data);
        }
    });
    // $('#btn-save-locations').on('click', function (e) {
    $('#addLocationModal').modal();
    addPolicyToLocation(policyId);
    // });
}
var locationArr =[];
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
                            // name: "true",
                            isDelete: "true"

                        }
                        if(!containsObject(temp, locations)) {
                            locations.push(temp);
                            // break;
                        }


                    } else {
                        var temp = {
                            id: checkedLocation.id,
                            location: checkedLocation.location,
                            // name: "false",
                            isDelete: "false"
                        }
                        if(!containsObject(temp, locations)) {
                            locations.push(temp);
                            // break;
                        }
                        // vehicleArr.push(temp);
                        // break;
                    }
                }
            }
        }
        
        var jsonObject = {
            policyId: policyId,
            locationArr: locations
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

function emptyLocationCheckboxes() {
    $('.control-group-location').empty();
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
// function getVehiclesByPolicy(policyId) {
//     var
//     $.ajax({
//         type: "GET",
//         dataType: "json",
//         // contentType: "application/json",
//         // data: JSON.stringify(content),
//         url: 'http://localhost:8080/policy-vehicleType/get-vehicleTypes/' + policyId,
//         success: function (data) {
//             console.log("Policies: " + data);
//
//             loadPolicy(data,locationId);
//         }, error: function () {
//             console.log("Could not load policy")
//         }
//     });
// }