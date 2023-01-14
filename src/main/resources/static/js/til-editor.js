const simpleMde = new SimpleMDE({
    element: $('#content-textarea')[0],
    indentWithTabs: false,
    placeholder: '내용',
    spellChecker: false,
    tabSize: 4
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

    $.ajax({
        type: 'POST',
        url: '/api/til',
        contentType: 'application/json',
        data: JSON.stringify({
            'title': title,
            'content': content,
            'tags': tags
        })
    }).done(function () {
        alert('TIL 작성을 성공했습니다.');
        window.location.replace(`/profile/${memberNickname}`);
    }).fail(function () {
        alert('TIL 작성을 실패했습니다.');
    });
}

function cancelEdit() {
    window.location.replace(`/profile/${memberNickname}`);
}