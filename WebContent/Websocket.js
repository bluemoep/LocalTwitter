function Websocket() {

	// Singleton
	if (typeof Websocket.instance === 'object') {
		return Websocket.instance;
	}
	Websocket.instance = this;

	var getOrigin = function() {
		// TODO: Find current host + root
		return "localhost:8080/LocalTwitter";
	};

	// Defaults
	Websocket.host = "ws://" + getOrigin() + "/ws";
	
	var socket = new WebSocket(Websocket.host);
	
	socket.onopen = function() {
		// TODO: Send Boundaries
	};

	socket.onmessage = function(message) {
		// TODO: Notify LTmap
	};

	socket.onclose = function() {
		// TODO: Reconnect!
		// TODO: Do not reconnect if window is closing and onclose is fired.
	};

	this.send = function(message) {
		// TODO: Maybe JSON.stringify necessary, MAYBE!
		socket.send(message);
	};
}