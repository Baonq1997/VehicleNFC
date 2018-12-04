
$(document).ready(function () {
   searchUser(0);
   $('#searchBtn').off().on('click', function () {
       searchValue = $('#searchValue').val();
      searchUser(0);
   });

   $('#search-user-form').on('submit', function (e) {
       e.preventDefault();
       searchValue = $('#searchValue').val();
       searchUser(0);
   })
});
var searchValue ="";

function searchUser(pageNumber) {
    var url = "search-user";
    if (pageNumber != null) {
        url = url + "?page=" + pageNumber;
    }
    var listFilterObject = [];
    var vehicleType = $('#search-filter option:selected').val();

    console.log("Search By: " + vehicleType);
    console.log("SearchValue: " + searchValue);
    var deActiveUser = createSearchObject("isActivated",":",false);
    var filterObject = createSearchObject(vehicleType, ":", searchValue);
    listFilterObject.push(filterObject);
    listFilterObject.push(deActiveUser);
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

function loadData(res) {
    var content = res.data;
    var row = "";
    if (content.length != 0) {
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
                // "<a href=\"#\" onclick=\"addVehicleToUser('" + content[i].decodedId + "')\" class=\"btn btn-success btnAction\"><i class=\"far fa-check-square\"></i></a>";
            } else {

                var vehicleNumber = (content[i].vehicle.vehicleNumber != null)
                    ? content[i].vehicle.vehicleNumber : "N/A";
                row += '<td class="text-center">' + vehicleNumber + '</td>';
                var vehicleType = (content[i].vehicle.vehicleTypeId != null)
                    ? content[i].vehicle.vehicleTypeId.name : "N/A";
                row += '<td class="text-center">' + vehicleType + '</td>';
            }

            var active = "<a href=\"#\" onclick=\"activeUser('" + content[i].phoneNumber + "')\" class=\"btn btn-primary btnAction\"><i class=\"lnr lnr-pencil\"></i></a>";
            row += cellBuilder(active);
            row += '</tr>';
            $('#user-table tbody').append(row);
        }

        var pageNumber = res.pageNumber;
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
    } else {
        $('#user-table tbody').append('<tr class="blank-row"><td colspan="7" style="text-align: center;">No Data</td></tr>');
    }

}

function activeUser(phoneNumber) {
    $.ajax({
        type:"POST",
        url: "active/"+phoneNumber,
        success: function (data) {
            location.reload(true);
        }, error: function (data) {
            alert("Please try again");
        }
    })
}

function emptyTable() {
    $('#user-table tbody tr').remove();
}

function emptyPaginationLi() {
    $('#pagination').empty();
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

function createPageButton(pageNumber, label, isDisable, isActive) {
    var className = (isActive) ? 'nav-item active' : 'nav-item';
    className += (isDisable) ? ' disabled-href' : '';
    return '<li class="' + className + '">\n<a href="#" class="nav-link" onclick="searchUser(' + pageNumber + ')">' + label + '</a>\n' +
        '</li>';
}

function createEtcButton() {
    return '<li class="etc ">...</li>';
}
