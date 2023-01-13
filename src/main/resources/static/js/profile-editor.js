let nicknameDuplicationCheckState = true;

let pictureFile = null;

$(function () {
    $.ajax({
        type: 'GET',
        url: `/api/profile/${memberNickname}`
    }).done(function (data) {
        $('#picture').attr('src', data.picture);
        $('#nickname-input').val(data.nickname);
    });
});

$('#picture-file-input').change(function (eventData) {
    if (eventData.target.files.length > 0) {
        pictureFile = eventData.target.files[0];

        const fileReader = new FileReader();

        fileReader.onload = function () {
            $('#picture').attr('src', fileReader.result);
        };

        fileReader.readAsDataURL(pictureFile);

        $('#picture-file-name').text(pictureFile.name);
    }
});

$('#nickname-input').change(function () {
    nicknameDuplicationCheckState = false;
});

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

function updateProfile() {
    const nickname = $('#nickname-input').val();

    $.ajax({
        type: 'PATCH',
        url: '/api/profile',
        contentType: 'application/json',
        data: JSON.stringify({
            'nickname': nickname
        }),
        async: false
    });
}

function uploadProfilePicture(pictureFile) {
    const formData = new FormData();

    formData.append('pictureFile', pictureFile);

    $.ajax({
        type: 'PATCH',
        url: '/api/profile/picture',
        contentType: false,
        processData: false,
        data: formData,
        async: false
    });
}

function completeEdit() {
    if (nicknameDuplicationCheckState == false) {
        alert('닉네임 중복 확인이 필요합니다.');

        return;
    }

    const nickname = $('#nickname-input').val();

    updateProfile();

    if (pictureFile != null) {
        uploadProfilePicture(pictureFile);
    }

    window.location.replace(`/profile/${nickname}`);
}

function cancelEdit() {
    window.location.replace(`/profile/${memberNickname}`);
}