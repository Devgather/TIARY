let emailVerificationState = false;
let nicknameDuplicationCheckState = false;

let verificationUuid = null;
let profileUuid = null;

$('#nickname-input').change(function () {
    nicknameDuplicationCheckState = false;
});

function disableEmailInput() {
    $('#email-input').prop('disabled', true);
}

function hideEmailVerificationButton() {
    if (!$('#email-verification-button').hasClass('is-hidden')) {
        $('#email-verification-button').addClass('is-hidden');
    }
}

function showEmailVerificationLabel() {
    $('#email-verification-label').removeClass('is-hidden');
}

function hideEmailVerificationLabel() {
    if (!$('#email-verification-label').hasClass('is-hidden')) {
        $('#email-verification-label').addClass('is-hidden');
    }
}

function showEmailVerificationField() {
    $('#email-verification-field').removeClass('is-hidden');
}

function hideEmailVerificationField() {
    if (!$('#email-verification-field').hasClass('is-hidden')) {
        $('#email-verification-field').addClass('is-hidden');
    }
}

function checkEmailExistence() {
    const email = $('#email-input').val();

    let result = false;

    $.ajax({
        type: 'HEAD',
        url: `/api/account/email/${email}`,
        async: false
    }).done(function () {
        result = false;
    }).fail(function (jqXHR) {
        if (jqXHR.status == 404) {
            result = true;
        } else {
            result = false;
        }
    });

    return result;
}

function sendVerificationMail() {
    const email = $('#email-input').val();

    let result = false;

    $.ajax({
        type: 'POST',
        url: `/api/account/verification/${email}`,
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        },
        async: false
    }).done(function () {
        result = true;
    }).fail(function () {
        result = false;
    });

    return result;
}

function verifyEmail() {
    const emailExistenceCheckResult = checkEmailExistence();

    if (!emailExistenceCheckResult) {
        alert('사용할 수 없는 이메일입니다.');

        return;
    }

    const verificationMailSendResult = sendVerificationMail();

    if (!verificationMailSendResult) {
        alert('인증 메일 전송에 실패했습니다.');

        return;
    }

    alert('인증 메일을 전송했습니다.');

    showEmailVerificationLabel();
    showEmailVerificationField();
}

function verifyCode() {
    const email = $('#email-input').val();
    const code = $('#code-input').val();

    $.ajax({
        type: 'PATCH',
        url: '/api/account/verification',
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        },
        contentType: 'application/json',
        data: JSON.stringify({
            'email': email,
            'code': code
        })
    }).done(function (data) {
        alert('이메일 인증이 완료되었습니다.');

        emailVerificationState = true;

        verificationUuid = data['uuid'];

        disableEmailInput();
        hideEmailVerificationButton();
        hideEmailVerificationLabel();
        hideEmailVerificationField();
    }).fail(function () {
        alert('잘못된 인증 코드입니다.');
    });
}

function checkNicknameDuplication() {
    const nickname = $('#nickname-input').val();

    $.ajax({
        type: 'HEAD',
        url: `/api/profile/nickname/${nickname}`
    }).done(function () {
        alert('사용할 수 없는 닉네임입니다.');
    }).fail(function (jqXHR) {
        if (jqXHR.status == 404) {
            alert('사용할 수 있는 닉네임입니다.');

            nicknameDuplicationCheckState = true;
        } else {
            alert('사용할 수 없는 닉네임입니다.');
        }
    });
}

function createProfile() {
    const nickname = $('#nickname-input').val();

    let result = false;

    $.ajax({
        type: 'POST',
        url: '/api/profile',
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        },
        contentType: 'application/json',
        data: JSON.stringify({
            'nickname': nickname
        }),
        async: false
    }).done(function (data) {
        profileUuid = data['uuid'];

        result = true;
    }).fail(function () {
        result = false;
    });

    return result;
}

function createAccount() {
    const email = $('#email-input').val();
    const password = $('#password-input').val();

    let result = false;

    $.ajax({
        type: 'POST',
        url: '/api/account',
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        },
        contentType: 'application/json',
        data: JSON.stringify({
            'verificationUuid': verificationUuid,
            'profileUuid': profileUuid,
            'email': email,
            'password': password
        }),
        async: false
    }).done(function () {
        result = true;
    }).fail(function () {
        result = false;
    });

    return result;
}

function register() {
    const email = $('#email-input').val();
    const password = $('#password-input').val();
    const confirmPassword = $('#confirm-password-input').val();
    const nickname = $('#nickname-input').val();

    if (!email) {
        alert('이메일을 입력해 주세요.');

        return;
    }

    if (!emailVerificationState) {
        alert('이메일 인증이 필요합니다.');

        return;
    }

    if (!password) {
        alert('비밀번호를 입력해 주세요.');

        return;
    }

    if (!confirmPassword) {
        alert('비밀번호 확인을 입력해 주세요.');

        return;
    }

    if (password != confirmPassword) {
        alert('일치하지 않는 비밀번호입니다.');

        return;
    }

    if (!nickname) {
        alert('닉네임을 입력해 주세요.');

        return;
    }

    if (!nicknameDuplicationCheckState) {
        alert('닉네임 중복 확인이 필요합니다.')

        return;
    }

    const profileCreationResult = createProfile();

    if (!profileCreationResult) {
        alert('회원가입에 실패하였습니다.');

        return;
    }

    const accountCreationResult = createAccount();

    if (!accountCreationResult) {
        alert('회원가입에 실패하였습니다.');

        return;
    }

    alert('회원가입을 성공하였습니다.');

    window.location.replace('/login');
}