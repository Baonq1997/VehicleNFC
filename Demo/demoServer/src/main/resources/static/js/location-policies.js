var locationId;
var locationMap = new Map();
$(document).ready(function () {
    var url = window.location + "";
    console.log("url: " + url);
    var idIndex = url.lastIndexOf("/");
    var id = url.slice(idIndex + 1, url.length);
    console.log("ID: " + id);
    id = id.replace("#","");
    locationId = id;
    // initData(id);
    getLocation();
    loadVehicles();
    filterPolicies(0);
    $('#searchBtn').off().on('click', function (e) {
        e.preventDefault();
       filterPolicies(0);
    });
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
    $('#location-form-search').submit((e) => {
        e.preventDefault();
        searchLocation();
    });
    $('#btn-close').on('click', function (e) {
        location.reload(true);
    });
});

var isShow = false;

function filterBox() {
    isShow = !isShow;
    if (isShow === true) {
        $('#filter').show();
        // $('#filter').attr('display', 'flex');
        // $('#filter').attr('flex-wrap', 'wrap');
    } else {
        $('#filter').hide();
    }
}
function getLocation() {
    var policies = [];
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/location/get/' + locationId,
        success: function (data) {
            $('#location').text(data.location);
            $('#locationId').val(data.id);
            $('#status').text(convertStatus(data.activated));
        }, error: function () {
            alert("Can't load data")
        }
    });
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
                    ul += '<li class="filter-item"> <input type="checkbox" name="vehicle" value="' + data[i].id + '"><label>' + data[i].en_name + '</label></li>'
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
                row += '<span class="label label-success">' + vehicleList[j].vehicleTypeId.en_name + '</span>';
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
    // let locationId = $('#locationId').val();
    let url = "http://localhost:8080/policy/edit?policyId=" + policyId;
    window.location.href = url;
}

function deletePolicy(policyInstanceId) {
    // let locationId = $('#locationId').val();
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
    $('#location-wrapper').show();
    $('#main-wrapper').hide();
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

var locationContain = [];

const setTick = (id, location) => {
    console.log(id);
    if ($('#locationChkbox' + id).is(':checked')) {
        item = '<td><label class="control control--checkbox">\n' +
            '                                <input id="locationChkbox' + id + '" checked type="checkbox" value="' + id + '" name="chk"' +
            ' onclick="setTick(' + id + ',\'' + location + '\')"/>' +
            '<label>' + location + '</label>\n' +
            '                                <div class="control__indicator"></div>\n' +
            '                            </label></td>';
    } else {
        item = '<td> <label class="control control--checkbox">\n' +
            '                                <input id="locationChkbox' + id + '" type="checkbox" value="' + id + '" name="chk"' +
            ' onclick="setTick(' + id + ',\'' + location + '\')"/>' +
            '<label>' + location + '</label>\n' +
            '                                <div class="control__indicator"></div>\n' +
            '                            </label></td>';
    }
    locationMap.set(location, item);
}

function loadColumn(value, element, j) {
    var item = "";
    if (!containsObject(value, existedLocations)) {
        locationContain.push(value);
        item = '<td> <label class="control control--checkbox">\n' +
            '                                <input id="locationChkbox' + value.id + '" type="checkbox" value="' + value.id + '" name="chk"' +
            ' onclick="setTick(' + value.id + ',\'' + value.location + '\')"/>' +
            '<label>' + value.location + '</label>\n' +
            '                                <div class="control__indicator"></div>\n' +
            '                            </label></td>';
        locationMap.set(value.location, item);
        element.append(locationMap.get(value.location));
        j++;
    }
    return j;
}


function convertStatus(status) {
    if (status === true) {
        return "Available";
    } else {
        return "Unavailable";
    }
}

var existedLocations = [];

function loadLocations(policyId) {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/location/get-locations',
        success: function (data) {
            if (data != null) {
                var locations = data.data;
                emptyLocationCheckboxes();
                var i;
                let j = 0;
                let rowNum = 0;
                for (i = 0; i < locations.length; i++) {
                    if (j % 3 === 0) {
                        rowNum = j;
                        if ($(`#locationRow${rowNum}`).length) {

                        } else {
                            $("#locations").append('<tr id="locationRow' + rowNum + '">\n' +

                                '                    </tr>');
                        }
                    }
                    j = loadColumn(locations[i], $(`#locationRow${rowNum}`), j);
                }
            }

        }, error: function (data) {
            console.log(data);
        }
    });
    // $('#addLocationModal').modal();
    addPolicyToLocation(policyId);
}

