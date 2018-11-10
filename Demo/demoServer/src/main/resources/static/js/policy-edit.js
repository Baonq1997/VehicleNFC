var locationHasVehicles = [];
var policyHasVehicleTypeId = [];
var vehicleTypeArr = [];
$(document).ready(function () {
    // var policyHasVehicleType = "";
    var url = location.href;
    console.log("URL: " + url);
    var index = url.lastIndexOf("?");
    var params = url.slice(index, url.length);
    console.log("params: " + params);
    var paramArr = params.split("&");

    var policyIdStr = paramArr[0];
    console.log("POlicyStr: " + policyIdStr);
    var policyInstanceId = policyIdStr.slice(policyIdStr.indexOf("=") + 1, policyIdStr.length);
    console.log("POlicyId: " + policyInstanceId);
    //
    // var vehicleTypeIdStr = paramArr[1];
    // var vehicleTypeId = vehicleTypeIdStr.slice(vehicleTypeIdStr.indexOf("=") + 1, vehicleTypeIdStr.indexOf("=") + 2);
    // console.log(vehicleTypeId);
    // // parseTimeToLong();
    // var locationIdStr = paramArr[1];
    // var locationId = locationIdStr.slice(locationIdStr.indexOf("=") + 1, locationIdStr.indexOf("=") + 2);

    loadPolicy(policyInstanceId);
    savePolicyHasVehicleType(policyInstanceId);
    deletePolicy(policyInstanceId);
    submitPricing();
    $('.clockpickerFrom').clockpicker({
        placement: 'bottom',
        align: 'left',
        donetext: 'Done',
        afterDone: function () {
            console.log("after done");
            parseTimeToLong("clockpickerFrom", "ParkingFrom");
        }
    });
    $('.clockpickerTo').clockpicker({
        placement: 'bottom',
        align: 'left',
        donetext: 'Done',
        afterDone: function () {
            console.log("after done");
            parseTimeToLong("clockpickerTo", "ParkingTo");
        }
    });
});

function loadPolicy(policyInstanceId) {
    $.ajax({
        type: "GET",
        dataType: "json",
        // url: 'http://localhost:8080/policy-vehicleType/get-by-policy?policyId=' + policyId,
        url: 'http://localhost:8080/policy-instance-vehicle/policy-instance-vehicles?policyInstanceId='+policyInstanceId,
        success: function (res) {
            console.log(res);
            loadData(res);
            getPolicy(policyInstanceId);
            loadVehicleTypes(res);
        }, error: function (res) {
            console.log(res);
        }
    });
}

function getPolicy(policyInstanceId) {
    $.ajax({
        type: "GET",
        dataType: "json",
        // url: "http://localhost:8080/policy/get/" + policyInstanceId,
        url: 'http://localhost:8080/policy-instance/get/'+policyInstanceId,
        success: function (data) {
            $('#ParkingFrom').val(convertDate(data.allowedParkingFrom));
            $('#ParkingTo').val(convertDate(data.allowedParkingTo));
            $('#policyId').val(data.id);
            $('#allowedParkingFrom').val(data.allowedParkingFrom);
            $('#allowedParkingTo').val(data.allowedParkingFrom);
        }, error: function (data) {
            console.log("Could not load Policy");
            console.log(data);
        }
    });
}

function loadData(data) {
    for (let i = 0; i < data.length; i++) {
        var navTabs = '<ul class="nav nav-tabs">';
        var tabPanes = '<div class="tab-content">'
        for (i = 0; i < data.length; i++) {
            if (i == 0) {
                navTabs += '<li class="nav-item">' +
                    '<a class="nav-link active" data-toggle="tab" href="#vehicle-' + data[i].vehicleTypeId.id + '">' + data[i].vehicleTypeId.name + '</a>' +
                    '</li>';
                tabPanes += ' <div class="tab-pane container active" id="vehicle-' + data[i].vehicleTypeId.id + '"></div>';
            } else {
                navTabs += '<li class="nav-item">' +
                    '<a class="nav-link" data-toggle="tab" href="#vehicle-' + data[i].vehicleTypeId.id + '">' + data[i].vehicleTypeId.name + '</a>' +
                    '</li>';
                tabPanes += ' <div class="tab-pane container" id="vehicle-' + data[i].vehicleTypeId.id + '"></div>';
            }
        }
        navTabs += '</ul>';
        $('.pricing-container').append(navTabs);
        tabPanes += '</div>';
        $('.pricing-container').append(tabPanes);
        // var tables = "";
        for (let j = 0; j < data.length; j++) {
            var vehicleId = data[j].vehicleTypeId.id;
            createTable(vehicleId, data[j].id);
            policyHasVehicleTypeId.push(data[j].id);
            // policyHasVehicleTypeId.push(data[i].id);
            var pricings = data[j].pricingList;
            if (pricings != null) {
                for (let i = 0; i < pricings.length; i++) {
                    var row = '<tr>';
                    row += '<td>' + pricings[i].fromHour + '</td>';
                    row += '<td>' + convertMoney(pricings[i].pricePerHour) + '</td>';
                    row += '<td>' + convertMoney(pricings[i].lateFeePerHour) + '</td>';
                    row += '<td><a href="#" onclick="savePricing(' + data[j].id + ' , ' + pricings[i].id + ')" class="btn btn-primary saveBtn btnBG"><i class="lnr lnr-pencil"></i></a></td>'
                    row += '<td><a href="#" onclick="deleteModal(' + pricings[i].id + ',' + data[j].id + ')" class="btn btn-danger delBtn"><i class="lnr lnr-trash"></i></a></td>'
                    row += '</tr>';
                    $('#pricing-vehicle-' + vehicleId + ' tbody').append(row);
                }
            }
        }
    }
}

