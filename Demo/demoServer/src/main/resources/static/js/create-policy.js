var vehicleTypeArr = [];
var vehicleExistedArr= [];
var locationArr = [];
var locationExistedArr= [];
var policyHasVehicleTypeId = [];
$(document).ready(function () {
    localStorage.clear();
    loadVehicleTypes();
    loadLocations();
    savePolicyVehicle();
    submitPricing();
    deletePolicy();
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
    $('#btn-back').on('click', function (e) {
        $('#pricing-panel').hide();
        $('#wrapper').show();
    });
});
function loadLocations() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/location/get-locations',
        success: function (res) {
            if (res != null) {
                var data = res.data;
                console.log("VehicleTypes: " + data);
                for (i = 0; i < data.length; i++) {
                    var chk = '<input type="checkbox" class="locations" name="locationChk" id="location-' + i + '" value="' + data[i].id + '"><label>' + data[i].location + '</label>';
                    var select = '<option id="location-'+ i +'" value="'+data[i].id + '"><label>' + data[i].location + '</label>';
                    $('#locationArr').append(select);
                    // $('#locationArr').append(chk);
                    // if (containsObject(data,locationArr)) {
                    //     if (!containsObject(data, locationExistedArr)) {
                    //         locationExistedArr.push(data);
                    //     }
                    // }
                }
            }

        }, error: function (data) {
            console.log("Could not load vehicle types")
        }
    });
}
function loadVehicleTypes() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/vehicle-type/get-all',
        success: function (data) {
            console.log("VehicleTypes: " + data);
            for (i = 0; i < data.length; i++) {
                var chk = '<input type="checkbox" class="vehicles" name="chk" id="vehicleType-' + i + '" value="' + data[i].id + '"><label class="form-control">' + data[i].name + '</label>';
                $('#vehicleTypeArr').append(chk);

            }
        }, error: function (data) {
            console.log("Could not load vehicle types")
        }
    });
}
function loadVehiclesCheckedBoxes() {
    // $('#vehicleTypeArr input').remove();
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/vehicle-type/get-all',
        success: function (data) {
            console.log("VehicleTypes: " + data);
            for (i = 0; i < data.length; i++) {
                let vehicleType = data[i];
                console.log("ARR: "+vehicleTypeArr.length);
                // var checkedVehicles =
                console.log("OBJ: "+vehicleType);
                let isFound = false;
                for (let j = 0; j < vehicleTypeArr.length; j++) {
                    if (parseInt(vehicleTypeArr[j].id) === vehicleType.id) {
                        isFound = true;
                        let existedVehicle = {
                            id: data[i].id,
                            name: data[i].name
                            // existed: true
                        }
                        if (!containsObject(existedVehicle,vehicleExistedArr)) {
                            vehicleExistedArr.push(existedVehicle);
                        }

                    }
                }
                let chk = "";
                if (isFound) {
                    chk =  '<input type="checkbox" name="chk" id="vehicleType-' + i + '" value="' + data[i].id + '" checked><label>' + data[i].name + '</label>';

                } else {
                    chk = '<input type="checkbox" existed="false" name="chk" id="vehicleType-' + i + '" value="' + data[i].id + '"><label>' + data[i].name + '</label>';
                }
                $('#vehicleTypeArr').append(chk);

            }
        }, error: function (data) {
            console.log(data);
            console.log("Could not load vehicle types")
        }
    });
}

function parseTimeToLong(clockPicker, type) {
    var time = $('.' + clockPicker + ' #' + type).val();
    var temp = time.split(":")
    var hour = temp[0];
    var minute = temp[1];
    var ms = parseInt(hour * 3600000) + parseInt(minute * 60000);
    // console.log(ms);
    $('#allowed' + type).val(ms);
}