// function loadLocations(policyId) {
//     // getExistedLocations(policyId);
//     $.ajax({
//         type: "GET",
//         dataType: "json",
//         url: 'http://localhost:8080/location/get-locations',
//         success: function (data) {
//             if (data != null) {
//                 var locations = data.data;
//                 emptyLocationCheckboxes();
//                 for (let i = 0; i < locations.length; i++) {
//                     var item = "";
//                     if (containsObject(locations[i], existedLocations)) {
//                         item = ' <label class="control control--checkbox">\n' +
//                             '                                <input type="checkbox" value="' + locations[i].id + '" name="chk" checked="checked"/> ' +
//                             '<label>' + locations[i].location + '</label>\n' +
//                             '                                <div class="control__indicator"></div>\n' +
//                             '                            </label>';
//                         $('#locations').append(item);
//                     } else {
//                         item = ' <label class="control control--checkbox">\n' +
//                             '                                <input type="checkbox" value="' + locations[i].id + '" name="chk"/>' +
//                             '<label>' + locations[i].location + '</label>\n' +
//                             '                                <div class="control__indicator"></div>\n' +
//                             '                            </label>';
//                         $('#locations').append(item);
//                     }
//                 }
//             }
//
//         }, error: function (data) {
//             console.log(data);
//         }
//     });
//     // $('#btn-save-locations').on('click', function (e) {
//     // $('#addLocationModal').modal();
//     addPolicyToLocation(policyId);
//     // });
// }

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
        // if (existedLocations.length === 0) {
        //     locations = locationArr;
        // } else {
        //     for (let i = 0; i < locationArr.length; i++) {
        //         for (let j = 0; j < existedLocations.length; j++) {
        //             var checkedLocation = locationArr[i];
        //             var existedLocation = existedLocations[j];
        //             if (!containsObject(existedLocation, locationArr)) {
        //                 var temp = {
        //                     id: existedLocation.id,
        //                     location: existedLocation.location,
        //                     isDelete: "true"
        //
        //                 }
        //                 if (!containsObject(temp, locations)) {
        //                     locations.push(temp);
        //                 }
        //             } else {
        //                 var temp = {
        //                     id: checkedLocation.id,
        //                     location: checkedLocation.location,
        //                     isDelete: "false"
        //                 }
        //                 if (!containsObject(temp, locations)) {
        //                     locations.push(temp);
        //                 }
        //             }
        //         }
        //     }
        // }

        var jsonObject = {
            policyId: policyId,
            locationArr: locationArr,
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

    // $('#filter-btn').on('click', function (e) {
    var vehicleTypeArr = [];
    // var locationId = $('#locationId').val();
    var url = "http://localhost:8080/policy/filter-policies?locationId=" + locationId;
    if (pageNumber != null) {
        url = url + "&page=" + pageNumber;
    }
    // var allowedParkingFrom = $('#allowedParkingFrom').val();
    // var allowedParkingTo = $('#allowedParkingTo').val();
    var vehicleTypes = $('input[name=vehicle]:checked').map(function (i) {
        var vehicleType = {
            id: this.value,
            en_name: $(this).next('label').text()
        }
        vehicleTypeArr.push(vehicleType.en_name);
        return this;
    }).get();
    console.log("ARR" + vehicleTypeArr);
    var listSearchParam = [];
    if (vehicleTypeArr != null && vehicleTypeArr.length > 0) {

        var vehicleTypes = createSearchObject("vehicleTypes", ":", vehicleTypeArr);
        listSearchParam.push(vehicleTypes);
    }
    $.ajax({
        type: 'POST',
        url: url,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify(listSearchParam),
        success: function (response) {

            emptyTable();
            emptyLi();
            // emptyPaginationLi();
            loadTableAfterFilter(response);
            console.log(response);
        }, error: function (res) {
            console.log(res);
        }
    });
    // });
}

function emptyTable() {
    $('#location-policies tbody').empty();
}