// function deleteModal(pricingId, vehicleTypeId) {
//     $('#deleteModal').modal();
//
//     $('#btn-delete-pricing').off().click(function () {
//         $.ajax({
//             type: "POST",
//             // dataType: "json",
//             // contentType: "application/json; charset=utf-8",
//             url: 'http://localhost:8080/pricing/delete-pricing/' + pricingId,
//             success: function (res) {
//                 console.log(res);
//                 $('#deleteModal').modal('hide');
//                 var pricings = JSON.parse(localStorage.getItem('pricingList-'+vehicleTypeId));
//                 var temp = [];
//                 for (let i = 0; i < pricings.length; i++) {
//                     if (pricings[i].id === pricingId) {
//                         var removeIndex = pricings.indexOf(pricings[i]);
//                         pricings.splice(removeIndex,1);
//                     }
//                 }
//                 localStorage.setItem('pricingList-'+vehicleTypeId, JSON.stringify(pricings));
//                 emptyTable(vehicleTypeId);
//                 loadPricingTable(vehicleTypeId);
//                 // location.reload(true);
//             }, error: function (res) {
//                 console.log("Failed to delete");
//             }
//         });
//     });
// }
function submitPricing() {
    var frm = $('#save-pricing');
    frm.submit(function (e) {
        var vehicleTypeId = $('#save-pricing #vehicleTypeId').val();
        console.log("SubmitPricing - VehicleTypeId: " + vehicleTypeId);
        var policyInstanceVehicleId = $('#save-pricing #policyHasTblVehicleTypeId').val();
        console.log("VehicleTypeId: " + vehicleTypeId);
        e.preventDefault();
        $.ajax({
            type: frm.attr('method'),
            url: frm.attr('action')+'?policyInstanceVehicleId='+policyInstanceVehicleId,
            data: frm.serialize(),
            success: function (data) {
                console.log("Add Successfully");
                console.log(data);
                $('#savePricingModal').modal('hide');
                // loadPolicy(policyId, vehicleTypeId);
                emptyTable(vehicleTypeId);
                location.reload(true);
            }, error: function (data) {
                console.log("Failed to save");
                console.log(data);
            }
        });
    });
}

function createTable(vehicleTypeId, policyHasVehicleTypeId) {
    var btnAddPricing = '<button class="btn btn-primary btnBG" type="button" value="Add Pricing" onclick="addPricing(' + policyHasVehicleTypeId + ', ' + vehicleTypeId + ')" id="btn-add-pricing">Add Pricing\n' +
        '                                </button>';
    var table = ' <table class="table table-hover" id="pricing-vehicle-' + vehicleTypeId + '">\n' +
        '                                    <thead>\n' +
        '                                    <tr>\n' +
        '                                        <th>From Hour:</th>\n' +
        '                                        <th>Price Per Hour</th>\n' +
        '                                        <th>Late Fee Per Hour</th>\n' +
        '                                        <th colspan="2">Action</th>\n' +
        '                                    </tr>\n' +
        '                                    </thead>\n' +
        '                                    <tbody>\n' +
        '\n' +
        '                                    </tbody>\n' +
        '                                </table>';

    var tableData = btnAddPricing + table;
    $('#vehicle-' + vehicleTypeId).append(btnAddPricing);
    $('#vehicle-' + vehicleTypeId).append(table);
    return table;
}

