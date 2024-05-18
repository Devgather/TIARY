$(function () {
    $.ajax({
        type: 'GET',
        url: `/api/til/${uuid}`
    }).done(function (data) {
        $('#title').text(data.title);
        $('#author').text(data.author);
        $('#created-date').text(data.createdDate);
        $('#content').html(data.content);

        hljs.highlightAll();
    });

    $.ajax({
        type: 'GET',
        url: `/api/comment/list/${uuid}?page=${page - 1}&size=${size}`
    }).done(function (data) {
        const comments = data.comments;
        const totalPages = data.totalPages;

        comments.forEach(comment => {
            let commentHtml = `
                <div class="box is-shadowless mb-4">
                    <div class="is-flex is-justify-content-space-between mb-3">
                        <div class="is-flex is-flex-direction-row">
                            <p>
                                <a href="/profile/${comment.nickname}?page=1&size=5" class="nickname">${comment.nickname}</a>
                            </p>

                            <p>&nbsp;·&nbsp;</p>

                            <p>${comment.createdDate}</p>
                        </div>
            `;

            if (memberNickname == comment.nickname) {
                commentHtml += `
                    <div id="comment-buttons-${comment.uuid}" class="buttons mb-0">
                        <button class="button has-background-primary-dark has-text-white is-size-7" onclick="editComment('${comment.uuid}')">수정</a>
                        <button class="button is-size-7" onclick="deleteComment('${comment.uuid}')">삭제</button>
                    </div>
                    
                    <div id="comment-edit-buttons-${comment.uuid}" class="buttons mb-0 is-hidden">
                        <button class="button has-background-primary-dark has-text-white is-size-7" onclick="completeCommentEdit('${comment.uuid}')">완료</button>
                        <button class="button is-size-7" onclick="cancelCommentEdit('${comment.uuid}')">취소</button>
                    </div>
                `;
            }

            commentHtml += `
                    </div>
    
                    <div id="comment-content-${comment.uuid}" class="is-size-6">${comment.content}</div>
                    
                    <input type="text" placeholder="댓글" value="${comment.content}" id="comment-edit-input-${comment.uuid}" class="input is-hidden">
                </div>
            `;

            $('#comment').append(commentHtml);
        });

        createPagination(page, totalPages, 2);
    });
});

function showCommentEditComponents(commentUuid) {
    if (!$(`#comment-buttons-${commentUuid}`).hasClass('is-hidden')) {
        $(`#comment-buttons-${commentUuid}`).addClass('is-hidden');
    }

    $(`#comment-edit-buttons-${commentUuid}`).removeClass('is-hidden');

    if (!$(`#comment-content-${commentUuid}`).hasClass('is-hidden')) {
        $(`#comment-content-${commentUuid}`).addClass('is-hidden');
    }

    $(`#comment-edit-input-${commentUuid}`).removeClass('is-hidden');
}

function hideCommentEditComponents(commentUuid) {
    $(`#comment-buttons-${commentUuid}`).removeClass('is-hidden');

    if (!$(`#comment-edit-buttons-${commentUuid}`).hasClass('is-hidden')) {
        $(`#comment-edit-buttons-${commentUuid}`).addClass('is-hidden');
    }

    $(`#comment-content-${commentUuid}`).removeClass('is-hidden');

    if (!$(`#comment-edit-input-${commentUuid}`).hasClass('is-hidden')) {
        $(`#comment-edit-input-${commentUuid}`).addClass('is-hidden');
    }
}

function deleteTil() {
    $.ajax({
        type: 'DELETE',
        url: `/api/til/${uuid}`,
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        }
    }).done(function () {
        alert('TIL 삭제를 성공했습니다.');
        window.location.replace(`/profile/${memberNickname}?page=1&size=5`);
    });
}

function writeComment() {
    if (!memberNickname) {
        alert('로그인이 필요합니다.');

        return;
    }

    const commentContent = $('#comment-input').val();

    if (!commentContent) {
        alert('댓글을 입력해 주세요.');

        return;
    }

    $.ajax({
        type: 'POST',
        url: '/api/comment',
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        },
        contentType: 'application/json',
        data: JSON.stringify({
            'tilUuid': uuid,
            'content': commentContent
        })
    }).done(function () {
        alert('댓글 작성을 성공했습니다.');
        window.location.reload();
    }).fail(function () {
        alert('댓글 작성을 실패했습니다.');
    });
}

function editComment(commentUuid) {
    showCommentEditComponents(commentUuid);
}

function completeCommentEdit(commentUuid) {
    const commentContent = $(`#comment-edit-input-${commentUuid}`).val();

    $.ajax({
        type: 'PUT',
        url: `/api/comment/${commentUuid}`,
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        },
        contentType: 'application/json',
        data: JSON.stringify({
            'content': commentContent
        })
    }).done(function () {
        alert('댓글 수정을 성공했습니다.');
        window.location.reload();
    }).fail(function () {
        alert('댓글 수정을 실패했습니다.');
    });
}

function cancelCommentEdit(commentUuid) {
    hideCommentEditComponents(commentUuid);
}

function deleteComment(commentUuid) {
    $.ajax({
        type: 'DELETE',
        url: `/api/comment/${commentUuid}`,
        headers: {
            'X-XSRF-TOKEN': document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1')
        }
    }).done(function () {
        alert('댓글 삭제를 성공했습니다.');
        window.location.replace(`/til/${uuid}?page=1&size=5`);
    });
}

function createPagination(currentPage, totalPages, offset) {
    let firstPage = currentPage - offset;
    let lastPage = currentPage + offset;

    if (firstPage < 1) {
        firstPage = 1;

        if (lastPage < firstPage + offset * 2) {
            lastPage = firstPage + offset * 2;
        }
    }

    if (lastPage > totalPages) {
        lastPage = totalPages;

        if (firstPage > lastPage - offset * 2) {
            firstPage = lastPage - offset * 2;
        }
    }

    if (firstPage < 1) {
        firstPage = 1;
    }

    const paginationList = $('#pagination-list');

    for (let pageNumber = firstPage; pageNumber <= lastPage; pageNumber++) {
        if (pageNumber == currentPage) {
            paginationList.append(`
                <li>
                    <a href="/til/${uuid}?page=${pageNumber}&size=${size}" class="pagination-link has-background-primary-dark has-text-white">${pageNumber}</a>
                </li>
            `);
        } else {
            paginationList.append(`
                <li>
                    <a href="/til/${uuid}?page=${pageNumber}&size=${size}" class="pagination-link">${pageNumber}</a>
                </li>
            `);
        }
    }
}