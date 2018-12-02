$(document).ready(function (e) {
    var phone = $('#main-content', window.parent.document).attr('phone');
    if (typeof(phone) === 'undefined') {
        $.ajax({
            type: "GET",
            dataType: "json",
            url: 'get-users-json',
            success: function (data) {
                console.log(data);
                loadData(data);
            }, error: function () {
                alert("Can't load data")
            }

        });
    } else {
        $('#searchValue').val(phone);
        searchUser(0);
    }
    $('#btn-vehicle').on('click', function (e) {
        showVehicleModal();
    })
});

var userId;

function deleteVehicle() {
    $('#delete-btn').off().on('click', function () {
        $.ajax({
            type: "POST",
            // dataType: "json",
            url: "http://localhost:8080/user/unbind-vehicle?userId=" + userId,
            success: function (data) {
                location.reload(true);
            }, error: function (data) {
                console.log(data);
            }
        });
    });

}

function showVehicleModal() {
    let vehicleNumber = $('#vehicleNumberHidden').val();
    $.ajax({
        type: "GET",
        url: "http://localhost:8080/vehicle/get-vehicle/" + vehicleNumber,
        success: function (data) {
            $('#deleteVehicleModal #vehicleNumber').text(data.vehicleNumber);
            $('#deleteVehicleModal #licensePlateId').text(data.licensePlateId);
            $('#deleteVehicleModal #brand').text(data.brand);
            if (data.expireDate === null) {
                $('#deleteVehicleModal #expireDate').text("N/A");
            } else {
                var date = new Date(data.expireDate);
                $('#deleteVehicleModal #expireDate').text(date.getDate() + '-' + (date.getMonth() + 1) + '-' + date.getFullYear());
            }

        }, error: function (data) {
            console.log(data);
        }
    });
    deleteVehicle();
    $('#deleteVehicleModal').modal();
}

function emptyTable() {
    $('#user-table tbody tr').remove();
}

function emptyPaginationLi() {
    $('#pagination').empty();
}

