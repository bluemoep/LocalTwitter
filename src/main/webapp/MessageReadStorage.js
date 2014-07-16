function MessageReadStorage() {
	
	// Singleton
	if (typeof MessageReadStorage.instance === 'object') {
		return MessageReadStorage.instance;
	}
	MessageReadStorage.instance = this;
	
	var messagesRead = JSON.parse(localStorage.getItem('messagesRead'));
	if(messagesRead == null)
		messagesRead = {};
	
	var flush = function() {
		localStorage.setItem('messagesRead', JSON.stringify(messagesRead));
	};
	
	var flushTimeout = null;
	var flushTimer = function() {
		clearTimeout(flushTimeout);
		flushTimeout = setTimeout(flush, 1000);
	};
	
	// Cleanup
	setTimeout(function() {
		var cut = new Date();
		cut.setDate(cut.getDate()-14);
		
		for(var key in messagesRead) {
			if(messagesRead[key] < cut)
				delete messagesRead[key];
		}
		
		flushTimer();
	}, 10000);
	
	this.addMessage = function(tweet) {
		messagesRead[tweet.id_str] = new Date(tweet.created_at);
		flushTimer();
	};
	
	this.messageRead = function(id_str) {
		return messagesRead.hasOwnProperty(id_str);
	};
	
}