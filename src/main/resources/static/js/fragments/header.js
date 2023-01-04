$('#dropdown-trigger').click(function () {
    if ($('#dropdown-menu').hasClass('is-hidden')) {
        $('#dropdown-menu').removeClass('is-hidden');
    } else {
        $('#dropdown-menu').addClass('is-hidden');
    }
});

function logout() {
    $.ajax({
        type: 'POST',
        url: '/api/account/logout',
        success: function (response) {
            window.location.replace('/');
        }
    });
}