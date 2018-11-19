$(document).ready(function () {
    // initOrders();
    sortHeaders();
    $('#datepicker').datepicker({
        weekStart: 1,
        autoclose: true,
        format: "dd-mm-yyyy",
        todayHighlight: true,
    });
    // $('#order-table').attr('display','none');
    // $('#order-table').hide();
    // var picker = $('#datepicker');

    // picker.on('changeDate', function(e) {
    //     console.log($('#datepicker').val());
    // });
    $('#searchBtn').on('click', function (e) {
        e.preventDefault();

        $('#search-date').val($('#datepicker').val());
        var searchValue = $('#searchValue').val();
        $('#search-value').val(searchValue);
        filterOrder(0);
    });

    $('#btn-deposit').on('click', function (e) {
        e.preventDefault();
        $('#deposit-modal').modal();
        $('#deposit-modal #order-id').val();
    })
    depositModal();
});

function initOrders() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/order/get-orders',
        success: function (data) {
            console.log(data);
            $('#order-table').attr('display', 'block');
            loadData(data);
        }, error: function () {
            alert("Can't load data")
        }
    });
}

function loadData(res) {
    var content = "";
    content = res.data;
    $('#order-table').show();
    $('#guide-msg').hide();
    if (content.length != 0) {
        var row = "";
        for (var i = 0; i < content.length; i++) {
            row = '<tr style="height: 59px">';
            row += cellBuilder((i + (res.pageNumber * res.pageSize) + 1), "text-center");
            row += '<td>' + convertDate(content[i].checkInDate) + '</td>';
            row += '<td>' + convertDate(content[i].checkOutDate) + '</td>';
            row += '<td>' + content[i].location.location + '</td>';
            if (content[i].orderStatusId.name === "Close") {
                row += '<td style=" color: #ff0000;">' + content[i].orderStatusId.name + '</td>';
            } else if (content[i].orderStatusId.name === "Open") {
                row += '<td style="color: #339933;">' + content[i].orderStatusId.name + '</td>';
            } else {
                row += '<td style="color: #323599;">' + content[i].orderStatusId.name + '</td>';
            }

            // row += '<td>' + content[i].userId.firstName+' '+ content[i].userId.lastName + '</td>';
            // row += '<td>' + content[i].userId.phoneNumber + '</td>';
            row += '<td>' + content[i].userId.vehicle.vehicleNumber + '</td>';
            var duration = "";
            if (content[i].duration === null) {
                duration = 0;
            } else {
                duration = content[i].duration;
            }
            row += '<td>' + msToTime(duration) + '</td>';
            var total = "";
            if (content[i].total === null) {
                total = 0;
            } else {
                total = content[i].total;
            }
            row += '<td>' + convertMoney(total) + '</td>';
            row += '<td class="text-center"><a href="#" onclick="viewPricingDetail(' + content[i].id + ')" class="btn btn-primary btnAction"><i class="fas fa-info"></i></a></td>'
            row += '</tr>';
            $('#order-table tbody').append(row);
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
    for (var i = 0; i < res.pageSize - content.length; i++) {
        if (i === 2) {
            $('#order-table tbody').append('<tr class="blank-row"><td colspan="9" id="no-data-row"><span>No Result found</span></td></tr>');
        } else {
            $('#order-table tbody').append('<tr class="blank-row"></tr>');
        }
        if (i === res.pageSize - 1) {
            $('#no-data-row').show();
        }
    }


}

function createPageButton(pageNumber, label, isDisable, isActive) {
    var className = (isActive) ? 'nav-item active' : 'nav-item';
    className += (isDisable) ? ' disabled-href' : '';
    return '<li class="' + className + '">\n<a href="#" class="nav-link" onclick="filterOrder(' + pageNumber + ')">' + label + '</a>\n' +
        '</li>';
}

function createEtcButton() {
    return '<li class="etc ">...</li>';
}

function filterOrder(pageNumber) {
    var url = "http://localhost:8080/order/filter-order";
    if (pageNumber != null) {
        url = url + "?page=" + pageNumber;
    }
    var searchType = $('#search-filter option:selected').val();
    var searchValue = $('#search-value').val();
    var checkInDate = convertDateToMs($('#search-date').val());
    console.log(checkInDate);
    var timeType = 'checkInDate';
    console.log("Search By: " + searchType);
    console.log("SearchValue: " + searchValue);
    var listFilterObject = [];
    if (searchValue !== "") {
        var searchValue = createSearchObject(searchType, ":", searchValue);
        listFilterObject.push(searchValue);
    }
    var searchTime = createSearchObject(timeType, ":", checkInDate);
    listFilterObject.push(searchTime);
    $.ajax({
        type: 'POST',
        url: url,
        dataType: "json",
        contentType: 'application/json',
        data: JSON.stringify(listFilterObject),
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

function getOrderById(id) {
    var obj = "";
    $.ajax({
        type: "GET",
        dataType: "json",
        sync: false,
        url: 'http://localhost:8080/order/get-order/' + id,
        success: function (res) {
            console.log("Order:" + res.id)
            obj = res;

        }, error: function (data) {
            console.log("Could not load Order")
            console.log(data);
        }
    });
    return obj;
}

function closeForm() {
    $('#order-detail').hide();
    $('.searchBox').show();
    $('#order-list').show();
}

function viewPricingDetail(orderId) {
    $('#order-detail').show();
    $('.searchBox').hide();
    $('#order-list').hide();
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
            if (order.orderStatusId.name === "Close") {
                $('#order-detail #btn-deposit').show();
            } else {
                $('#order-detail #btn-deposit').hide();
            }
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

function depositModal() {
    $('#btn-submit-deposit').on('click', function (e) {
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/refund/request',
            data: {
                orderId: $('#deposit-modal #order-id').val(),
                amount: $('#deposit-modal #refund-money').val(),
                description: $('#deposit-modal #refund-description').val(),
                username: $('#main-content', window.parent.document).attr('username')
            },
            success: function (data) {
                if (data) {
                    alert("Request refund success");
                    location.reload(true);
                } else {
                    alert("Request refund fail");
                }
                $('#deposit-modal').modal('hide');
            }, error: function (data) {
                console.log(data);
            }
        })
    });
}

function emptyTable() {
    $('#order-table tbody tr').remove();
}

function emptyPricingTable() {
    $('#orderPricings td').remove();
}

function emptyPaginationLi() {
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

function msToTime(ms) {
    var seconds = parseInt(ms / 1000);
    var minutes = parseInt(seconds / 60, 10);
    seconds = seconds % 60;
    var hours = parseInt(minutes / 60, 10);
    minutes = minutes % 60;

    return hours + ':' + minutes;
}

// function parseTimeToLong(clockPicker, type) {
//     var time = $('.' + clockPicker + ' #' + type).val();
//     var temp = time.split(":")
//     var hour = temp[0];
//     var minute = temp[1];
//     var ms = parseInt(hour * 3600000) + parseInt(minute * 60000);
//     $('#allowed' + type).val(ms);
// }
function convertDate(dateTypeLong) {
    if (dateTypeLong === null) {
        return "Empty";
    }
    var dateStr = new Date(dateTypeLong),
        dformat = [dateStr.getDate(),
                dateStr.getMonth() + 1].join('/') + ' ' +
            [dateStr.getHours(),
                dateStr.getMinutes(),
                dateStr.getSeconds()].join(':');
    return dformat;
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

function convertDateToMs(dateStr) {
    if (dateStr === "") {
        return "";
    }
    var time = dateStr.split("-")
    var date = new Date(time[1] + "-" + time[0] + "-" + time[2])

    var ms = date.getTime();
    console.log(ms);
    // alert(ms);
    // console.log("MS: "+ms);

    // console.log("redundant: "+(parseInt(23 * 3600000) + parseInt(59 * 60000) + parseInt(59)));
    // console.log(convertDate(154030));
    return ms;
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

function sortHeaders() {
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
}

function cellBuilder(text, className) {
    text = (text != null) ? text : "N/A";
    return "<td class='" + className + "'>" + text + "</td>";
}

function convertMoney(money) {
    return (money * 1000).toLocaleString() + " VNĐ";
}
