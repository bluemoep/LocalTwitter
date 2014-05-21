/*
 * Inspired by https://gist.github.com/wadey/442463
 */

function TweetParser(tweet) {

	var text = tweet.text;
	var indexMap = [];
	var textChanged = false;

	var escapeHTML = function(text) {
		return $('<div/>').text(text).html();
	};

	var parseHashtags = function() {
		textChanged = true;
		var hashtag;
		for (var i = 0; i < tweet.entities.hashtags.length; i++) {
			hashtag = tweet.entities.hashtags[i];
			indexMap[hashtag.indices[0]] = {
				end : hashtag.indices[1],
				content : '#<a href="//twitter.com/search/'
						+ encodeURIComponent('#' + hashtag.text) + '">'
						+ escapeHTML(hashtag.text) + '</a>'
			};
		}
	};

	var parseSymbols = function() {
		textChanged = true;
		var symbol;
		for (var i = 0; i < tweet.entities.symbols.length; i++) {
			symbol = tweet.entities.symbols[i];
			indexMap[symbol.indices[0]] = {
				end : symbol.indices[1],
				content : '$<a href="//twitter.com/search/'
						+ encodeURIComponent('$' + symbol.text) + '">'
						+ escapeHTML(symbol.text) + '</a>'
			};
		}
	};

	var parseUrls = function() {
		textChanged = true;
		var url;
		for (var i = 0; i < tweet.entities.urls.length; i++) {
			url = tweet.entities.urls[i];
			indexMap[url.indices[0]] = {
				end : url.indices[1],
				content : '<a href="' + url.expanded_url + '">'
						+ escapeHTML(url.display_url) + '</a>'
			};
		}
	};

	var parseUserMentions = function() {
		textChanged = true;
		var mention;
		for (var i = 0; i < tweet.entities.user_mentions.length; i++) {
			mention = tweet.entities.user_mentions[i];
			indexMap[mention.indices[0]] = {
				end : mention.indices[1],
				content : '@<a href="//twitter.com/'
						+ encodeURIComponent(mention.screen_name) + '">'
						+ escapeHTML(mention.screen_name) + '</a>'
			};
		}
	};

	var parseMedia = function() {
		textChanged = true;
		var media;
		for (var i = 0; i < tweet.entities.media.length; i++) {
			media = tweet.entities.media[i];
			indexMap[media.indices[0]] = {
				end : media.indices[1],
				content : '<a href="'
						+ media.expanded_url
						+ '"><img width="'
						+ media.sizes.thumb.w
						+ '" height="'
						+ media.sizes.thumb.h
						+ '" src="'
						+ media.media_url
								.substr(media.media_url.indexOf(':') + 1)
						+ '" alt="' + escapeHTML(media.display_url)
						+ '" /></a>'
			};
		}
	};

	this.parse = function() {
		parseHashtags();
		parseSymbols();
		parseUrls();
		parseUserMentions();
		parseMedia();
		return this.getText();
	};

	this.getText = function() {
		if (textChanged) {
			var result = '';
			var lastEnd = 0;
			for (var i = 0; i < tweet.text.length; i++) {
				var index = indexMap[i];
				if (index) {
					var start = i;
					var end = index.end;
					if (start > lastEnd)
						result += tweet.text.substring(lastEnd, start);
					result += index.content;
					i = end - 1;
					lastEnd = end;
				}
			}

			if (i > lastEnd)
				result += tweet.text.substr(lastEnd);

			text = result;
		}
		return text;
	};

}