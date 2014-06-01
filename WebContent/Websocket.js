function Websocket() {

	// Singleton
	if (typeof Websocket.instance === 'object') {
		return Websocket.instance;
	}
	Websocket.instance = this;

	// Defaults
	Websocket.socket = null;
	Websocket.host = "ws://localhost:8080/LocalTwitter/ws";

	this.connect = function() {
		try {
			Websocket.socket = new WebSocket(Websocket.host);
			Websocket.socket.onopen = function() {
				alert("Connected");
			};

			Websocket.socket.onmessage = function(message) {
				alert(message.data);
			};

			Websocket.socket.onclose = function() {
				alert("Closed");
			};
		} catch (exception) {
			console.log("Error " + exception);
		}
	};

	this.sendMessage = function(tweet) {
		Websocket.socket.send(tweet);
	};
}