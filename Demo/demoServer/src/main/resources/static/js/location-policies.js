$(document).ready(function () {
    var url = window.location + "";
    console.log("url: " + url);
    var idIndex = url.lastIndexOf("/");
    var id = url.slice(idIndex + 1, url.length);
    console.log("ID: " + id);
    initData(id);
    loadVehicles();
    filterPolicies();
    // addLocations();
    $('.clockpickerFrom').clockpicker({
        placement: 'bottom',
        align: 'left',
        donetext: 'Done',
        afterDone: function () {
            parseTimeToLong("clockpickerFrom", "ParkingFrom");
        }
    });
    $('.clockpickerTo').clockpicker({
        placement: 'bottom',
        align: 'left',
        donetext: 'Done',
        afterDone: function () {
            parseTimeToLong("clockpickerTo", "ParkingTo");
        }
    });
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
        }, error: function () {
            alert("Can't load data")
        }
    });
    // console.log(policiesListJson);
}

function loadVehicles() {
    $.ajax({
        type: "GET",
        data: "json",
        url: 'http://localhost:8080/vehicle-type/get-all',
        success: function (data) {
            if (data != null) {
                var ul = "<ul>";
                for (var i = 0; i < data.length; i++) {
                    ul += '<li> <input type="checkbox" name="vehicle" value="' + data[i].id + '"><label>' + data[i].name + '</label></li>'
                }
                ul += '</ul>';
                $('#filter').append(ul);
            }
        }, error: function (data) {
            console.log(data);
        }
    })
}

function loadTable(data) {
    var policyList = data.policyList;

    $('#location').text(data.location);
    $('#locationId').val(data.id);
    var index = 0;
    for (let i = 0; i < policyList.length; i++) {
        var vehicleList = policyList[i].policyHasTblVehicleTypes;
        row = '<tr>';
        row += '<td>' + (++index) + '</td>';
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
        row += '<td> <a href="#" class="btn btn-primary btnAction" onclick="getExistedLocations(' + policyList[i].id + ')"><i class="fas fa-plus-square"></i></a>';
        row += '<a href="#" class="btn btn-primary btnAction" onclick="editPolicy(' + policyList[i].id + ')"><i class="lnr lnr-pencil"></i> </a>';
        row += '<a href="#" class="btn btn-danger btnAction" onclick="deletePolicy(' + policyList[i].id + ')"><i class="lnr lnr-trash"></i></a></td>';
        row += '</tr>';
        $('#location-policies tbody').append(row);
    }
}

function editPolicy(policyId) {
    let locationId = $('#locationId').val();
    let url = "http://localhost:8080/policy/edit?policyId=" + policyId;
    window.location.href = url;
}

function deletePolicy(policyInstanceId) {
    let locationId = $('#locationId').val();
    $.ajax({
        type: "POST",
        // url: 'http://localhost:8080/policy/delete-by-location-policy?locationId=' + locationId + '&policyId=' + policyId,
        url: 'http://localhost:8080/policy/delete-by-location-policy?locationId=' + locationId + '&policyId=' + policyInstanceId,
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
var existedLocations = [];
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

function filterPolicies(pageNumber) {

    $('#filter-btn').on('click', function (e) {
        var vehicleTypeArr = [];
        var locationId = $('#locationId').val();
        var url = "http://localhost:8080/policy/filter-policies?locationId="+locationId;
        if (pageNumber != null) {
            url = url+"&page="+pageNumber;
        }
        // var allowedParkingFrom = $('#allowedParkingFrom').val();
        // var allowedParkingTo = $('#allowedParkingTo').val();
        var vehicleTypes = $('input[name=vehicle]:checked').map(function (i) {
            var vehicleType = {
                id: this.value,
                name: $(this).next('label').text()
            }
            vehicleTypeArr.push(vehicleType.name);
            return this;
        }).get();
        console.log("ARR" + vehicleTypeArr);
        var listSearchParam = [];
        // if (allowedParkingFrom !== "") {
        //     var allowedParkingFromObj = createSearchObject("allowedParkingFrom", ">", allowedParkingFrom);
        //     listSearchParam.push(allowedParkingFromObj);
        // }
        // if (allowedParkingTo !== "") {
        //     var allowedParkingToObj = createSearchObject("allowedParkingTo", "<", allowedParkingTo);
        //     listSearchParam.push(allowedParkingToObj);
        // }
        if (vehicleTypeArr!= null && vehicleTypeArr.length > 0) {

            var vehicleTypes = createSearchObject("vehicleTypes", ":", vehicleTypeArr);
            listSearchParam.push(vehicleTypes);
        }
        $.ajax({
            type:'POST',
            url: url,
            dataType:"json",
            contentType: 'application/json',
            data: JSON.stringify(listSearchParam),
            success:function(response){

                emptyTable();
                // emptyPaginationLi();
                loadTableAfterFilter(response);
                console.log(response);
            }, error: function (res) {
                console.log(res);
            }
        });
    });
}

function emptyTable() {
    $('#location-policies tbody').empty();
}
function loadTableAfterFilter(response) {
    var data = response.data;
    if (data != null) {
        for (var i = 0; i < data.length; i++) {
            var vehicleList = data[i].policyHasTblVehicleTypes;
            row = '<tr>';
            row += '<td>' + data[i].id + '</td>';
            row += '<td>' + msToTime(data[i].allowedParkingFrom) + ' - ' + msToTime(data[i].allowedParkingTo) + '</td>';
            if (vehicleList != null || vehicleList.length != 0) {
                row += '<td class="vehicle-tags">';
                for (let j = 0; j < vehicleList.length; j++) {
                    row += '<span class="badge badge-success">' + vehicleList[j].vehicleTypeId.name + '</span>';
                }
                row += '</td>';
            } else {
                row += '<td> N/A </td>'
            }
            row += '<td> <a href="#" class="btn btn-primary btnAction" onclick="getExistedLocations(' + data[i].id + ')"><i class="fas fa-plus-square"></i></a>';
            row += '<a href="#" class="btn btn-primary btnAction" onclick="editPolicy(' + data[i].id + ')"><i class="lnr lnr-pencil"></i> </a>';
            row += '<a href="#" class="btn btn-danger btnAction" onclick="deletePolicy(' + data[i].id + ')"><i class="lnr lnr-trash"></i></a></td>';
            row += '</tr>';
            $('#location-policies tbody').append(row);
        }
    } else {
        var row = '<td colspan="4" style="text-align: center;"><strong> No data </strong></td>';
        $('#location-policies tbody').append(row);
    }
}

function emptyLocationCheckboxes() {
    $('#locations').empty();
}

function createSearchObject(key, operation, value) {
    var obj = {
        key: key,
        operation: operation,
        value: value
    };
    return obj;
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
function parseTimeToLong(clockPicker, type) {
    var time = $('.' + clockPicker + ' #' + type).val();
    var temp = time.split(":")
    var hour = temp[0];
    var minute = temp[1];
    var ms = (parseInt(hour * 3600000) + parseInt(minute * 60000));
    $('#allowed' + type).val(ms);
}
function msToTime (ms) {
    var seconds = parseInt(ms/1000);
    var minutes = parseInt(seconds/60, 10);
    seconds = seconds%60;
    var hours = parseInt(minutes/60, 10);
    minutes = minutes%60;

    return hours + ':' + minutes;
}
