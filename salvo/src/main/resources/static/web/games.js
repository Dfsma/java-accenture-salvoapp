$(function() {
    loadData()
});
    function updateView(data) {
        let html = data.map((game) => {
           return  '<li>' + '<h1>' + 'Game id: ' + game.id + ' ' + 'Game Date: ' + new Date(game.created).toLocaleString() + '</h1>'
                    +
                    game.gamePlayers.map((player) => {
                    return `
                        <p> Player id: ${player.player.id}</p>
                        <p> Player email: ${player.player.email}</p>
                            `
                    })
                    +
                    '</li>';

        }).join("");
        document.getElementById("liGames").innerHTML = html;
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

