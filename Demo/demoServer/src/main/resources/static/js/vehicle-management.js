$(document).ready(function (e) {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'get-vehicles',
        success: function (data) {
            console.log(data);
            loadData(data);
        }, error: function () {
            alert("Can't load data")
        }

    });
});

function emptyTable() {
    $('#user-table td').remove();
}


function emptyPaginationLi() {
    var a = document.getElementById("pagination");
    $('#pagination').empty();
}


function loadData(res) {
    var content = "";
    content = res.data;
    var row = "";
    for (i = 0; i < content.length; i++) {
        row = (content[i].verified) ? '<tr>' : '<tr class="not-verified">';
        var vehicleType = (content[i].vehicleTypeId != null) ? content[i].vehicleTypeId.name : "Empty";
        var ownerPhone = (content[i].owner != null) ? content[i].owner.phoneNumber : "Empty";
        row += cellBuilder((i + (res.pageNumber * res.pageSize) + 1), "text-center");
        row += cellBuilder(content[i].vehicleNumber, "text-right");
        row += cellBuilder(content[i].licensePlateId, "text-right");
        row += cellBuilder(vehicleType, "text-center");
        row += cellBuilder(content[i].brand, "");
        row += cellBuilder(content[i].size, "text-right");
        row += cellBuilder(convertDate(content[i].expireDate), "text-right");
        row += cellBuilder(ownerPhone, "text-right");

        var verify = (!content[i].verified) ? "<a href=\"#\" onclick=\"loadVehicleInfo('" + content[i].vehicleNumber + "','main-content-verify-form'," + setUpFormData + ",'vehicle-list')\" class=\"btn btn-success btnVerify\">Verify</a>" : "";
        var edit = "<a href=\"#\" onclick=\"loadVehicleInfo('" + content[i].vehicleNumber + "','main-content-save-form'," + setUpSaveFormData + ",'save-vehicle-list')\" class=\"btn btn-primary btnAction\"><i class=\"lnr lnr-pencil\"></i></a>";
        var disable = (content[i].owner != null) ? "disabled" : "onclick=\"openDeleteModal('" + content[i].vehicleNumber + "')\" ";
        var deleteStr = "<a " + disable +
            " href=\"#\" class=\"btn btn-danger btnAction-remove\"><i class=\"lnr lnr-trash\"></i></a>";
        row += cellBuilder(deleteStr + edit + verify);
        row += '</tr>';
        $('#user-table tbody').append(row);
    }

    var pageNumber = res.pageNumber;
    console.log("page: " + pageNumber);
    console.log("Total Page: " + res.totalPages);
    var currentPage;
    var li = "";
    for (currentPage = 0; currentPage < res.totalPages; currentPage++) {
        if (currentPage === pageNumber) {
            li = '<li class="nav-item active">\n' +
                '<a href="#" class="nav-link" onclick="searchUser(' + currentPage + ')">' + (currentPage + 1) + '</a>\n' +
                '</li>';
            $('#pagination').append(li);
        } else {

            li = '<li class="nav-item">\n' +
                '<a href="#" class="nav-link" onclick="searchUser(' + currentPage + ')">\n' +
                +(currentPage + 1) + '</a>\n' +
                '</li>';
            $('#pagination').append(li);
        }
    }
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
    var url = "search-vehicle";
    if (pageNumber != null) {
        url = url + "?page=" + pageNumber;
    }
    var vehicleType = $('#search-filter option:selected').val();
    var searchValue = $('#searchValue').val();
    console.log("Search By: " + vehicleType);
    console.log("SearchValue: " + searchValue);

    // var filterObject = createSearchObject(vehicleType, ":", searchValue);
    var filterObject = createSearchObject(vehicleType, ":", searchValue);
    $.ajax({
        type: 'POST',
        url: url,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify(filterObject),
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
        value: value
    };
    return obj;
}

function convertDate(dateTypeLong) {
    if (dateTypeLong === null) {
        return "Empty";
    }
    var dateStr = new Date(dateTypeLong),
        dformat = [dateStr.getDate(), dateStr.getMonth() + 1,
            dateStr.getFullYear()].join('-');

    return dformat;
}

function cellBuilder(text, className) {
    text = (text != null) ? text : "Empty";
    return "<td class='" + className + "'>" + text + "</td>";
}

function loadVehicleInfo(vehicleNumber, form, setUpFunction, vehicleTypeHolder) {
    $('#' + vehicleTypeHolder).empty();
    $('#main-content-vehicle-list').hide();
    $('#' + form).show();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'get-vehicle/' + vehicleNumber,
        success: function (data) {
            setUpFunction(data);
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
            setUpVehicleType(data, vehicleTypeHolder)
        }, error: function () {
            alert("Can't load data")
        }
    });
}

