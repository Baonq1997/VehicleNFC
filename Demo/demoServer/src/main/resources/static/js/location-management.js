var searchValue = "";
$(document).ready(function (e) {
    // $.ajax({
    //     type: "GET",
    //     dataType: "json",
    //     url: 'http://localhost:8080/location/get-locations',
    //     success: function (data) {
    //         console.log(data);
    //         loadData(data);
    //     }, error: function (data) {
    //         console.log("Could not load data");
    //         console.log(data);
    //         // alert("Can't load data")
    //     }
    // });
    filterLocations(0);
    $('#searchBtn').off().on('click', function () {
        searchValue = $('#searchValue').val();
        filterLocations(0);
    });

});

function emptyTable() {
    $('#location-table tr').remove();
}

function emptyPaginationLi() {
    $('#pagination').empty();
}

function emptyLi() {
    $('#policy-table li').empty();
}

function loadData(res) {
    var content = "";
    content = res.data;
    var row = "";
    for (i = 0; i < content.length; i++) {
        var status = "";
        if (content[i].activated === true) {
            status = "Active";
        } else {
            status = "De-active"
        }
        row = '<tr>';
        row += '<td>' + content[i].id + '</td>';
        row += '<td>' + content[i].location + '</td>';
        row += '<td>' + content[i].description + '</td>';
        row += '<td>' + convertStatus(content[i].activated) + '</td>';
        row += '<td><a href="#" onclick="viewPolicy(' + content[i].id + ')" class="btn btn-primary btnAction"><i class="lnr lnr-magnifier"></i></a>';
        row += '<a href="#" onclick="deleteLocation(' + content[i].id + ')" class="btn btn-danger btnAction"><i class="lnr lnr-trash"></i></a>';
        row += '<a href="#" onclick="getLocationModal(' + content[i].id + ')" class="btn btn-primary btnAction"><i class="lnr lnr-pencil"></i></a>';
        row += '<a href="#" onclick="createPolicy(' + content[i].id + ')" class="btn btn-primary btnAction"><i class="fas fa-plus-square"></i></a></td>';
        row += '</tr>';
        $('#location-table tbody').append(row);
    }

    var pageNumber = res.pageNumber;
    console.log("page: " + pageNumber);
    console.log("Total Page: " + res.totalPages);
    var currentPage;
    var li = "";
    for (currentPage = 0; currentPage <= res.totalPages - 1; currentPage++) {
        if (currentPage === pageNumber) {
            li = '<li class="nav-item active">\n' +
                '<a href="#" class="nav-link" onclick="filterLocations(' + currentPage + ')">' + (currentPage+1) + '</a>\n' +
                '</li>';
            $('#pagination').append(li);
        } else {

            li = '<li class="nav-item">\n' +
                '<a href="#" class="nav-link" onclick="filterLocations(' + currentPage + ')">\n' +
                +(currentPage+1)+ '</a>\n' +
                '</li>';
            $('#pagination').append(li);
        }
    }
}

function filterLocations(pageNumber) {
    var listSearchObject = [];
    var url = "http://localhost:8080/location/filter";
    if (pageNumber != null) {
        url = url + "?page=" + pageNumber;
    }
    var searchObject = createSearchObject("location", ":",searchValue);
    listSearchObject.push(searchObject);
    $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(listSearchObject),
        url: url,
        success: function (data) {
            emptyTable();
            emptyPaginationLi();
            loadData(data);
        }, error: function (data) {
            console.log(data);
        }
    })
}

function changeStatus() {
    $('##btn-change-status').off().on('click', function () {
        var status = $('#updatePricingModal #status').val();

    });
}

function deleteLocation(locationId) {
    $('#deleteModal').modal();
    $('#btn-delete-location').on('click', function (e) {

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/location/delete?id="+locationId,
            success: function (data) {
                $('#deleteModal').modal('hide');
                location.reload(true);
            }, error: function (data) {
                console.log(data);
            }
        })
    })
}
function getLocationModal(locationId) {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "http://localhost:8080/location/get/" + locationId,
        success: function (data) {
            $('#updatePricingModal #location').val(data.location);
            $('#updatePricingModal #description').val(data.description);
            $('#updatePricingModal #status').val(data.activated);
            $('#updatePricingModal #btn-change-status').text(convertStatus(data.activated));
            $('#updatePricingModal').modal();
        }, error: function (data) {
            console.log(data);
        }
    })
    updateLocation(locationId);
}

