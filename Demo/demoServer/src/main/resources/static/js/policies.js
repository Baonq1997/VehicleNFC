$(document).ready(function () {
    loadVehicles();
    filterPolicies(0);
    $('#searchBtn').off().on('click', function (e) {
        searchValue = $('#searchValue').val();
        filterPolicies(0);
    });

    $('#btn-close').on('click', function (e) {
        location.reload(true);
    });
    $('#searchLocationBtn').on('click', function (e) {
        e.preventDefault()
        searchLocation();
    });
});

var isShow = false;
function filterBox() {
    isShow = !isShow;
    if (isShow === true) {
        $('#filter').show();
        // $('#filter').attr('display', 'flex');
        // $('#filter').attr('flex-wrap', 'wrap');
    }else {
        $('#filter').hide();
    }
}
var listPolicyJson = [];
var searchValue = "";

function filterPolicies(pageNumber) {

    var vehicleTypeArr = [];
    var locationId = $('#locationId').val();
    var url = "http://localhost:8080/policy/filter-policies";
    if (pageNumber != null) {
        url = url + "?page=" + pageNumber;
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

    var listSearchParam = [];
    if (vehicleTypeArr != null && vehicleTypeArr.length > 0) {
        var vehicleTypes = createSearchObject("vehicleTypes", ":", vehicleTypeArr);
        listSearchParam.push(vehicleTypes);
    }
    var locationObj = createSearchObject("locationId", ":", searchValue);
    listSearchParam.push(locationObj);
    $.ajax({
        type: 'POST',
        url: url,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify(listSearchParam),
        success: function (response) {

            emptyTable();
            emptyPaginationLi();
            loadData(response);
            console.log(response);
        }, error: function (res) {
            console.log(res);
        }
    });
}

function initPolicies() {
    var policies = [];
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/policy/policies",
        dataType: "json",
        success: function (data) {
            if (data != null) {
                for (let i = 0; i < data.length; i++) {
                    policies.push(data);
                    // getVehicleByPolicyId(data.id);
                }
                loadData(data)
                listPolicyJson.push(policies);
            }

        }, error: function (data) {
            console.log(data);
        }
    })
}

function emptyLocationCheckboxes() {
    $('#locations').empty();
}

jQuery.extend({
    getValues: function (id) {
        var result = null;
        var url = 'http://localhost:8080/location/get/' + id;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            async: false,
            success: function (data) {
                result = data.location;
            }, error: function (res) {
                console.log(res);
            }
        });
        return result;
    }
});

var locationName = "";

function getLocationName(id) {
    locationName = "";
    var url = 'http://localhost:8080/location/get/' + id;
    $.ajax({
        url: url,
        type: 'get',
        dataType: 'json',
        async: false,
        success: function (data) {
            locationName = data.location;
        }, error: function (res) {
            console.log(res);
        }
    });
}

