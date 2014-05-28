function Websocket() {
	
	// Singleton
	if (typeof Websocket.instance === 'object') {
		return Websocket.instance;
	}
	Websocket.instance = this;
	
	// Defaults
	Websocket.socket = null;
	Websocket.host = "ws://...";
	
	var serverMessage;
	
	this.connect = function() {
		try {
			Websocket.socket = new WebSocket(Websocket.host);
			Websocket.socket.onopen = function() {
				altert("Connected");
			}
			
			Websocket.socket.onclose = function() {
				alert("Closed");
			}
		} catch (exception) {
			console.log("Error " +exception);
		}
	}
	
	this.sendMessage = function(tweet) {
		Websocket.socket.send(tweet);
	}
	
	this.serverMessage = function() {
		Websocket.socket.onmessage = function(message) {
			return message.data;
		}
	}
}