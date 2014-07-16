/**
 * @DEPRECATED
 */

function MessageStorage() {
	
	// Singleton
	if (typeof MessageStorage.instance === 'object') {
		return MessageStorage.instance;
	}
	MessageStorage.instance = this;
	
	var latestMessage = JSON.parse(localStorage.getItem('messages'));
	
	var flush = function() {
		localStorage.setItem('messages', JSON.stringify(latestMessage));
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
		
		var currentMessage = latestMessage;
		var lastCurrentMessage = null;
		while(currentMessage.createdAt > cut) {
			lastCurrentMessage = currentMessage;
			currentMessage = currentMessage.next;
		}
		lastCurrentMessage.next = null;
		
		flushTimer();
	}, 10000);
	
	this.addMessage = function(tweet) {
		var message = {
			id : tweet.id,
			createdAt : new Date(tweet.created_at),
			readStatus : false,
			tweet : tweet,
			next : null
		};
		
		var currentMessage = latestMessage;
		var lastCurrentMessage = null;
		while(currentMessage.createdAt > message.createdAt) {
			lastCurrentMessage = currentMessage;
			currentMessage = currentMessage.next;
		}
		
		// Check if message exists in localStorage
		var currentMessage2 = currentMessage;
		while(currentMessage2.createdAt == message.createdAt) {
			if(currentMessage2.id == message.id)
				return;
			currentMessage2 = currentMessage2.next;
		}
		
		message.next = currentMessage;
		// Check if this is the latest message, otherwise this is lastCurrentMessage's next message
		if(lastCurrentMessage == null)
			latestMessage = message;
		else
			lastCurrentMessage.next = message;
		
		flushTimer();
	};
	
	this.getLatestMessage = function() {
		return latestMessage;
	};
	
}