$(document).ready(function (e) {
    var phone = $('#main-content', window.parent.document).attr('phone');
    if (typeof(phone) === 'undefined') {
        searchRequest(0);
    } else {
        $('#searchValue').val(phone);
        searchRequest(0);
    }
    $('#searchBtn').on('click', function (e) {
        e.preventDefault();
        searchRequest(0);
    });
});

function emptyTable() {
    $('#user-table tbody tr').remove();
}

function emptyPaginationLi() {
    var a = document.getElementById("pagination");
    $('#pagination').empty();
}

function convertDate(dateTypeLong) {
    if (dateTypeLong === null) {
        return "Empty";
    }
    var dateStr = new Date(dateTypeLong),
        dformat = [dateStr.getDate(),
                dateStr.getMonth() + 1,
                dateStr.getFullYear()].join('-') + ' ' +
            [dateStr.getHours(),
                dateStr.getMinutes(),
                dateStr.getSeconds()].join(':');
    return dformat;
}

function loadData(res) {
    var content = "";
    content = res.data;
    var row = "";
    for (i = 0; i < content.length; i++) {
        row = '<tr>';
        row += cellBuilder((i + (res.pageNumber * res.pageSize) + 1), "text-center");
        row += cellBuilder(content[i].staff.username, "text-right");
        row += cellBuilder(content[i].description, "text-right");
        row += cellBuilder(convertMoney(content[i].amount), "text-right");
        row += cellBuilder(convertDate(content[i].createDate), "text-right");
        row += '<td><a href="#" onclick="approveRequest(' + content[i].id + ',true)" class="btn btn-primary btnAction"><i class="fas fa-check"></i></a>';
        row += '<a href="#" onclick="approveRequest(' + content[i].id + ',false)" class="btn btn-primary btnAction"><i class="fa fa-ban"></i></a>';
        row += '<a href="#" onclick="viewPricingDetail(' + content[i].orderId + ')" class="btn btn-primary btnAction"><i class="fas fa-info"></i></a></td>'
        row += '</tr>';
        $('#user-table tbody').append(row);
    }

    for (var i = 0; i < res.pageSize - content.length; i++) {
        if (i === 2) {
            $('#user-table tbody').append('<tr class="blank-row"><td colspan="9" id="no-data-row"><span>Currently no refund request available</span></td></tr>');
        } else {
            $('#user-table tbody').append('<tr class="blank-row"></tr>');
        }
        if (i === res.pageSize - 1) {
            $('#no-data-row').show();
        }
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
    return '<li class="' + className + '">\n<a href="#" class="nav-link" onclick="searchRequest(' + pageNumber + ')">' + label + '</a>\n' +
        '</li>';
}

function createEtcButton() {
    return '<li class="etc ">...</li>';
}

function approveRequest(id, isApprove) {
    $.ajax({
        type: 'POST',
        url: 'approve',
        data: {
            requestId: id,
            username: $('#main-content', window.parent.document).attr('username'),
            isApprove: isApprove
        },
        success: function (response) {
            var msg = (isApprove)?"Request approved":"Request rejected";
            alert(msg);
            location.reload(true);
        }, error: function (res) {
            console.log(res);
        }
    });
}


function viewPricingDetail(orderId) {
    $('#order-detail').show();
    $('#main-content-vehicle-list').hide();
    // emptyPaginationLi();
    var order = $.getValues("http://localhost:8080/order/get-order/" + orderId);
    console.log(order);
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/order-pricing/get/' + orderId,
        success: function (res) {
            // console.log(res);
            // $('.myForm #lastName').text(order.userId.firstName+' '+ order.userId.lastName);
            $('#order-detail #order-id').text(order.id);
            $('#order-detail #vehicleNumber').text(order.userId.vehicle.vehicleNumber);
            $('#order-detail #licenseId').text(order.userId.vehicle.licensePlateId);
            $('#order-detail #phoneNumber').text(order.userId.phoneNumber);
            $('#order-detail #location').text(order.location.location);
            $('#order-detail #allowedParkingFrom').text(convertTime(order.allowedParkingFrom));
            $('#order-detail #allowedParkingTo').text(convertTime(order.allowedParkingTo));
            $('#order-detail #checkInDate').text(convertDate(order.checkInDate));
            $('#order-detail #checkOutDate').text(convertDate(order.checkOutDate));
            let row = "";
            emptyPricingTable();
            console.log("Pricing SIze: " + res.length);
            var hourHasPrices = order.hourHasPrices;
            var passHour = 0;
            var minutes = hourHasPrices[hourHasPrices.length - 1].minutes;
            console.log("MInute: " + minutes);
            var checkInDate = order.checkInDate;
            var checkOutDate = order.checkOutDate;
            for (i = 0; i < hourHasPrices.length; i++) {
                // table as receipt
                var hourHasPrice = hourHasPrices[i];
                var milliseconds = convertToMilliseconds(hourHasPrice.hour - passHour, "hour");
                if (i === hourHasPrices.length - 1) {
                    milliseconds += convertToMilliseconds(minutes, "minute");
                }
                var toHour = "";
                if (!compare2Dates(checkInDate, checkOutDate + milliseconds)) {
                    toHour = convertDateAsTimeDate(checkInDate + milliseconds);
                } else {
                    toHour = msToTime(checkInDate + milliseconds);
                }
                console.log("checkIndate: " + checkInDate);
                console.log("toHour: " + toHour);
                row = '<tr>';
                row += '<td>' + convertTime(checkInDate) + ' To ' + toHour + '</td>';
                row += '<td>' + convertMoney(hourHasPrice.price) + '</td>';
                row += '<td>' + convertMoney(hourHasPrice.total) + '</td>';
                row += '</tr>';

                checkInDate += milliseconds;
                passHour = hourHasPrice.hour;

                $('#order-detail #orderPricings tbody').append(row);
            }

            var duration = "";
            if (order.duration === null) {
                duration = 0;
            } else {
                duration = order.duration;
            }

            $('#order-detail #duration').text(msToTime(duration));
            var total = "";
            if (order.total === null) {
                total = 0;
            } else {
                total = order.total;
            }
            var rowTotal = '<tr><td></td><td><label>Total: </label></td><td><label>' + convertMoney(total) + '</label></td></tr>';
            $('#order-detail #orderPricings tbody').append(rowTotal);
            // $('.myForm #vehicleTypeId').text(order.userId.vehicleTypeId.name);
            $('#deposit-modal #order-id').val(orderId);
            $('#deposit-modal #user-id').val(order.userId.id);
            $('#deposit-modal #phone').text(order.userId.phoneNumber);
            $('#deposit-modal #username').text(order.userId.firstName + " " + order.userId.lastName);
            $('#deposit-modal #checkInDate').text(convertDate(order.checkInDate));
            $('#deposit-modal #checkOutDate').text(convertDate(order.checkOutDate));
            $('#deposit-modal #duration').text(msToTime(duration));
            $('#deposit-modal #duration').text(total);
        }, error: function (res) {
            console.log(res);
            console.log("Could not load data");
        }
    });
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

