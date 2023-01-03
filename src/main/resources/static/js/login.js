function login() {
    const email = $('#email-input').val();
    const password = $('#password-input').val();

    $.ajax({
        type: 'POST',
        url: '/api/account/login',
        contentType: 'application/json',
        data: JSON.stringify({
            'email': email,
            'password': password
        })
    }).done(function () {
        alert('로그인을 성공했습니다.');
        window.location.replace('/');
    }).fail(function () {
        alert('로그인을 실패했습니다.');
    });
}