function loadData(res) {
    var data = res.data;
    locationName = "";
    if (data != null) {
        for (var i = 0; i < data.length; i++) {
            var vehicleList = data[i].policyHasTblVehicleTypes;
            row = '<tr>';
            row += '<td>' + (i + (res.pageNumber * res.pageSize) + 1) + '</td>';
            row += '<td>' + msToTime(data[i].allowedParkingFrom) + ' - ' + msToTime(data[i].allowedParkingTo) + '</td>';
            if (vehicleList != null || vehicleList.length != 0) {
                row += '<td class="vehicle-tags">';
                for (let j = 0; j < vehicleList.length; j++) {
                    row += '<span class="label label-success">' + vehicleList[j].vehicleTypeId.en_name + '</span>';
                }
            } else {
                row += '<td> N/A </td>'
            }
            if (data[i].locationId === null) {
                row += '<td> N/A </td>'
            } else {
                getLocationName(data[i].locationId)
                row += '<td>' + locationName + '</td>';
            }
            // row += '<td> <a href="#" class="btn btn-primary btnAction" onclick="getExistedLocations(' + data[i].id + ')"><i class="fas fa-plus-square"></i></a>';
            row += '<td><a href="#" class="btn btn-primary btnAction" onclick="editPolicy(' + data[i].id + ')"><i class="lnr lnr-pencil"></i></a>';
            row += ' <a class="btn btn-danger btnAction-remove" onclick="deletePolicy(' + data[i].id + ')"><i class="lnr lnr-trash"></i></a></td>';
            row += '</tr>';
            $('#policies-table tbody').append(row);
        }
    } else {
        var row = '<td colspan="5" style="text-align: center"><strong> No data </strong></td>';
        $('#policies-table tbody').append(row);
    }
    var pageNumber = res.pageNumber;
    console.log("page: " + pageNumber);
    console.log("Total Page: " + res.totalPages);
    // var currentPage;
    var li = "";
    $('#pagination').append(createPageButton(0, 'First', false, false));
    $('#pagination').append(createPageButton((pageNumber - 1), '<', (pageNumber < 1), false));
    if (pageNumber > 2) {
        $('#pagination').append(createEtcButton());
    }
    for (var currentPage = 0; currentPage < res.totalPages; currentPage++) {
        if (currentPage > res.pageNumber - 3 && currentPage < res.pageNumber + 3) {
            $('#pagination').append(createPageButton(currentPage, (currentPage + 1), false, (currentPage === pageNumber)));
        }
    }
    if (res.totalPages - pageNumber > 3) {
        $('#pagination').append(createEtcButton());
    }
    $('#pagination').append(createPageButton((pageNumber + 1), '>', (pageNumber === res.totalPages - 1), false));
    $('#pagination').append(createPageButton((res.totalPages - 1), 'Last', false, false));
}

var existedLocations = [];
var policy;

// function getExistedLocations(policyId) {
//     policy = policyId;
//     existedLocations = [];
//     $.ajax({
//         type: "GET",
//         dataType: "json",
//         url: 'http://localhost:8080/location/locations?policyId=' + policyId,
//         success: function (data) {
//             if (data != null) {
//                 for (let i = 0; i < data.length; i++) {
//                     existedLocations.push(data[i]);
//                 }
//                 loadLocations(policyId)
//             }
//         }, error: function (data) {
//             console.log(data);
//         }
//     });
// }

// function searchLocation() {
//
//     var listSearchObject = [];
//     var url = "http://localhost:8080/location/filter?page=0";
//     var searchValue = $('#locationName').val();
//     var searchObject = createSearchObject("location", ":", searchValue);
//     listSearchObject.push(searchObject);
//     $.ajax({
//         type: "POST",
//         contentType: "application/json; charset=utf-8",
//         dataType: "json",
//         data: JSON.stringify(listSearchObject),
//         url: url,
//         success: function (data) {
//             emptyLocationCheckboxes();
//             var locations = data.data;
//             for (var i = 0; i < locations.length; i++) {
//                 item = ' <label class="control control--checkbox">\n' +
//                     '                                <input type="checkbox" value="' + locations[i].id + '" name="chk"/>' +
//                     '<label>' + locations[i].location + '</label>\n' +
//                     '                                <div class="control__indicator"></div>\n' +
//                     '                            </label>';
//                 $('#locations').append(item);
//             }
//             // loadLocations(policy);
//         }, error: function (data) {
//             console.log(data);
//         }
//     });
//     addPolicyToLocation(policy);
// }

