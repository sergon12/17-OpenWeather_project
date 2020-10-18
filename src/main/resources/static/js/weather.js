function getWeather() {
    //location.replace("/gt");

    let url = "/gt";
    jQuery.ajax({
    //$.ajax({
        url: url,
        method: "GET",
        async: false,
        processData: false,
        contentType: "application/json",
        success: function(data) {
            location.reload();
        },
        error: function(xhr, status) {
            alert("error-" + status);
        },
        complete: function(xhr, status) {
        }
    });
}

function reloadTable() {
    location.replace("/t");
}