function addPricing(policyHasVehicleTypeId, vehicleTypeId) {

    $('#savePricingModal').modal();

    $('#save-pricing #policyHasTblVehicleTypeId').val(policyHasVehicleTypeId);
    $('#save-pricing #vehicleTypeId').val(vehicleTypeId);

}

function savePricing(policyInstanceVehicleId, pricingId) {

    $('#updatePricingModal').modal();
    var updateFrm = $('#update-pricing');
    $('#pricingId').val(pricingId);
    $('.update-pricing-form #policyHasTblVehicleTypeId').val(policyHasVehicleTypeId);
    updateFrm.submit(function (e) {
        e.preventDefault();
        $.ajax({
            type: updateFrm.attr('method'),
            url: updateFrm.attr('action')+'?policyInstanceVehicleId='+policyInstanceVehicleId,
            data: updateFrm.serialize(),
            success: (res) => {
                location.reload(true);
            console.log(res);
        console.log("Update Successfully");
        $('#updatePricingModal').modal('hide');
    }, error: (res) => {
            console.log("Failed to save");
            console.log(res);
        }
    });
    });
}

// function addPricing() {
//     $('#btn-add-pricing').on('click', function (e) {
//         $('#savePricingModal').modal();
//         var frm = $('#save-pricing');
//         frm.submit(function (e) {
//             e.preventDefault();
//             $.ajax({
//                 type: frm.attr('method'),
//                 url: frm.attr('action'),
//                 data: frm.serialize(),
//                 success: function (data) {
//                     console.log("Add Successfully");
//                     console.log(data);
//                     $('#savePricingModal').modal('hide');
//                     location.reload(true);
//                 }, error: function (data) {
//                     console.log("Failed to save");
//                     console.log(data);
//                 }
//             });
//         });
//
//     });
// }

function deleteModal(pricingId, policyInstanceVehicleId) {
    $('#deleteModal').modal();
    var frm = $('#delete-form');
    frm.submit(function (e) {
        e.preventDefault();
        $.ajax({
            type: frm.attr('method'),
            url: frm.attr('action') + "?id=" + pricingId,
            success: function (res) {
                console.log(res);
                $('#deleteModal').modal('hide');
                location.reload(true);
            }, error: function (res) {
                console.log("Failed to delete");
            }
        });
    });
}

function savePolicyHasVehicleType(locationId) {
    $('#save-policy').on('click', function (e) {
        var json = createPolicyHasVehicleTypeJson(locationId);
        console.log("JSON: " + json);
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            url: 'http://localhost:8080/policy-instance/create',
            data: JSON.stringify(json),
            success: function (res) {
                console.log(res);
                console.log("Save successfully");
                location.reload(true);
            }, error: function (res) {
                console.log(res);
                console.log("Failed to save");
            }
        });
    });
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

function deletePolicy(locationId) {
    $('#delete-policy').on('click', function (e) {
        var json = createPolicyHasVehicleTypeJson(locationId);
        console.log("JSON: " + json);
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            // dataType: "json",
            url: 'http://localhost:8080/policy-instance/delete',
            data: JSON.stringify(json),
            success: function (res) {
                console.log(res);
                console.log("Save successfully");
                window.location.href = 'http://localhost:8080/user/admin';
            }, error: function (res) {
                console.log(res);
                console.log("Failed to save");
            }
        });
    });
}

function convertTime(dateTypeLong) {
    if (dateTypeLong === null || dateTypeLong === 0){
        return "Empty";
    }

    var dateStr = new Date(dateTypeLong),
        dformat =
            [dateStr.getHours(),
                dateStr.getMinutes()].join(':');
    return dformat;
}

var locationHasVehicles = [];

function loadVehicleTypes(data) {

    // $.ajax({
    //     type: "GET",
    //     dataType: "json",
    //     url: 'http://localhost:8080/policy-vehicleType/get-by-policy?policyId=' + policyId,
    //     success: function (data) {
    //         console.log("Number Of policy's vehicles: " + data.length);
    for (let i = 0; i < data.length; i++) {
        var vehicleType = data[i].vehicleTypeId;
        locationHasVehicles.push(vehicleType);
        // locationHasVehicles =  appendCheckedVehicleTypes(vehicleType);
    }
    console.log("ArrayLENGTH: " + locationHasVehicles.length);
    loadVehiclesCheckedBoxes(locationHasVehicles);

    // locationHasVehicles = data;
    // loadVehiclesCheckedBoxes(locationHasVehicles);
    // }, error: function (data) {
    //     console.log("Could not load vehicle types in location");
    //     console.log(data);
    // }
    // })
}

