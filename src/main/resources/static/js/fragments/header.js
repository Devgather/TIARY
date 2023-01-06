$('#dropdown-trigger').click(function () {
    if ($('#dropdown-menu').hasClass('is-hidden')) {
        $('#dropdown-menu').removeClass('is-hidden');
    } else {
        $('#dropdown-menu').addClass('is-hidden');
    }
});

function logout() {
    $.ajax({
        type: 'DELETE',
        url: '/api/account/logout',
        success: function () {
            window.location.replace('/');
        }
    });
}