function loadData(res) {
    var content = res.data;
    var row = "";
    for (var i = 0; i < content.length; i++) {
        row = '<tr>';
        // row += '<td>' + content[i].id + '</td>';
        row += cellBuilder((i + (res.pageNumber * res.pageSize) + 1), "text-center");
        row += '<td class="text-right">' + content[i].phoneNumber + '</td>';
        // row += '<td>' + content[i].password + '</td>';
        row += '<td>' + content[i].firstName + ' ' + content[i].lastName + '</td>';
        row += '<td class="text-right">' + (content[i].money * 1000).toLocaleString() + " vnđ" + '</td>';
        var addVehicle;
        if (content[i].vehicle == null) {
            row += '<td class="text-center">N/A</td>';
            row += '<td class="text-center">N/A</td>';
            addVehicle = "<a href=\"#\" onclick=\"addVehicleToUser('" + content[i].phoneNumber + "')\" class=\"btn btn-success btnAction\"><i class=\"fas fa-plus-square\"></i></a>";
            // "<a href=\"#\" onclick=\"addVehicleToUser('" + content[i].decodedId + "')\" class=\"btn btn-success btnAction\"><i class=\"far fa-check-square\"></i></a>";
        } else {

            var vehicleNumber = (content[i].vehicle.vehicleNumber != null)
                ? content[i].vehicle.vehicleNumber : "N/A";
            row += '<td class="text-center">' + vehicleNumber + '</td>';
            var vehicleType = (content[i].vehicle.vehicleTypeId != null)
                ? content[i].vehicle.vehicleTypeId.name : "N/A";
            row += '<td class="text-center">' + vehicleType + '</td>';
        }
        // row += '<td>' + content[i].vehicleTypeId.name + '</td>';

        var edit = "<a href=\"#\" onclick=\"loadUserInfo('" + content[i].decodedId + "')\" class=\"btn btn-primary btnAction\"><i class=\"lnr lnr-pencil\"></i></a>";
        var deleteStr = "<a href=\"#\" onclick=\"openDeleteModal('" + content[i].decodedId + "')\" class=\"btn btn-danger btnAction-remove\"><i class=\"lnr lnr-trash\"></i></a>";
        if (addVehicle != null) {
            row += cellBuilder(addVehicle + deleteStr + edit);
        } else {
            row += cellBuilder(deleteStr + edit);
        }

        row += '</tr>';
        $('#user-table tbody').append(row);
    }
    for (var i = 0; i < res.pageSize - content.length; i++) {
        $('#user-table tbody').append('<tr class="blank-row"></tr>');
    }

    var pageNumber = res.pageNumber;
    console.log("page: " + pageNumber);
    console.log("Total Page: " + res.totalPages);
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

function createPageButton(pageNumber, label, isDisable, isActive) {
    var className = (isActive) ? 'nav-item active' : 'nav-item';
    className += (isDisable) ? ' disabled-href' : '';
    return '<li class="' + className + '">\n<a href="#" class="nav-link" onclick="searchUser(' + pageNumber + ')">' + label + '</a>\n' +
        '</li>';
}

function createEtcButton() {
    return '<li class="etc ">...</li>';
}

$(document).ready(function (e) {
    // Sort table headers
    $('th').click(function () {
        var table = $(this).parents('table').eq(0)
        var rows = table.find('tr:gt(0)').toArray().sort(comparer($(this).index()))
        this.asc = !this.asc
        if (!this.asc) {
            rows = rows.reverse()
        }
        for (var i = 0; i < rows.length; i++) {
            table.append(rows[i])
        }
    })

    function comparer(index) {
        return function (a, b) {
            var valA = getCellValue(a, index), valB = getCellValue(b, index)
            return $.isNumeric(valA) && $.isNumeric(valB) ? valA - valB : valA.toString().localeCompare(valB)
        }
    }

    function getCellValue(row, index) {
        return $(row).children('td').eq(index).text()
    }

    // end sort table headers
});

$(document).ready(function (e) {

    $('#searchBtn').on('click', function (e) {
        e.preventDefault();
        searchUser(0);
    });
});

function searchUser(pageNumber) {
    var url = "search-user";
    if (pageNumber != null) {
        url = url + "?page=" + pageNumber;
    }
    var listFilterObject = [];
    var vehicleType = $('#search-filter option:selected').val();
    var searchValue = $('#searchValue').val();

    console.log("Search By: " + vehicleType);
    console.log("SearchValue: " + searchValue);
    var filterObject = createSearchObject(vehicleType, ":", searchValue);
    listFilterObject.push(filterObject);
    $.ajax({
        type: 'POST',
        url: url,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify(listFilterObject),
        success: function (response) {
            emptyTable();
            emptyPaginationLi();
            loadData(response.data);
            console.log(response);
        }
    });
}

function createSearchObject(key, operation, value) {
    var obj = {
        key: key,
        operation: operation,
        value: value,
    };
    return obj;
}

function cellBuilder(text, className) {
    text = (text != null) ? text : "N/A";
    return "<td class='" + className + "'>" + text + "</td>";
}

function loadVehicleInfo(vehicleNumber) {
    $('#main-content-user-list').hide();
    $('#main-content-verify-form').show();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'vehicle/get-vehicle/' + vehicleNumber,
        success: function (data) {
            setUpFormData(data);
        }, error: function () {
            alert("Can't load data")
        }
    });
    //load vehicle Type
    $.ajax({
        type: "GET",
        dataType: "json",
        url: '/vehicle-type/get-all',
        success: function (data) {
            setUpVehicleType(data, 'verify-vehicle-list')
        }, error: function () {
            alert("Can't load data")
        }

    });
}


function setUpFormData(vehicle) {
    $('#vehicleNumberShow').val(vehicle.vehicleNumber);
    $('#verify-vehicleNumber').val(vehicle.vehicleNumber);
    $('#licenseIdShow').val(vehicle.licensePlateId);
    $('#licenseId').val(vehicle.licensePlateId);
}

function setUpVehicleType(list, holder) {
    $('#' + holder).empty();
    for (var i = 0; i < list.length; i++) {
        var option = "<option value='" + list[i].id + "'>" + list[i].name + "</option>";
        $('#' + holder).append(option);
    }
}


