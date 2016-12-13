$(function() {
    if ("WebSocket" in window) {
        var socket = new WebSocket("ws://" + document.location.host + getContectPath() + "notews/all");
        socket.onopen = function() {
            console.log("connected");
        }

        socket.onclose = function() {
            console.log("disconnected");
        }

        socket.onmessage = function(evt) {
            var data = JSON.parse(evt.data);
            data = JSON.parse(data.message);
            $("input[name='catIds[]']").each( function () {
                if ($(this).is(":checked") && $(this).val() == data.category_name) {
                    var html = "";
                    html += "<tr>";
                    html += "<td>"+data.title+"</td>";
                    html += "<td>"+data.posted_date+"</td>";
                    html += "<td>"+data.category_name+"</td>";
                    html += "<td>"+data.content+"</td>";
                    html += "</tr>";
                    $("#table-notes > tbody > tr:first").before(html);
                }
            });
        }
    }
});
function getContectPath() {
    var path = document.location.pathname;
    var contextPath = path;
    if (path.indexOf("face") > 0) {
        contextPath = path.substring(0, path.indexOf("face"));
    }
    return contextPath;
}