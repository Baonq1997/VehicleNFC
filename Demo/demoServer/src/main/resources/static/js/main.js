function switchContentFragment(url, button) {
    if (button != null) {
        deActiveAllMenuButton();
        if (button.attr('class').includes("holder")) {
            button.attr('class', "active pointer menu holder-active");
        } else {
            button.attr('class', "active pointer menu");
        }

    }
    $('#main-content').removeAttr("phone");
    $('#main-content').attr('src', url);
}

function deActiveAllMenuButton() {
    var buttons = document.getElementsByClassName("menu");
    for (var i = 0; i < buttons.length; i++) {
        if (buttons[i].className.includes("holder")) {
            buttons[i].className = "pointer menu holder";
        } else {
            buttons[i].className = "pointer menu";
        }
    }
}

var show = true;

function shortenNavBar() {
    var items = document.getElementsByClassName("title");
    for (var i = 0; i < items.length; i++) {
        items[i].style.fontSize = (show) ? "0" : "21px";
        items[i].style.width = (show) ? "0" : "190px";
    }
    $("#footer").css("font-size", (show) ? "0" : "18px");
    $("#content-title-holder").css("left", (show) ? "50px" : "240px");
    $("#main").width((show) ? "calc(100% - 70px)" : "calc(100% - 260px)");
    $("#shortenNavBarIcon").attr('class', (show) ? "lnr lnr-chevron-right" : "lnr lnr-chevron-left");
    if (!show) {
        $(".menu-icon").show();
    } else {
        $(".menu-icon").hide();
    }
    show = !show;
}



var lastClassName;

function openHideList(holder) {
    var className = holder.attr('id');
    if (lastClassName === className) {
        if (holder.attr('isOpen') === 'false') {
            $('.' + className).attr('class', 'pointer sub-menu-show ' + className);
            $('#' + className + "Icon").attr('class', 'lnr lnr-chevron-up float-right menu-icon');
            holder.attr('isOpen', 'true');
        } else {
            $('.' + className).attr('class', 'pointer sub-menu ' + className);
            $('#' + className + "Icon").attr('class', 'lnr lnr-chevron-down float-right menu-icon');
            holder.attr('isOpen', 'false');
        }
    } else {
        $('.sub-menu-show').attr('class', 'pointer sub-menu ' + lastClassName);
        $('#' + lastClassName + "Icon").attr('class', 'lnr lnr-chevron-down float-right menu-icon');
        $('#' + lastClassName).attr('isOpen', 'false');
        $('.' + className).attr('class', 'pointer sub-menu-show ' + className);
        $('#' + className + "Icon").attr('class', 'lnr lnr-chevron-up float-right menu-icon');
        holder.attr('isOpen', 'true');
    }
    lastClassName = className;
}