function setLongFromExpireDate() {
    var time = $('#datepicker').val().split("-");
    var date = new Date(time[1] + "-" + time[0] + "-" + time[2]);
    $('#expireDate').val(date.getTime());
}


$('#datepicker').datepicker({
    weekStart: 1,
    autoclose: true,
    format: "dd-mm-yyyy",
    todayHighlight: true,
});

$('#verify-vehicle-form').on('submit', function (e) {
    $.ajax({
        type: 'post',
        url: '/vehicle/verify-vehicle',
        data: $('#verify-vehicle-form').serialize(),
        success: function (data) {
            if (data) {
                location.reload();
            }
        }
    });
    e.preventDefault();
});

function closeForm() {
    $('#verify-vehicle-form').trigger("reset");
    $('#main-content-user-list').show();
    $('#main-content-verify-form').hide();
    $('#main-content-save-form').hide();
}

function openSaveForm() {

    $('#main-content-user-list').hide();
    $('#main-content-save-form').show();
}

$('#save-user-form').on('submit', function (e) {
    e.preventDefault();
    $.ajax({
        type: 'post',
        url: 'update-user',
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: buildUserJSON(),
        success: function (data) {
            location.reload();
        }, error: function (data) {
            console.log(data);
        }
    });
});

function buildUserJSON() {
    var string = {
        decodedId: $('#decodedId').val(),
        phoneNumber: $('#phoneNumber').val(),
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        money: $('#money').val(),
        password: $('#password').val(),
        smsNoti: $('#smsNoti').prop('checked'),
        activated: $('#activated').prop('checked')
        // vehicle: {
        //     "vehicleNumber": $('#vehicleNumber').val(),
        //     "licensePlateId": $('#licensePlateId').val()
        // }
    };
    return JSON.stringify(string);
}

function loadUserInfo(id) {
    userId = id;
    openSaveForm();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'get-user/' + id,
        success: function (data) {
            setUpUserInfo(data);
        }, error: function () {
            alert("Can't load data")
        }
    });
}

function setUpUserInfo(data) {
    $('#decodedId').val(data.decodedId);
    $('#phoneNumber').val(data.phoneNumber);
    $('#firstName').val(data.firstName);
    $('#lastName').val(data.lastName);
    $('#password').val(data.password);
    $('#money').val(data.money);
    $('#smsNoti').prop('checked', data.smsNoti);
    $('#activated').prop('checked', data.activated);
    $('#vehicleNumberHidden').val(data.vehicle.vehicleNumber)
}


function openDeleteModal(vehicleNumber) {
    $('#deleteModal').modal(focus);
    $('#delete-id').val(vehicleNumber);
}

function deleteUser(id) {
    $.post("delete-user",
        {
            id: id,
        },
        (function (data, status) {
            if (data) {
                location.reload();
            }
        }));
}

// Add Vehicle To User
// Vehicles to add to user.
var vehicleSearch = "";
var currentPhoneNumber = "";
function addVehicleToUser(phoneNumber) {
    currentPhoneNumber = "";
    $('#vehicle-list').show();
    $('#main-content-user-list').hide();
    searchVehicle(0);
    currentPhoneNumber = phoneNumber;
}

$(document).ready(function () {
    $('#search-vehicle-form').submit((e) => {
        e.preventDefault();
        vehicleSearch = $('#vehicleSearch').val();
        searchVehicle(0);
    });
});

function searchVehicle(pageNumber) {
    var url = "http://localhost:8080/vehicle/search-vehicle";
    if (pageNumber != null) {
        url = url + "?page=" + pageNumber;
    }
    var listFilterObject = [];
    var vehicleType = $('#vehicle-search-filter option:selected').val();

    var verifyObject = createSearchObject("user", ":", null);
    var filterObject = createSearchObject(vehicleType, ":", vehicleSearch);
    listFilterObject.push(filterObject);
    listFilterObject.push(verifyObject);
    $.ajax({
        type: 'POST',
        url: url,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify(listFilterObject),
        success: function (response) {
            emptyVehicleTable();
            emptyVehiclePaginationLi();
            loadVehicles(response.data);
            console.log(response);
        }, error: function (data) {
            console.log(data);
        }
    });
}

