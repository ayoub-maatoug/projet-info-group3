//emse_dom_js/js/dom_api.js

// to add a room
function addRoom (room_id, light, noise) {
  // créer un nouvel élément
  var room_list = document.createElement("div"); // creation of an element div
  room_list.id = 'room_id'+room_id; // definition of his id
  room_list.textContent = name; // definition of his content
  room_list.setAttribute('class', 'btn btn-xs btn-info'); // add a style, it becomes a button with the classe btn
  // We add an eventListener for the evenement click
  room_list.addEventListener("click", function () {
      alert("I am the room " + room_id + "the light level is " + light + "the noise level is" + noise); //you can add the function JavaScript you want
  });
  document.getElementById("rooms").appendChild(room_list); // We append this new node

  var br = document.createElement("br"); // creation of an element br
  document.getElementById("rooms").appendChild(br); // We append this new node
}

// Made a call AJAX GET
// in parameters  the URL target and the function callback call when success
function ajaxGet(url, callback) {
    var req = new XMLHttpRequest();
    req.open("GET", url);
    req.addEventListener("load", function () {
        //console.log('statut : ' + req.status);
        if (req.status >= 200 && req.status < 400) {
            // call function callback with an argument which is the request's answer
            callback(req.responseText);
        } else {
            console.error(req.status + " " + req.statusText + " " + url);
        }
    });
    req.addEventListener("error", function () {
        console.error("Erreur réseau avec l'URL " + url);
    });
    req.send(null);

}

// we call the function ajaxGet
//const heroku_url_api = "https://thawing-journey-78988.herokuapp.com/api/rooms";
//https://faircorp.cleverapps.io/api/rooms
ajaxGet("https://thawing-journey-78988.herokuapp.com/api/rooms", function (reponse) {
    // on trace dans la console la réponse
    console.log("Réponse : " + reponse);
    var rooms = JSON.parse(reponse); // Transform the answer in array of JavaScript's objects
    console.log("Rooms : " + rooms);
    rooms.forEach(function (room) {
      addRoom(room.id, room.light.level, room.noise.level);
        console.log("room id : "  + room.id);
    })
});


// // Création d'une requête HTTP
//  var req = new XMLHttpRequest();
//
//  // Requête HTTP GET synchrone vers l'api' publiée
//  req.open("GET", "https://faircorp.cleverapps.io/api/rooms");
//  req.addEventListener("load", function () {
//      if (req.status >= 200 && req.status < 400) { // Le serveur a réussi à traiter la requête
//          console.log(req.responseText);
//      } else {
//          // Affichage des informations sur l'échec du traitement de la requête
//          console.error(req.status + " " + req.statusText);
//      }
//  });
//  req.addEventListener("error", function () {
//      // La requête n'a pas réussi à atteindre le serveur
//      console.error("Erreur réseau");
//  });
//  req.send(null);
