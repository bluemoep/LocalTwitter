// Websocket Objekt
var socket;
var host = "ws://...";

// Methode zum verbindugsaufbau
function webSocketConnect() {
	try {
		var socket = new WebSocket(host);
	
		// erstellt die eingentliche Verbindung
		socket.onopen = function() {
			alert("Connection " +socket.readyState());
		}	

		// passiert wenn Servernachrichten empfangen wird
		socket.onmessage = function(serverMessage) {
			console.log('Server Message: ' +serverMessage.data);
		}

		// passiert beim schlieﬂen der Verbindung
		socket.onclose = function() {
			alter("Connection is closed");
		}
	} catch(exception) {
		console.log('Error: ' +exception);
	}
}

// Methode zum Nachrichten senden
function sendMessage(Message message) {
	socket.send(message);
}

// Methode zum Verbindung schlieﬂen
function closeSocket() {
	socket.close();
}