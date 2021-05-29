$(function() {
    loadData()
});
    function updateView(data) {
        let html = data.map((game) => {
         return  '<div class="col-12 mb-2 col-md-4 mt-4">'
                             +
                                 '<div class="card">'
                                 +
                                     '<div class="card-body">'
                                     +
                                         '<h5 id="gameId">' + 'Game Id: ' + game.id + '</h5>' +
                                         '<p id="gameDate">' + 'Game Date: ' + new Date(game.created).toLocaleString() + '</p>'
                                         +
                                         '<div class="d-flex flex-fill justify-content-center">'
                                         +
                                         '<b>' + 'PLAYERS' + '</b>'
                                         +
                                         '</div>'
                                         +
                                         '<div>'
                                         +
                                         game.gamePlayers.map((player) => {
                                             return `
                                                 <p> Player id: ${player.player.id}</p>
                                                 <p> Player email: ${player.player.email}</p>
                                                     `
                                             })
                                         +
                                         '</div>'
                                     +
                                     '</div>'
                                 +
                                 '</div>'

                             +
                             '</div>';

        }).join("");
        document.getElementById("cards").innerHTML = html;
    }
    function loadData() {
        $.get("/api/games")
            .done(function(data) {
              updateView(data);
            })
            .fail(function( jqXHR, textStatus ) {
              alert( "Failed: " + textStatus );
            });
    }

