const markdownToTextConverter = new marked.Marked({gfm: true}).use(markedPlaintify());

$(function () {
    $.ajax({
        type: 'GET',
        url: `/api/profile/${nickname}`
    }).done(function (data) {
        $('#nickname').text(data.nickname);
        $('#picture').attr('src', data.picture);
    });

    const today = new Date();
    const startDate = new Date(today.getFullYear(), today.getMonth() - 3, today.getDate() + 1, -today.getTimezoneOffset() / 60);
    const endDate = new Date(today.getFullYear(), today.getMonth(), today.getDate(), -today.getTimezoneOffset() / 60);

    const streakTemplate = (DateHelper) => {
        const MILLISECOND_PER_DAY = 8.64E+7;

        return {
            name: 'streak',
            allowedDomainType: ['month'],
            rowsCount: () => 7,
            columnsCount: (ts) => {
                if (ts < startDate.valueOf()) {
                    ts = startDate.valueOf();

                    const clampStart = DateHelper.date(ts);
                    const clampEnd = DateHelper.getLastWeekOfMonth(ts);

                    return clampEnd.diff(clampStart, 'week') + 1;
                }

                if (DateHelper.getLastWeekOfMonth(ts).valueOf() > endDate.valueOf() + MILLISECOND_PER_DAY) {
                    const clampStart = DateHelper.getFirstWeekOfMonth(ts);
                    const clampEnd = DateHelper.date(endDate.valueOf() + MILLISECOND_PER_DAY);

                    return clampEnd.diff(clampStart, 'week') + 1;
                }

                return DateHelper.getWeeksCountInMonth(ts);
            },
            mapping: (startTimestamp, endTimestamp) => {
                let clampStart = DateHelper.getFirstWeekOfMonth(startTimestamp);
                let clampEnd = DateHelper.getFirstWeekOfMonth(endTimestamp);

                if (startTimestamp < startDate.valueOf()) {
                    clampStart = DateHelper.date(startDate.valueOf());
                }

                if (endTimestamp > endDate.valueOf() + MILLISECOND_PER_DAY) {
                    clampEnd = DateHelper.date(endDate.valueOf() + MILLISECOND_PER_DAY);
                }

                let pivotDay = clampStart.weekday();
                let x = -1;

                return DateHelper.intervals('day', clampStart, clampEnd).map((ts) => {
                    const weekday = DateHelper.date(ts).weekday();

                    if (weekday == pivotDay) {
                        pivotDay = 0;
                        x++;
                    }

                    return {
                        t: ts,
                        x: x,
                        y: weekday
                    };
                });
            },
            extractUnit: (ts) => DateHelper.date(ts).startOf('day').valueOf()
        };
    };

    $.ajax({
        type: 'GET',
        url: `/api/til/streak/${nickname}?startDate=${convertDateToString(startDate)}&endDate=${convertDateToString(endDate)}`
    }).done(function (data) {
        let maximumTilNumber = 0;

        for (let streak of data.streaks) {
            maximumTilNumber = Math.max(maximumTilNumber, streak.tilNumber);
        }

        const calHeatmap = new CalHeatmap();

        calHeatmap.addTemplates(streakTemplate);

        calHeatmap.paint({
            itemSelector: '#streak-graph',
            range: 4,
            domain: {
                type: 'month',
                gutter: 2,
                label: {
                    text: 'MMM'
                }
            },
            subDomain: {
                type: 'streak'
            },
            date: {
                start: startDate
            },
            data: {
                source: data.streaks,
                x: 'date',
                y: 'tilNumber'
            },
            scale: {
                color: {
                    range: ['#00FFD9', '#00947E', '#002923'],
                    domain: [0, maximumTilNumber]
                }
            }
        }, [
            [
                Tooltip,
                {
                    text: (timestamp, value, dayjsDate) => {
                        if (value == null) {
                            value = 0;
                        }

                        const unit = (value <= 1) ? ('TIL') : ('TILs');

                        return value + ' ' + unit + ' on ' + dayjsDate.format('LL');
                    }
                }
            ]
        ]);
    });

    $.ajax({
        type: 'GET',
        url: `/api/tag/list/profile/${nickname}`
    }).done(function (data) {
        data.tags.forEach(tag => {
            $('#tags').append(`<a href="/profile/${nickname}?tag=${tag}&page=1&size=5">${tag}</a>`);
        });
    });

    $.ajax({
        type: 'GET',
        url: `/api/til/list/${nickname}?${(tag == null) ? ("") : ("tag=" + tag + "&")}page=${page - 1}&size=${size}`
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

function convertDateToString(date) {
    return date.getFullYear() + '-' + (date.getMonth() + 1).toString().padStart(2, '0') + '-' + date.getDate().toString().padStart(2, '0');
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