function loadVehiclesCheckedBoxes(locationHasVehiclesArr) {
    // $('#vehicleTypeArr input').remove();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/vehicle-type/get-all',
        success: function (data) {
            console.log("VehicleTypes: " + data);
            for (i = 0; i < data.length; i++) {
                let vehicleType = data[i];
                console.log("ARR: " + locationHasVehiclesArr.length);
                // var checkedVehicles =
                console.log("OBJ: " + vehicleType);
                let isFound = false;
                for (let j = 0; j < locationHasVehiclesArr.length; j++) {
                    if (locationHasVehiclesArr[j].id === vehicleType.id) {
                        isFound = true;
                    }
                }
                let chk = "";
                if (isFound) {
                    chk = '<input type="checkbox" name="chk" id="vehicleType-' + i + '" value="' + data[i].id + '" checked><label>' + data[i].name + '</label>';
                } else {
                    chk = '<input type="checkbox" name="chk" id="vehicleType-' + i + '" value="' + data[i].id + '"><label>' + data[i].name + '</label>';
                }
                $('#vehicleTypeArr').append(chk);

            }
        }, error: function (data) {
            console.log(data);
            console.log("Could not load vehicle types")
        }
    });
}

function createPolicyHasVehicleTypeJson(locationId) {
    var vehicleTypes = $('input[name=chk]:checked').map(function (i) {
        var vehicleType = {
            id: this.value,
            name: $(this).next('label').text()
        }
        vehicleTypeArr.push(vehicleType);
        return this;
    }).get();

    let vehicleArr = [];
    if (locationHasVehicles.length === 0) {
        vehicleArr = vehicleTypeArr;
    } else {
        for (let i = 0; i <vehicleTypeArr.length; i++) {
            for (let j = 0; j < locationHasVehicles.length; j++) {
                var checkedVehicle = vehicleTypeArr[i];
                var vehicleExisted = locationHasVehicles[j];
                if (!containsObject(vehicleExisted, vehicleTypeArr)) {
                    var temp = {
                        id: vehicleExisted.id,
                        name: vehicleExisted.name,
                        // name: "true",
                        isDelete: "true"

                    }
                    if(!containsObject(temp, vehicleArr)) {
                        vehicleArr.push(temp);
                        // break;
                    }


                } else {
                    var temp = {
                        id: checkedVehicle.id,
                        name: checkedVehicle.name,
                        // name: "false",
                        isDelete: "false"
                    }
                    if(!containsObject(temp, vehicleArr)) {
                        vehicleArr.push(temp);
                        // break;
                    }
                    // vehicleArr.push(temp);
                    // break;
                }
            }
        }
    }

    var policyId = $('#policyId').val();
    var policyJson = {
        id: policyId,
        allowedParkingFrom: $('#allowedParkingFrom').val(),
        allowedParkingTo: $('#allowedParkingTo').val()
    }
    var json = {
        locationId: locationId,
        policyInstance: policyJson,
        policyHasVehicleTypeId: policyHasVehicleTypeId,
        vehicleTypes: vehicleArr
    }

    return json;
}

function emptyTable(vehicleTypeId) {
    $('#pricing-vehicle-' + vehicleTypeId + ' tbody').empty();
}

function parseTimeToLong(clockPicker, type) {
    // console.log(type);
    // console.log("log: " + $('.clockpickerFrom #ParkingFrom').val());
    var time = $('.' + clockPicker + ' #' + type).val();
    // console.log("Time: " + time);
    var temp = time.split(":")
    var hour = temp[0];
    console.log("hour: " + hour);
    var minute = temp[1];
    console.log("Minute: " + minute);
    // console.log("hour ms: " + parseInt(hour * 3600000));
    // console.log("minute ms: " + parseInt(minute * 60000));
    var ms = (parseInt(hour * 3600000) + parseInt(minute * 60000));
    // var seconds = (parseInt(hour * 3600) + parseInt(minute * 60));
    var seconds = ms / 1000;
    alert(convertDate(ms));
    alert(ms);
    console.log(ms);
    $('#allowed' + type).val(ms);
}

function convertMoney(money) {
    return (money * 1000).toLocaleString() + " VNĐ";
}
function convertDate(dateTypeLong) {
    if (dateTypeLong === undefined) {
        return "N/A";
    }
    var defaultYear = 1970;
    var defaultMonth = 1;
    var defaultDay = 1;
    var milliseconds = parseInt((dateTypeLong % 1000) / 100),
        seconds = parseInt((dateTypeLong / 1000) % 60),
        minutes = parseInt((dateTypeLong / 60000) % 60),
        hours = parseInt((dateTypeLong / (1000 * 60 * 60)));

    return hours+":"+minutes;
}