const markdownToTextConverter = new marked.Marked({gfm: true}).use(markedPlaintify());

$(function () {
    $.ajax({
        type: 'GET',
        url: `/api/profile/${nickname}`
    }).done(function (data) {
        $('#nickname').text(data.nickname);
        $('#picture').attr('src', data.picture);
    });

    $.ajax({
        type: 'GET',
        url: `/api/til/list/${nickname}?page=${page - 1}&size=${size}`
    }).done(function (data) {
        const tils = data.tils;
        const totalPages = data.totalPages;

        tils.forEach(til => {
            $('#til').append(`
                <div class="card mb-4">
                    <a href="/til/${til.uuid}?page=1&size=5">
                        <div class="card-content">
                            <div class="content">
                                <h4 class="title is-4">${til.title}</h4>
                                
                                <p class="has-text-black">${markdownToTextConverter.parse(til.content).substring(0, 30)}</p>
                            </div>
                        </div>
                    </a>
                </div>
            `);
        });

        createPagination(page, totalPages, 2);
    });
});

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
                    <a href="/profile/${nickname}?page=${pageNumber}&size=${size}" class="pagination-link has-background-primary-dark has-text-white">${pageNumber}</a>
                </li>
            `);
        } else {
            paginationList.append(`
                <li>
                    <a href="/profile/${nickname}?page=${pageNumber}&size=${size}" class="pagination-link">${pageNumber}</a>
                </li>
            `);
        }
    }
}