$('#dropdown-trigger').click(function () {
    if ($('#dropdown-menu').hasClass('is-hidden')) {
        $('#dropdown-menu').removeClass('is-hidden');
    } else {
        $('#dropdown-menu').addClass('is-hidden');
    }
});