function emptyVehicleTable() {
    $('#vehicle-table tbody tr').remove();
}

function emptyVehiclePaginationLi() {
    $('#vehicle-pagination').empty();
}

function loadVehicles(res) {
    var content = "";
    content = res.data;
    var row = "";
    for (i = 0; i < content.length; i++) {
        row = '<tr>';
        var vehicleType = (content[i].vehicleTypeId != null) ? content[i].vehicleTypeId.name : "N/A";
        var ownerPhone = (content[i].owner != null) ? content[i].owner.phoneNumber : "N/A";
        row += cellBuilder((i + (res.pageNumber * res.pageSize) + 1), "text-center");
        row += cellBuilder(content[i].vehicleNumber, "text-right");
        row += cellBuilder(content[i].licensePlateId, "text-right");
        row += cellBuilder(vehicleType, "text-center");
        row += cellBuilder(content[i].brand, "");
        row += cellBuilder(content[i].size, "text-right");
        row += cellBuilder(convertDate(content[i].expireDate), "text-right");
        row += cellBuilder(ownerPhone, "text-right");
        var addToUser = "<a href=\"#\" onclick=\"addToUser('" + content[i].vehicleNumber + "')\" class=\"btn btn-success btnAction\"><i class=\"fas fa-plus-square\"></i></a>";
        row += cellBuilder(addToUser, "text-center");
        row += '</tr>';
        $('#vehicle-table tbody').append(row);
    }

    for (var i = 0; i < res.pageSize - content.length; i++) {
        $('#vehicle-table tbody').append('<tr class="blank-row"></tr>');
    }

    var pageNumber = res.pageNumber;
    console.log("page: " + pageNumber);
    console.log("Total Page: " + res.totalPages);
    $('#vehicle-pagination').append(createVehiclePageButton(0, 'First', false, false));
    $('#vehicle-pagination').append(createVehiclePageButton((pageNumber - 1), '<', (pageNumber < 1), false));
    if (pageNumber > 2) {
        $('#vehicle-pagination').append(createEtcButton());
    }
    for (var currentPage = 0; currentPage < res.totalPages; currentPage++) {
        if (currentPage > res.pageNumber - 3 && currentPage < res.pageNumber + 3) {
            $('#vehicle-pagination').append(createVehiclePageButton(currentPage, (currentPage + 1), false, (currentPage === pageNumber)));
        }
    }
    if (res.totalPages - pageNumber > 3) {
        $('#vehicle-pagination').append(createEtcButton());
    }
    $('#vehicle-pagination').append(createVehiclePageButton((pageNumber + 1), '>', (pageNumber === res.totalPages - 1), false));
    $('#vehicle-pagination').append(createVehiclePageButton((res.totalPages - 1), 'Last', false, false));
}

function addToUser(addedVehicleNumber) {
    console.log(addedVehicleNumber);
    $.ajax({
        url: "http://localhost:8080/user/add-vehicle",
        type: "POST",
        data: {
            vehicleNumber: addedVehicleNumber,
            phoneNumber: currentPhoneNumber
        },
        success: function (data) {
            console.log(data);
            if (data === false) {
                alert("Can't add vehicle to user");
            } else {
                location.reload(true);
            }
        }, error: function (data) {
            console.log(data);
        }
    })
}

function createVehiclePageButton(pageNumber, label, isDisable, isActive) {
    var className = (isActive) ? 'nav-item active' : 'nav-item';
    className += (isDisable) ? ' disabled-href' : '';
    return '<li class="' + className + '">\n<a href="#" class="nav-link" onclick="searchVehicle(' + pageNumber + ')">' + label + '</a>\n' +
        '</li>';
}

function convertDate(dateTypeLong) {
    if (dateTypeLong === null) {
        return "N/A";
    }
    var dateStr = new Date(dateTypeLong),
        dformat = [dateStr.getDate(), dateStr.getMonth() + 1,
            dateStr.getFullYear()].join('-');
    return dformat;
}
