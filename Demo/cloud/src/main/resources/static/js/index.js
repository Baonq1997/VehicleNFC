$(document).ready(function (e) {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: 'http://localhost:8080/gundam/get-gundams/',
        success: function (data) {
            console.log(data);
            loadProducts(data);
        }, error: function () {
            alert("Can't load data")
        }
    });
});
function loadProducts(data) {
    var card =""
    for (i = 0; i < data.length; i++) {
        card += '<div class="card">\n' +
            ' <div class="card-header">\n' +
            '<a href="#">\n' +
            '<img src="http://ymsgundam.com/image/cache/data/hinhsp/bandai/mg/Barbatos6th01-133x133.jpg" />\n' +
            '</a>\n' +
            '\n' +
            '</div>\n' +
            '<div class="card-content">\n' +
            '<span>' + data[i].name+ '</span><br/>\n' +
            '<span>' + data[i].price + '</span>\n' +
            '</div>\n' +
            '</div>';
    }
    console.log("Card: "+card);
    $('.products').append(card);
}