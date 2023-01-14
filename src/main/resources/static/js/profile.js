$(function () {
    $.ajax({
        type: 'GET',
        url: `/api/profile/${nickname}`
    }).done(function (data) {
        $('#nickname').text(data.nickname);
        $('#picture').attr('src', data.picture);
    });
});