function switchContentFragment(url, title, button) {
    if (button != null) {
        deActiveAllMenuButton();
        button.className = "active pointer menu";
    }
    $('#content-title').text(title);
    $('#main-content').removeAttr("phone");
    $('#main-content').attr('src', url);
}

function deActiveAllMenuButton() {
    var buttons = document.getElementsByClassName("menu");
    for (var i = 0; i < buttons.length; i++) {
        buttons[i].className = "pointer menu";
    }
}

var show = true;

function shortenNavBar() {
    var items = document.getElementsByClassName("title");
    for (var i = 0; i < items.length; i++) {
        items[i].style.fontSize = (show) ? "0" : "16px";
        items[i].style.width = (show) ? "0" : "190px";
    }
    $("#content-title-holder").css("left", (show) ? "75px" : "280px");
    $("#main").width((show) ? "calc(100% - 70px)" : "calc(100% - 260px)");
    $("#shortenNavBarIcon").attr('class', (show) ? "lnr lnr-chevron-right" : "lnr lnr-chevron-left");
    show = !show;
}

var notiNumber = 0;

function setUpNoti(payload) {
    // alert(payload.phoneNumber);
    $('#noti-container').append(buildNotiBody(payload.phoneNumber));
    $('#bell-noti').text(++notiNumber);
}

// Initialize Firebase
var config = {
    apiKey: "AIzaSyDkGKXxs-SsX7cfOdX6XND2lb1LAQ3u8FI",
    authDomain: "userapplication-f0a2a.firebaseapp.com",
    databaseURL: "https://userapplication-f0a2a.firebaseio.com",
    projectId: "userapplication-f0a2a",
    storageBucket: "userapplication-f0a2a.appspot.com",
    messagingSenderId: "200004929613"
};
firebase.initializeApp(config);
const messaging = firebase.messaging();
messaging.requestPermission().then(
    function () {
        console.log("have permission")
        console.log(messaging.getToken());
    }
).catch(
    function (reason) {
        console.log(reason)
    }
)

messaging.onMessage(function (payload) {
    console.log('onMessage: ', payload);
    setUpNoti(payload.data);
})

function buildNotiBody(phone) {
    return "<li" + " id='" + "noti" + phone + "'" +
        " onclick='notiClick(\"" + phone + "\")'><a href=\"#\" class=\"notification-item\"><span class=\"dot bg-danger\"></span>" +
        "[VERIFY] User with phone number: " + phone +
        "</a></li>";
}

function notiClick(phoneNumber) {
    // this.parentNode.removeChild(this);
    $("#noti" + phoneNumber).remove();
    $('#bell-noti').text(--notiNumber);
    switchContentFragment('/user/get-user', 'User Management', null);
    $('#main-content').attr('phone', phoneNumber);
}