function setUpFormData(vehicle) {
    $('#vehicleNumberShow').val(vehicle.vehicleNumber);
    $('#vehicleNumber').val(vehicle.vehicleNumber);
    $('#licenseIdShow').val(vehicle.licensePlateId);
    $('#licenseId').val(vehicle.licensePlateId);
}

function setUpSaveFormData(vehicle) {
    $('#save-VehicleNumber').val(vehicle.vehicleNumber);
    $('#save-VehicleNumber').prop('disabled', true);
    $('#save-licenseId').val(vehicle.licensePlateId);
    $('#save-brand').val(vehicle.brand);
    $('#save-size').val(vehicle.size);
    if (vehicle.expireDate != null) {
        $('#save-expireDate').val(vehicle.expireDate);
        var date = new Date(vehicle.expireDate);
        $('#save-datepicker').val(date.getDate() + '-' + (date.getMonth() + 1) + '-' + date.getFullYear());
    }
}

function setUpVehicleType(list, holder) {
    for (var i = 0; i < list.length; i++) {
        var option = "<option value='" + list[i].id + "'>" + list[i].name + "</option>";
        $('#' + holder).append(option);
    }
}

function setLongFromExpireDate(holder, id) {
    var time = $('#' + holder).val().split("-");
    var date = new Date(time[1] + "-" + time[0] + "-" + time[2]);
    $('#' + id).val(date.getTime());
}


$('#datepicker').datepicker({
    weekStart: 1,
    autoclose: true,
    format: "dd-mm-yyyy",
    todayHighlight: true,
});

$('#save-datepicker').datepicker({
    weekStart: 1,
    autoclose: true,
    format: "dd-mm-yyyy",
    todayHighlight: true,
});

$('#verify-vehicle-form').on('submit', function (e) {

    $.ajax({
        type: 'post',
        url: 'verify-vehicle',
        data: $('#verify-vehicle-form').serialize(),
        success: function (data) {
            if (data) {
                location.reload();
            }
        }
    });
    e.preventDefault();
});

$('#save-vehicle-form').on('submit', function (e) {
    $('#save-VehicleNumber').prop('disabled', false);
    $.ajax({
        type: 'post',
        url: 'save-vehicle',
        data: $('#save-vehicle-form').serialize(),
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
    $('#save-vehicle-form').trigger("reset");
    $('#save-VehicleNumber').prop('disabled', false);
    $('#main-content-vehicle-list').show();
    $('#main-content-verify-form').hide();
    $('#main-content-save-form').hide();
}

function openSaveForm() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: '/vehicle-type/get-all',
        success: function (data) {
            setUpVehicleType(data, "save-vehicle-list")
        }, error: function () {
            alert("Can't load data")
        }
    });

    $('#main-content-vehicle-list').hide();
    $('#main-content-save-form').show();
}

function openDeleteModal(vehicleNumber) {
    $('#deleteModal').modal(focus);
    $('#delete-vehicleNumber').val(vehicleNumber);
}

function deleteVehicle(vehicleNumber) {
    $.post("delete-vehicle",
        {
            vehicleNumber: vehicleNumber,
        },
        (function (data, status) {
            if (data) {
                location.reload();
            }
        }));
}

