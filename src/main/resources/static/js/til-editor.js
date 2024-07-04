const simpleMde = new SimpleMDE({
    element: $('#content-textarea')[0],
    indentWithTabs: false,
    placeholder: '내용',
    spellChecker: false,
    tabSize: 4
});

$(function () {
    if (uuid) {
        $.ajax({
            type: 'GET',
            url: `/api/til/${uuid}`
        }).done(function (data) {
            $('#title-input').val(data.title);

            simpleMde.value(data.markdown);

            let tags = '';

            data.tags.forEach(tag => {
                tags += tag + ', ';
            });

            $('#tags-input').val(tags.substring(0, tags.length - 2));
        });
    }
});

function completeEdit() {
    const title = $('#title-input').val();
    const content = simpleMde.value();
    const tags = ($('#tags-input').val()) ? ($('#tags-input').val().trim().split(/\s*,\s*/)) : ([]);

    if (!title) {
        alert('제목을 입력해 주세요.');

        return;
    }

    if (!content) {
        alert('내용을 입력해 주세요.');

        return;
    }

    if (uuid) {
        $.ajax({
            type: 'PUT',
            url: `/api/til/${uuid}`,
            headers: {
                'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
            },
            contentType: 'application/json',
            data: JSON.stringify({
                'title': title,
                'content': content,
                'tags': tags
            })
        }).done(function () {
            alert('TIL 수정을 성공했습니다.');
            window.location.replace(`/til/${uuid}?page=1&size=5`);
        }).fail(function () {
            alert('TIL 수정을 실패했습니다.');
        });
    } else {
        $.ajax({
            type: 'POST',
            url: '/api/til',
            headers: {
                'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
            },
            contentType: 'application/json',
            data: JSON.stringify({
                'title': title,
                'content': content,
                'tags': tags
            })
        }).done(function () {
            alert('TIL 작성을 성공했습니다.');
            window.location.replace(`/profile/${memberNickname}?page=1&size=5`);
        }).fail(function () {
            alert('TIL 작성을 실패했습니다.');
        });
    }
}

function cancelEdit() {
    if (uuid) {
        window.location.replace(`/til/${uuid}?page=1&size=5`);

        return;
    }

    window.location.replace(`/profile/${memberNickname}?page=1&size=5`);
}