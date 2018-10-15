$(document).ready(function (e) {
    initData();
    editProduct();
    submitDeleteUserForm();
    sumitCreateModal();
});
function initData() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/gundam/get-gundams/',
        success: function (data) {
            console.log(data);
            loadData(data);
        }, error: function () {
            alert("Can't load data")
        }
    });
}
function loadData(content) {
    // var content = "";
    // content = res.data;
    var row = "";
    for (i = 0; i < content.length; i++) {
        row = '<tr>';
        row += '<td>' + content[i].id + '</td>';
        row += '<td>' + content[i].name + '</td>';
        row += '<td>' + content[i].price + '</td>';
        row += '<td><a href="#" onclick="loadUserModal(' + content[i].id + ')" class="btn btn-primary edtBtn">Edit</a></td>';
        row += '<td><a href="#" onclick="deleteModal(' + content[i].id + ')" class="btn btn-danger delBtn">Delete</a></td>';
        row += '</tr>';
        $('.product-table tbody').append(row);
    }
}
function editProduct() {
    var productForm = $('#save-product-form');
    productForm.submit(function (e) {
       e.preventDefault();
       $.ajax({
           type: productForm.attr('method'),
           url: productForm.attr('action'),
           data: productForm.serialize(),
           success: function (data) {
               console.log("Save successfully.");
               $('.myForm #editModal').modal('hide');
               console.log(data);
               location.reload(true);
           }, error: function (data) {
               console.log("Could not save user");
               console.log(data);
           }
       });
    });
}
function loadUserModal(id) {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/gundam/get/' + id,
        success: function (data) {
            // var user = JSON.parse(data);
            console.log("user: " + data);
            $('.myForm #id').val(data.id);
            $('.myForm #name').val(data.name);
            $('.myForm #price').val(data.price);
        }, error: function () {
            console.log("Could not load data");
        }
    });
    $('.myForm #editModal').modal();
}

function loadCreateModal() {
    $('.product-form #createProductModal').modal();
}

function sumitCreateModal() {
    var productFrm = $('#create-product-form');
    productFrm.submit(function (e) {
        e.preventDefault();
        $.ajax({
            type: productFrm.attr('method'),
            url: productFrm.attr('action'),
            data: productFrm.serialize(),
            success: function (data) {
                console.log("Create Successfully");
                $('.product-form #createProductModal').modal('hide');
                location.reload(true);

            }, error: function () {
                console.log("Could not create this user");
            }
        });

    });
}
function deleteModal(id) {
    // var url = "delete-user/" + id;
    $('.delBtn').on('click', function (event) {
        event.preventDefault();
        $('#deleteModal #id').val(id);

        $('#deleteModal').modal();
    });
}
function submitDeleteUserForm() {
    var deleteFrm = $('#delete-form');
    deleteFrm.submit(function (e) {
        var id = $('#deleteModal #id').val();
        e.preventDefault();
        $.ajax({
            type: deleteFrm.attr('method'),
            url: deleteFrm.attr('action'),
            // url: 'http://localhost:8080/user/delete-user/'+id,
            data: deleteFrm.serialize(),
            success: function (data) {
                console.log(data);
                console.log("Delete user successfully!");
                $('#deleteModal').modal('hide');
                location.reload(true);
            }, error: function (data) {
                console.log(data);
                console.log("Could not delete user!");
            }
        });
    });
}