var policy;
function savePolicyVehicle() {
    $('#save-policy-vehicle').on('click', function () {
        vehicleTypeArr = [];

        // var checkedLocaitons = $('input[name=locationChk]:checked').map(function (i) {
        //     var location = {
        //         id: this.value,
        //         location: $(this).next('label').text()
        //     }
        //     locationArr.push(location);
        //     return this;
        // }).get();
        var locationId = $('#locationArr :selected').val();
        var vehicleTypes = $('input[name=chk]:checked').map(function (i) {
            var vehicleType = {
                id: this.value,
                name: $(this).next('label').text()
            }
            vehicleTypeArr.push(vehicleType);
            return this;
        }).get();
        let vehicleArr = [];
        if (vehicleExistedArr.length === 0) {
            vehicleArr = vehicleTypeArr;
        } else {
            for (let i = 0; i <vehicleTypeArr.length; i++) {
                for (let j = 0; j < vehicleExistedArr.length; j++) {
                    var checkedVehicle = vehicleTypeArr[i];
                    var vehicleExisted = vehicleExistedArr[j];
                    if (!containsObject(vehicleExisted, vehicleTypeArr)) {
                        var temp = {
                            id: vehicleExisted.id,
                            name: vehicleExisted.name,
                            isDelete: "true"

                        }
                        if(!containsObject(temp, vehicleArr)) {
                            vehicleArr.push(temp);
                        }
                    } else {
                        var temp = {
                            id: checkedVehicle.id,
                            name: checkedVehicle.name,
                            isDelete: "false"
                        }
                        if(!containsObject(temp, vehicleArr)) {
                            vehicleArr.push(temp);
                        }
                    }
                }
            }
        }

        var policyJson = {
            id: policy,
            allowedParkingFrom: $('#allowedParkingFrom').val(),
            allowedParkingTo: $('#allowedParkingTo').val()
        }
        var json = {
            locationId: locationId,
            policy: policyJson,
            vehicleTypes: vehicleArr
        }
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(json),
            url: 'http://localhost:8080/policy/create',
            success: function (data) {
                console.log("Save successfully");
                console.log(data);
                $('#policyId').val(data.id);
                $('.pricing-container').show();
                $('.pricing-container').empty();
                $('#pricing-panel').show();
                $('#wrapper').hide();
                $('.button-wrapper').show();
                $('#pricing-vehicle tbody').empty();
                policy = data.id;
                createPricingTabs(data.id);
                $('#vehicleTypeArr').empty();
                loadVehiclesCheckedBoxes();
            }, error: function (data) {
                console.log("Could not save policy vehicle")
            }
        });
    });
}
function createPricingTabs(policyId) {
    let navTabs = "";
    let tabPanes = "";
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/policy-vehicle/policy-vehicles?policyId='+policyId,
        success: function (data) {
            let navTabs = '<ul class="nav nav-tabs">';
            let tabPanes = '<div class="tab-content">'
            for (i = 0; i < data.length; i++) {
                // navTabs += '<li class="nav-item">' +
                //     '<a class="nav-link active" data-toggle="tab" href="#vehicle-' + data[i].vehicleTypeId.id + '">' + data[i].vehicleTypeId.name + '</a>' +
                //             '</li>';
                if (i == 0){
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
            // tabs += navTabs;
            tabPanes += '</div>';
            // tabs += tabPanes;
            $('.pricing-container').append(tabPanes);
            var tables = "";
            for (let i = 0; i <data.length; i++) {
                tables += createTable(data[i].vehicleTypeId.id, data[i].id);
                policyHasVehicleTypeId.push(data[i].id);
                loadPricingTable(data[i].vehicleTypeId.id);
            }
            // tabs += tables;
        }, error: function (data) {
            console.log("Failed to get policy vehicle types");
        }
    });
}
function createTable(vehicleTypeId, policyHasVehicleTypeId) {
    var btnAddPricing = '<button class="btn btn-primary" type="button" value="Add Pricing" onclick="addPricing(' + policyHasVehicleTypeId + ', ' + vehicleTypeId + ')" id="btn-add-pricing">Add Pricing\n' +
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

function deletePolicy() {
    $('#delete-policy').off().click(function () {
        var policyId = $('#policyId').val();
        var policyJson = {
            id: policyId,
            allowedParkingFrom: $('#allowedParkingFrom').val(),
            allowedParkingTo: $('#allowedParkingTo').val()
        }
        var locationId = $('#locationArr :selected').val();
        var json = {
            locationId: parseInt(locationId),
            policy: policyJson,
            policyHasVehicleTypeId: policyHasVehicleTypeId,
            vehicleTypes: vehicleTypeArr
        }
        $.ajax({
            type: 'POST',
            // dataType: "json",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(json),
            url: 'http://localhost:8080/policy/delete',
            success: function (data) {
                console.log("Save successfully");
                console.log(data);
                location.href = "/policy/index";
            }, error: function (data) {
                console.log(data);
            }
        });
    });
}

function addPricing(policyHasVehicleTypeId, vehicleTypeId) {

    $('#savePricingModal').modal();

    $('#save-pricing #policyHasTblVehicleTypeId').val(policyHasVehicleTypeId);
    $('#save-pricing #vehicleTypeId').val(vehicleTypeId);
}


function submitPricing() {
    var frm = $('#save-pricing');
    frm.submit(function (e) {
        var vehicleTypeId  = $('#save-pricing #vehicleTypeId').val();
        var policyVehicle = $('#save-pricing #policyHasTblVehicleTypeId').val();
        console.log("SubmitPricing - VehicleTypeId: "+vehicleTypeId);
        var pricings = [];
        if (localStorage.getItem('pricingList-'+vehicleTypeId) != null){
            pricings = JSON.parse(localStorage.getItem('pricingList-'+vehicleTypeId));
        }
        console.log("VehicleTypeId: "+vehicleTypeId);
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: frm.attr('action')+'?policyVehicleId='+policyVehicle,
            data: frm.serialize(),
            success: function (data) {

                console.log("Add Successfully");
                console.log(data);
                $('#savePricingModal').modal('hide');
                pricings.push(data);
                console.log("pricingJson: "+JSON.stringify(pricings))
                localStorage.setItem('pricingList-'+vehicleTypeId, JSON.stringify(pricings));
                emptyTable(vehicleTypeId);
                loadPricingTable(vehicleTypeId);
            }, error: function (data) {
                console.log("Failed to save");
                console.log(data);
            }
        });
    });
}