function convertStatusToBoolean(status) {
    if (status === "Available") {
        return true;
    } else {
        return false;
    }
}

function updateLocation(locationId) {
    $('#btn-save-location').off().click(function (e) {
        var location = {
            id: locationId,
            location: $('#updatePricingModal #location').val(),
            description: $('#updatePricingModal #description').val(),
            isActivated: convertStatusToBoolean($('#updatePricingModal #status').val()),
            policyList: null
        }
        $.ajax({
            url: 'http://localhost:8080/location/save',
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify(location),
            success: function (data) {
                console.log("Update successfully");
            }, error: function (data) {
                console.log(data);
            }
        })
    });
}

function convertStatus(status) {
    console.log(status);
    if (status === true) {
        return "Available";
    } else {
        return "Unavailable";
    }
}

function createPolicy(locationId) {
    var url = "http://localhost:8080/location/create-policy?locationId=" + locationId;
    window.location.href = url;
}

function viewPolicy(id) {
    window.location.href = "http://localhost:8080/location/policies/" + id;
}

function loadPolicyTable(data) {

    console.log(data);
    var content = "";
    content = data.policyList;
    var locationId = data.id;
    for (i = 0; i < content.length; i++) {
        // alert(content[i].id);
        emptyTable();
        emptyLi();
        $.ajax({
            type: "GET",
            dataType: "json",
            // contentType: "application/json",
            // data: JSON.stringify(content),
            url: 'http://localhost:8080/policy-vehicleType/get-vehicleTypes/' + content[i].id,
            success: function (data) {
                console.log("Policies: " + data);

                loadPolicy(data, locationId);
            }, error: function () {
                console.log("Could not load policy")
            }
        });
    }
}

function loadPolicy(data, locationId) {
    var row = "";
    var pricings = "";
    for (i = 0; i < data.length; i++) {
        pricings = data[i].pricings;
        for (j = 0; j < pricings.length; j++) {
            row = '<tr>';
            // row += '<td><input type="text" class="input index-'+ i +'-'+ j +'" name="vehicleType" disabled onkeypress="this.style.width = ((this.value.length + 1) * 8) + \'px\';"  value="' + data[i].policyId.allowedParkingFrom + '"></td>';
            row += '<td>' + data[i].vehicleTypeId.name + '</td>';
            row += '<td>' + data[i].policyId.allowedParkingFrom + '</td>';
            row += '<td>' + data[i].policyId.allowedParkingTo + '</td>';
            row += '<td>' + pricings[j].fromHour + '</td>';
            row += '<td>' + pricings[j].pricePerHour + '</td>';
            row += '<td>' + pricings[j].lateFeePerHour + '</td>';
            row += '<td> <button class="editBtn" onclick="Edit(' + data[i].policyId + ', ' + data[i].vehicleTypeId.id + ', ' + locationId + ')">Edit</button>';
            // row += '<td> <button class="saveBtn" onclick="Save('+ pricings[j].id +', ' + i + ', '+ j +')">Save</button>';
            row += '</tr>';
            $('#policy-table tbody').append(row);
        }
    }
}

function Edit(policyId, vehicleTypeId, locationId) {

    // $('.index-'+policyIndex+'-'+pricingIndex).prop('disabled', false);
    $('.saveBtn').show();
    $('.editBtn').hide();
    var url = "http://localhost:8080/policy/edit?policyId=" + policyId + "&vehicleTypeId=" + vehicleTypeId + "&locationId=" + locationId;
    window.location.href = url;

}

function createSearchObject(key, operation, value) {
    var obj = {
        key: key,
        operation: operation,
        value: value
    };
    return obj;
}

function parseTimeToLong(clockPicker, type) {
    console.log(type);
    console.log("log: " + $('.clockpickerFrom #ParkingFrom').val());
    var time = $('.' + clockPicker + ' #' + type).val();
    console.log("Time: " + time);
    var temp = time.split(":")
    var hour = temp[0];
    console.log("hour: " + hour);
    var minute = temp[1];
    console.log("Minute: " + minute);
    console.log("hour ms: " + parseInt(hour * 3600000));
    console.log("minute ms: " + parseInt(minute * 60000));
    var ms = parseInt(hour * 3600000) + parseInt(minute * 60000);
    console.log(ms);
    $('#allowed' + type).val(ms);
}