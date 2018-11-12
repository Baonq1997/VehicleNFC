$(document).ready(function () {
    // initPolicies();
    loadVehicles();
    filterPolicies(0);
    $('#searchBtn').off().on('click', function (e) {
        searchValue = $('#searchValue').val();
       filterPolicies(0);
    });
});
var listPolicyJson = [];
var searchValue = "";
function filterPolicies(pageNumber) {

        var vehicleTypeArr = [];
        var locationId = $('#locationId').val();
        var url = "http://localhost:8080/policy/filter-policies";
        if (pageNumber != null) {
            url = url+"?page="+pageNumber;
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

        var listSearchParam = [];
        if (vehicleTypeArr!= null && vehicleTypeArr.length > 0) {
            var vehicleTypes = createSearchObject("vehicleTypes", ":", vehicleTypeArr);
            listSearchParam.push(vehicleTypes);
        }
        var locationObj = createSearchObject("locationId",":", searchValue);
        listSearchParam.push(locationObj);
        $.ajax({
            type:'POST',
            url: url,
            dataType:"json",
            contentType: 'application/json',
            data: JSON.stringify(listSearchParam),
            success:function(response){

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
    $('.control-group location').empty();
}

jQuery.extend({
    getValues: function(id) {
        var result = null;
        var url = 'http://localhost:8080/location/get/'+id;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            async: false,
            success: function(data) {
                result = data.location;
            }, error: function (res) {
                console.log(res);
            }
        });
        return result;
    }
});

function loadData(res) {
    if (res != null) {
        var data = res.data;
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
            } else {
                row += '<td> N/A </td>'
            }
            if (data[i].locationId === null) {
                row += '<td> N/A </td>'
            } else {
                row += '<td>'+ $.getValues(data[i].locationId) + '</td>';
            }
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
    var pageNumber = res.pageNumber;
    console.log("page: " + pageNumber);
    console.log("Total Page: " + res.totalPages);
    var currentPage;
    var li = "";
    for (currentPage = 0; currentPage <= res.totalPages - 1; currentPage++) {
        if (currentPage === pageNumber) {
            li = '<li class="nav-item active">\n' +
                '<a href="#" class="nav-link" onclick="filterPolicies(' + currentPage + ')">' + (currentPage+1) + '</a>\n' +
                '</li>';
            $('#pagination').append(li);
        } else {

            li = '<li class="nav-item">\n' +
                '<a href="#" class="nav-link" onclick="filterPolicies(' + currentPage + ')">\n' +
                +(currentPage+1)+ '</a>\n' +
                '</li>';
            $('#pagination').append(li);
        }
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
        if (parseInt(list[i].id) === obj.id) {
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

function msToTime (ms) {
    var seconds = parseInt(ms/1000);
    var minutes = parseInt(seconds/60, 10);
    seconds = seconds%60;
    var hours = parseInt(minutes/60, 10);
    minutes = minutes%60;

    return hours + ':' + minutes;
}