function loadPricingTable(vehicleTypeId) {
    let pricings = JSON.parse(localStorage.getItem('pricingList-'+vehicleTypeId));
    let policyHasVehicleType =  $('#save-pricing #policyHasTblVehicleTypeId').val();
    if (pricings != null) {
        for (let i = 0; i < pricings.length; i++) {
            let row = '<tr>';
            row += '<td>' + pricings[i].fromHour + '</td>';
            row += '<td>' + convertMoney(pricings[i].pricePerHour) + '</td>';
            row += '<td>' + convertMoney(pricings[i].lateFeePerHour) + '</td>';
            // row += '<td><a href="#" onclick="loadPricingModal(' + policyHasVehicleType + ',' + pricings[i].id + ')" class="btn btn-primary saveBtn">Edit</a></td>'
            row += '<td><a href="#" onclick="savePricing(' + policyHasVehicleType + ',' + vehicleTypeId + ',' + pricings[i].id + ')" class="btn btn-primary btnAction"><i class="lnr lnr-pencil"></i></a></td>'
            row += '<td><a href="#" onclick="deleteModal(' + pricings[i].id + ',' + vehicleTypeId + ')" class="btn btn-danger btnAction-remove"><i class="lnr lnr-trash"></i></a></td>'
            row += '</tr>';
            $('#pricing-vehicle-'+vehicleTypeId +' tbody').append(row);
        }
    }
}

function deleteModal(pricingId, vehicleTypeId) {
    $('#deleteModal').modal();
    $('#btn-delete-pricing').off().click(function () {
        $.ajax({
            type: "POST",
            url: 'http://localhost:8080/pricing/delete-pricing/?id=' + pricingId,
            success: function (res) {
                console.log(res);
                $('#deleteModal').modal('hide');
                var pricings = JSON.parse(localStorage.getItem('pricingList-'+vehicleTypeId));
                var temp = [];
                for (let i = 0; i < pricings.length; i++) {
                    if (pricings[i].id === pricingId) {
                        var removeIndex = pricings.indexOf(pricings[i]);
                        pricings.splice(removeIndex,1);
                    }
                }
                localStorage.setItem('pricingList-'+vehicleTypeId, JSON.stringify(pricings));
                emptyTable(vehicleTypeId);
                loadPricingTable(vehicleTypeId);
            }, error: function (res) {
                console.log("Failed to delete");
            }
        });
    });
}

function savePricing(policyHasVehicleTypeId, vehicleTypeId, pricingId) {
    // $('#updatePricingModal').modal();
    loadPricingModal(policyHasVehicleTypeId, pricingId);
    $('#btn-save-pricing').off().click(function () {
        let pricingObject =  {
            id: pricingId,
            fromHour: $('#updatePricingModal #fromHour').val(),
            pricePerHour: $('#updatePricingModal #pricePerHour').val(),
            lateFeePerHour: $('#updatePricingModal #lateFeePerHour').val(),
            policyHasTblVehicleTypeId: policyHasVehicleTypeId
        }

        var pricingJson = JSON.parse(localStorage.getItem('pricingList-'+vehicleTypeId));
        $.ajax({
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            type: 'POST',
            url: 'http://localhost:8080/pricing/save-pricing-json?policyVehicleId='+policyHasVehicleTypeId,
            data: JSON.stringify(pricingObject),
            success: function (res) {
                console.log(res);
                console.log("Update Successfully");
                for (let i = 0; i < pricingJson.length; i++) {
                    if (pricingJson[i].id === pricingId) {
                        pricingJson[i] = res;
                    }
                }
                localStorage.setItem('pricingList-'+vehicleTypeId, JSON.stringify(pricingJson));
                $('#updatePricingModal').modal('hide');
                emptyTable(vehicleTypeId);
                loadPricingTable(vehicleTypeId);
            }, error: function (res) {
                console.log("Failed to save");
                console.log(res);
            }
        });
    });
}

function loadPricingModal(policyHasVehicleType, pricingId) {
    console.log("PricingId: "+pricingId);
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/pricing/get/'+pricingId,
        success: function (data) {
            $('#updatePricingModal #policyHasTblVehicleTypeId').val(policyHasVehicleType);
            $('#updatePricingModal #pricingId').val(pricingId);
            $('#updatePricingModal #fromHour').val(data.fromHour);
            $('#updatePricingModal #pricePerHour').val(data.pricePerHour);
            $('#updatePricingModal #lateFeePerHour').val(data.lateFeePerHour);
            $('#updatePricingModal').modal();
        }, error: function () {
            console.log("Could not load Pricing");
        }
    });
}

function emptyTable(vehicleTypeId) {
    $('#pricing-vehicle-'+vehicleTypeId+' tbody').empty();
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
function convertMoney(money) {
    return (money * 1000).toLocaleString() + " VNĐ";
}