function loadTableAfterFilter(response) {
    var data = response.data;
    if (data != null) {
        if (data.length != 0) {

            for (var i = 0; i < data.length; i++) {
                var vehicleList = data[i].policyHasTblVehicleTypes;
                row = '<tr>';
                row += '<td>' + (i + (response.pageNumber * response.pageSize) + 1) + '</td>';
                row += '<td>' + msToTime(data[i].allowedParkingFrom) + ' - ' + msToTime(data[i].allowedParkingTo) + '</td>';
                if (vehicleList != null || vehicleList.length != 0) {
                    row += '<td class="vehicle-tags">';
                    for (let j = 0; j < vehicleList.length; j++) {
                        row += '<span class="label label-success">' + vehicleList[j].vehicleTypeId.en_name + '</span>';
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
            var pageNumber = response.pageNumber;
            $('#pagination').append(createPageButton(0, 'First', false, false));
            $('#pagination').append(createPageButton((pageNumber - 1), '<', (pageNumber < 1), false));
            if (pageNumber > 2) {

                $('#pagination').append(createEtcButton());
            }
            for (var currentPage = 0; currentPage < response.totalPages; currentPage++) {
                if (currentPage > response.pageNumber - 3 && currentPage < response.pageNumber + 3) {
                    $('#pagination').append(createPageButton(currentPage, (currentPage + 1), false, (currentPage === pageNumber)));
                }
            }
            if (response.totalPages - pageNumber > 3) {
                $('#pagination').append(createEtcButton());
            }
            $('#pagination').append(createPageButton((pageNumber + 1), '>', (pageNumber === response.totalPages - 1), false));
            $('#pagination').append(createPageButton((response.totalPages - 1), 'Last', false, false));
        } else {
            var row = '<td colspan="4" style="text-align: center;"><strong> No data </strong></td>';
            $('#location-policies tbody').append(row);
        }
    }else {
        var row = '<td colspan="4" style="text-align: center;"><strong> No data </strong></td>';
        $('#location-policies tbody').append(row);
    }
}

function emptyLocationCheckboxes() {
    $('#locations').empty();
}

function emptyLi() {
    $('#pagination').empty();
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
function createPageButton(pageNumber, label, isDisable, isActive) {
    var className = (isActive) ? 'nav-item active' : 'nav-item';
    className += (isDisable) ? ' disabled-href' : '';
    return '<li class="' + className + '">\n<a href="#" class="nav-link" onclick="filterPolicies(' + pageNumber + ')">' + label + '</a>\n' +
        '</li>';
}
function createEtcButton() {
    return '<li class="etc ">...</li>';
}
function msToTime(ms) {
    var seconds = parseInt(ms / 1000);
    var minutes = parseInt(seconds / 60, 10);
    seconds = seconds % 60;
    var hours = parseInt(minutes / 60, 10);
    minutes = minutes % 60;

    return hours + ':' + minutes;
}
function searchLocation() {

    // var listSearchObject = [];
    // var url = "http://localhost:8080/location/filter?page=0";
    var searchValue = $('#locationName').val();
    // var searchObject = createSearchObject("location", ":", searchValue);
    // listSearchObject.push(searchObject);

    emptyLocationCheckboxes();

    let j = 0;
    let rowNum = 0;
    for (var [location, item] of locationMap.entries()) {
        if (j % 3 === 0) {
            rowNum = j;
            if ($(`#locationRow${rowNum}`).length) {

            } else {
                $("#locations").append('<tr id="locationRow' + rowNum + '">\n' +

                    '                    </tr>');
            }
        }
        if (location.includes(searchValue)) {
            $(`#locationRow${rowNum}`).append(item);
            j++
        }
    }

    // $.ajax({
    //     type: "POST",
    //     contentType: "application/json; charset=utf-8",
    //     dataType: "json",
    //     data: JSON.stringify(listSearchObject),
    //     url: url,
    //     success: function (data) {
    //         emptyLocationCheckboxes();
    //         var locations = data.data;
    //         for (var i = 0; i < locations.length; i++) {
    //             item = ' <label class="control control--checkbox">\n' +
    //                 '                                <input type="checkbox" value="' + locations[i].id + '" name="chk"/>' +
    //                 '<label>' + locations[i].location + '</label>\n' +
    //                 '                                <div class="control__indicator"></div>\n' +
    //                 '                            </label>';
    //             $('#locations').append(item);
    //         }
    //         // loadLocations(policy);
    //     }, error: function (data) {
    //         console.log(data);
    //     }
    // });
    addPolicyToLocation(policy);
}