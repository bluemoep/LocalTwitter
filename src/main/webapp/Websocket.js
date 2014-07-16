function Websocket() {

	// Singleton
	if (typeof Websocket.instance === 'object') {
		return Websocket.instance;
	}
	Websocket.instance = this;

	var getOrigin = function() {
		return window.location.host + window.location.pathname;
	};

	// Defaults
	Websocket.host = 'ws://' + getOrigin() + 'ws';
	
	var socket = new WebSocket(Websocket.host);
	var open = false;
	
	var onopen = function() {
		open = true;
		new LTmap().update();
	};

	var onmessage = function(message) {
		new LTmap().addMessage(JSON.parse(message.data));
	};

	var onclose = function() {
		open = false;
		// Reconnect after 1 second
		setTimeout(connect, 1000);
	};
	
	var connect = function() {
		socket = new WebSocket(Websocket.host);
		socket.onopen = onopen;
		socket.onmessage = onmessage;
		socket.onclose = onclose;
	};
	
	connect();

	this.send = function(message) {
		if(open)
			socket.send(JSON.stringify(message));
	};
}