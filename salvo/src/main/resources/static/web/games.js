
$(function() {
  // display text in the output area
  function showOutput(text) {
    $("#games").text(text);
  }

  //Display list of games object
  function showList(text){
    $("#liGames").text(text);
  }

  // load and display JSON sent by server for /players
  function loadData() {
    $.get("/api/games")
    .done(function(data) {
      showList(JSON.stringify(data, null, 2));

      console.log(data);
    })
    .fail(function( jqXHR, textStatus ) {
      showOutput( "Failed: " + textStatus );
    });
  }

  loadData();
});