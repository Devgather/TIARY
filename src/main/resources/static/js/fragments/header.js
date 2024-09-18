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
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        }
    }).done(function () {
        window.location.replace('/');
    });
}