// function loadLocations(policyId) {
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
//                     if (!containsObject(locations[i], existedLocations)) {
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
//     // $('#addLocationModal').modal();
//     addPolicyToLocation(policyId);
// }
// function loadLocations(policyId) {
//     $.ajax({
//         type: "GET",
//         dataType: "json",
//         url: 'http://localhost:8080/location/get-locations',
//         success: function (data) {
//             if (data != null) {
//                 var locations = data.data;
//                 emptyLocationCheckboxes();
//                 var i;
//                 let j = 0;
//                 let rowNum = 0;
//                 for (i = 0; i < locations.length; i++) {
//                     if (j % 3 === 0) {
//                         rowNum = j;
//                         if ($(`#locationRow${rowNum}`).length) {
//
//                         } else {
//                             $("#locations").append('<tr id="locationRow' + rowNum + '">\n' +
//
//                                 '                    </tr>');
//                         }
//                     }
//                     j = loadColumn(locations[i], $(`#locationRow${rowNum}`), j);
//                 }
//             }
//
//         }, error: function (data) {
//             console.log(data);
//         }
//     });
//     // $('#addLocationModal').modal();
//     addPolicyToLocation(policyId);
// }
// var locationContain = [];
// function loadColumn(value, element, j) {
//     var item = "";
//     if (!containsObject(value, existedLocations)) {
//         locationContain.push(value);
//         item = '<td> <label class="control control--checkbox">\n' +
//             '                                <input id="locationChkbox' + value.id + '" type="checkbox" value="' + value.id + '" name="chk"' +
//             ' onclick="setTick(' + value.id + ',\'' + value.location + '\')"/>' +
//             '<label>' + value.location + '</label>\n' +
//             '                                <div class="control__indicator"></div>\n' +
//             '                            </label></td>';
//         locationMap.set(value.location, item);
//         element.append(locationMap.get(value.location));
//         j++;
//     }
//     return j;
// }
// var locationArr = [];
//
// function addPolicyToLocation(policyId) {
//     $('#btn-save-locations').off().on('click', function () {
//         var temp = $('input[name=chk]:checked').map(function (i) {
//             var location = {
//                 id: this.value,
//                 location: $(this).next('label').text()
//             }
//             locationArr.push(location);
//             return this;
//         }).get();
//
//         var jsonObject = {
//             policyId: policyId,
//             locationArr: locationArr,
//             currentLocationId: existedLocations
//         }
//
//         $.ajax({
//             type: "POST",
//             contentType: "application/json; charset=utf-8",
//             data: JSON.stringify(jsonObject),
//             url: 'http://localhost:8080/location/add-policy',
//             success: function (data) {
//                 // $('#addLocationModal').modal('hide');
//                 location.reload(true);
//             }, error: function (data) {
//                 console.log(data);
//             }
//         });
//     });
// }
//
// const setTick = (id, location) => {
//     console.log(id);
//     if ($('#locationChkbox' + id).is(':checked')) {
//         item = '<td><label class="control control--checkbox">\n' +
//             '                                <input id="locationChkbox' + id + '" checked type="checkbox" value="' + id + '" name="chk"' +
//             ' onclick="setTick(' + id + ',\'' + location + '\')"/>' +
//             '<label>' + location + '</label>\n' +
//             '                                <div class="control__indicator"></div>\n' +
//             '                            </label></td>';
//     } else {
//         item = '<td> <label class="control control--checkbox">\n' +
//             '                                <input id="locationChkbox' + id + '" type="checkbox" value="' + id + '" name="chk"' +
//             ' onclick="setTick(' + id + ',\'' + location + '\')"/>' +
//             '<label>' + location + '</label>\n' +
//             '                                <div class="control__indicator"></div>\n' +
//             '                            </label></td>';
//     }
//     locationMap.set(location, item);
// }


function editPolicy(policyInstanceId) {
    let locationId = $('#locationId').val();
    let url = "http://localhost:8080/policy/edit?policyId=" + policyInstanceId;
    window.location.href = url;
}

function deletePolicy(policyInstanceId) {
    let locationId = $('#locationId').val();
    $.ajax({
        type: "POST",
        // url: 'http://localhost:8080/policy/delete-by-location-policy?locationId=' + locationId + '&policyId=' + policyId,
        url: 'http://localhost:8080/policy/delete-policy?policyId=' + policyInstanceId,
        success: function (data) {
            console.log("Delete Successfully");
            location.reload(true);
        }, error: function (data) {
            console.log(data);
        }
    })
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

function emptyLocationCheckboxes() {
    $('#locations').empty();
}

function emptyTable() {
    $('#policies-table td').remove();
}

function emptyPaginationLi() {
    $('#pagination').empty();
}

function containsObject(obj, list) {
    var i;
    for (i = 0; i < list.length; i++) {
        if (parseInt(list[i].id) === parseInt(obj.id)) {
            return true;
        }
    }
    return false;
}

function createSearchObject(key, operation, value) {
    var obj = {
        key: key,
        operation: operation,
        value: value
    };
    return obj;
}

function msToTime(ms) {
    ms += 7 * 60 * 60 * 1000;
    var seconds = parseInt(ms / 1000);
    var minutes = parseInt(seconds / 60, 10);
    seconds = seconds % 60;
    var hours = parseInt(minutes / 60, 10);
    minutes = minutes % 60;

    return hours % 24 + ':' + minutes;
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