function searchRequest(pageNumber) {
    var url = "search-request";
    if (pageNumber != null) {
        url = url + "?page=" + pageNumber;
    }
    var listFilterObject = [];
    var vehicleType = $('#request-filter option:selected').val();
    var searchValue = $('#searchValue').val();

    console.log("Search By: " + vehicleType);
    console.log("SearchValue: " + searchValue);
    var verifyObject = createSearchObject("refundStatus", ":", 'Open');
    var filterObject = createSearchObject(vehicleType, ":", searchValue);
    listFilterObject.push(verifyObject);
    listFilterObject.push(filterObject);
    // listFilterObject.push(verifyObject);
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
        value: value
    };
    return obj;
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

function cellBuilder(text, className) {
    text = (text != null) ? text : "N/A";
    return "<td class='" + className + "'>" + text + "</td>";
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

function closeForm() {
    $('#verify-vehicle-form').trigger("reset");
    $('#save-vehicle-form').trigger("reset");
    $('#save-VehicleNumber').prop('disabled', false);
    $('#main-content-vehicle-list').show();
    $('#main-content-verify-form').hide();
    $('#main-content-save-form').hide();
}


function closeForm() {
    $('#order-detail').hide();
    $('#main-content-vehicle-list').show();
}

jQuery.extend({
    getValues: function (url) {
        var result = null;
        $.ajax({
            url: url,
            type: 'get',
            dataType: 'json',
            async: false,
            success: function (data) {
                result = data;
            }, error: function (res) {
                console.log(res);
            }
        });
        return result;
    }
});


function convertMoney(money) {
    return (money * 1000).toLocaleString() + " VNĐ";
}

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

function emptyPricingTable() {
    $('#orderPricings td').remove();
}

function convertToMilliseconds(value, type) {
    if (type === "hour") {
        return value * 3600000;
    } else {
        // minute
        return value * 60000;
    }
}

function compare2Dates(date1, date2) {
    // let checkInDate = convertDate(date1);
    // let checkOutDate = convertDate(date2);
    var checkInDate = new Date(date1);
    var checkOutDate = new Date(date2);
    var isTheSameDate = (checkInDate.getDate() == checkOutDate.getDate()
        && checkInDate.getMonth() == checkOutDate.getMonth()
        && checkInDate.getFullYear() == checkOutDate.getFullYear());
    return isTheSameDate;
}
function convertDateAsTimeDate(dateTypeLong) {
    if (dateTypeLong === null) {
        return "Empty";
    }
    var dateStr = new Date(dateTypeLong),
        dformat = [dateStr.getDate(),
                dateStr.getMonth() + 1].join('-') + ' ' +
            [dateStr.getHours(),
                dateStr.getMinutes()].join(':');
    return dformat;
}

function msToTime(ms) {
    var seconds = parseInt(ms / 1000);
    var minutes = parseInt(seconds / 60, 10);
    seconds = seconds % 60;
    var hours = parseInt(minutes / 60, 10);
    minutes = minutes % 60;

    return hours + ':' + minutes;
}
