const markdownToTextConverter = new marked.Marked({gfm: true}).use(markedPlaintify());

$(function () {
    $.ajax({
        type: 'GET',
        url: '/api/til/list?size=5'
    }).done(function (data) {
        const tils = data.tils;

        tils.forEach(til => {
            $('#recent-til').append(`
                <div class="card mb-4">
                    <a href="/til/${til.uuid}?page=1&size=5">
                        <div class="card-content">
                            <div class="media">
                                <div class="media-left">
                                    <figure class="image is-24x24">
                                        <img src="${til.picture}">
                                    </figure>
                                </div>
                                
                                <div class="media-content">
                                    <h5 class="title is-5">${til.nickname}</h5>
                                </div>
                            </div>
                            
                            <div class="content">
                                <h4 class="title is-4">${til.title}</h4>
                                
                                <p class="has-text-black">${markdownToTextConverter.parse(til.content).substring(0, 30)}</p>
                            </div>
                        </div>
                    </a>
                </div>
            `);
        });
    });
});