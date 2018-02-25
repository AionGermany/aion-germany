/* Using abbreviation YG for all YG content related*/
if (typeof YG == "undefined")
{
	/*the YG object it's an actual collection of methods for embedding different objects*/
	var YG = {};
}

YG.Versioning = 15;
YG.UserSettings = {};
YG.Tooltip =
{
	//CustomData: '',
	Id: 'myYG_tooltip',
	Style: 'yg_tooltip',
	CustomHtml: '<div class="yg_pageBorder"><div class="yg_TL yg_BK"></div><div class="yg_TR yg_BK"></div></div><div class="yg_page yg_tooltips">##myYGTooltip_customContent##</div></div><div class="yg_pageBorder"><div class="yg_BL yg_BK"></div><div class="yg_BR yg_BK"></div></div>',
	Delay: 200,
	LastRequest: null,
	Pos:
	{
		X: -1000,
		Y: -1000
	},

	/* all coordinates obtain for mouse events are relative (no scroll info)*/
	UpdateCursor: function(mouseX, mouseY)
	{
		this.Pos.X = mouseX; this.Pos.Y = mouseY;
	},

	InitTooltip: function(event)
	{
		var target, regExp, matches, links, customTooltip;
		if (!event)
			event = window.event;

		if (event.target != null)
			target = event.target;
		else
			target = event.srcElement;

		/*tooltip has more than just the event parameter so we are trying to display a custom tooltip, the second param*/
		if (arguments.length > 1)
			customTooltip = arguments[1];

		/* we need the target element, if this is not a custom tooltip then event handling will be added
		just for anchor tags since only those can provide the default tooltip info needed*/
		if (target && (customTooltip || (!customTooltip && target.href)))
		{
			/*reseting the timer until we get the user to stay on the same object more than x amount of time*/
			if (this.__ajaxTimer && (this.__ajaxTimer > 0))
			{
				clearTimeout(this.__ajaxTimer);
				this.__ajaxTimer = 0;
			}

			/*update cursor*/
			this.UpdateCursor(event.clientX, event.clientY);

			/*set mouve events on the target, needed so we clear timer and cancel showing the tooltip if the user moves away from the object*/
			target.onmousemove = this.MoveTooltip;
			target.onmouseout = this.HideTooltip;

			/*set timer to show/request the tooltip*/
			if (customTooltip)
			{
				if (this.CustomHtml)
					customTooltip = this.CustomHtml.replace('##myYGTooltip_customContent##', customTooltip);
				/* saving what we need from the event before the browser overwrites or trash it. */
				var myEventX = event.clientX, myEventY = event.clientY;
				this.__ajaxTimer = setTimeout(function() {YG.Tooltip.ShowTooltip(myEventX, myEventY, customTooltip)}, this.Delay);
			}
			else
				this.__ajaxTimer = setTimeout(function() {YG.Tooltip.RequestTooltip(target.href)}, this.Delay);
		}
	},

	RequestTooltip: function(source)
	{
		if (!source)
				return;

		/*check to see if it's one of the YG links, if so obtain all the info from the anchor about the requested object*/
		var objInfo = YG.Common.matchToYg(source);
		if (objInfo && objInfo.Net)
		{
			/*check to see if we already have the info for the tooltip*/
			var data = YG.Network.GetTooltipDataObj(objInfo.Net);
			if (!data)
				return;

			/*check if this is one of the support network languages*/
			if (!YG.Network.Languages[objInfo.Lang])
				return;

			if (!data[objInfo.Type])
				data[objInfo.Type] = {};
			if (!data[objInfo.Type][objInfo.Id])
				data[objInfo.Type][objInfo.Id] = {};

			if (data[objInfo.Type][objInfo.Id][objInfo.Lang] == null)
			{
				/*make the call to load the data*/
				/*default lang is english but since the url code for that is an empty string for that we just use 'en',
				so switch back to empty string when doing the request*/
				YG.Common.ajaxishRequest('http://' + objInfo.Server + '/tooltips/' + objInfo.Type +
					'/' + objInfo.Lang + '/obj_' + objInfo.Type + '_' + objInfo.Id + '_ajx.js');

				/*update the requested tooltip object data as IN PROGRESS*/
				data[objInfo.Type][objInfo.Id][objInfo.Lang] = {};

				/*show that we are loading the data*/
				this.ShowTooltip(this.Pos.X, this.Pos.Y, ' Loading ...');

				/*update the global object with IN PROGRESS object info*/
				this.LastRequest = objInfo;
			}
			else if (data[objInfo.Type][objInfo.Id][objInfo.Lang].tooltip)
				/*info is there already so just show it*/
				this.ShowTooltip(this.Pos.X, this.Pos.Y, data[objInfo.Type][objInfo.Id][objInfo.Lang].tooltip);
		}
	},

	ShowTooltip: function(mouseX, mouseY, content)
	{
		if (!this.Id)
			return;

		var tooltipObj = document.getElementById(this.Id);
		/*create tooltip actual container if not present*/
		if (tooltipObj == null)
		{
			tooltipObj = document.createElement("DIV");
			tooltipObj.id = this.Id;
			if (this.Style)
				tooltipObj.className = this.Style;
			tooltipObj.style.position = 'absolute';
			tooltipObj.style.zIndex = '99999999';
			document.body.appendChild(tooltipObj);
		}

		/*hide and move the tooltip to a location that doesn't mess up the current window width and heigth, so new content will not update the scroll values of the window*/
		tooltipObj.style.visibility = 'hidden';
		tooltipObj.style.top = '-1000px';
		tooltipObj.style.left = '-1000px';
		tooltipObj.innerHTML = content;

		/*move the tooltip to a new visible location inside the window*/
		this.MoveTo(mouseX, mouseY);
		tooltipObj.style.visibility = 'visible';
	},

	/*moves the tooltip div, must be linked to a MouseEvent*/
	MoveTooltip: function(event)
	{
		if (!event)
			event = window.event;

		var myObj = YG.Tooltip;
		/*keeping track of where the mouse is*/
		myObj.UpdateCursor(event.clientX, event.clientY);

		/*ShowTooltip may not even executed until now so mess with the tooltip only if we have one and it's visible*/
		var tooltipObj = document.getElementById(myObj.Id);
		if (!tooltipObj)
			return;
		if (tooltipObj.style.visibility != 'visible')
			return;

		if (!event)
			event = window.event;

		/*move the tooltip to a new visible location inside the window that tracks mouse movement*/
		myObj.MoveTo(event.clientX, event.clientY);
	},

	/*hides the tooltip div, must be linked to a MouseEvent*/
	HideTooltip: function (event)
	{
		if (!event)
			event = window.event;

		if (event.target != null)
			target = event.target;
		else
			target = event.srcElement;

		var timer = YG.Tooltip.__ajaxTimer;
		/*resetting the ShowTooltip timer so we don't show anything if the user already moved away from the object*/
		if (timer && (timer > 0))
		{
			clearTimeout(timer);
			timer = 0;
		}

		/*cleaning up events*/
		target.onmousemove = null;
		target.onmouseout = null;
		YG.Tooltip.LastRequest = null;

		/*hide the tooltip*/
		var tooltipObj = document.getElementById(YG.Tooltip.Id);
		if (!tooltipObj)
			return;
		tooltipObj.style.visibility = 'hidden';
	},

	/*actually moves the tooltip div*/
	MoveTo: function(newX, newY)
	{
		var tooltipObj = document.getElementById(this.Id);
		if (!tooltipObj)
			return;

		/*compute new coordonates and take into consideration the scroll values of the window*/
		var coords = this.__getAbsolutePosition(newX, newY, tooltipObj);

		/*update absolute div placement inside the window*/
		tooltipObj.style.left = coords.X.toString() + 'px';
		tooltipObj.style.top = coords.Y.toString() + 'px';
	},

	/*computes tooltip div placement values based on the window size and scroll*/
	__getAbsolutePosition: function(x, y, tooltipObj)
	{
		var scrollX, scrollY, windowX, windowY, newX, newY;
		/* getting browser viewport start*/
		if ((typeof window.innerWidth) != 'undefined')
		{
			windowX = window.innerWidth;
			windowY = window.innerHeight;
		}
		else if (((typeof document.documentElement) != 'undefined') && ((typeof document.documentElement.clientWidth) != 'undefined') && (document.documentElement.clientWidth != 0))
		{
			windowX = document.documentElement.clientWidth;
			windowY = document.documentElement.clientHeight;
		}
		else
		{
			windowX = document.body.clientWidth;
			windowY = document.body.clientHeight;
		}
		/* getting browser viewport end */
		if (typeof(window.pageXOffset) == 'undefined')
		{
			//IE
			scrollX = ((parseInt(document.body.scrollLeft, 10) > parseInt(document.documentElement.scrollLeft, 10)) ? document.body.scrollLeft : document.documentElement.scrollLeft);
			scrollY = ((parseInt(document.body.scrollTop, 10) > parseInt(document.documentElement.scrollTop, 10)) ? document.body.scrollTop : document.documentElement.scrollTop);
		}
		else
		{
			scrollX = window.pageXOffset;
			scrollY = window.pageYOffset;
		}

		/*compute for a horizontal scroll bar being present*/
		var windowWidthWithoutHorizontalScrollBar = windowX - 30;
		if (y + tooltipObj.clientHeight > windowY)
			newY = windowY - tooltipObj.clientHeight - 10;
		else
			newY = y + 10;

		if (x + tooltipObj.clientWidth >= windowWidthWithoutHorizontalScrollBar)
			newX = x - tooltipObj.clientWidth - 10;
		else
			newX = x + 10;

		return {X: newX + scrollX, Y: newY + scrollY};
	}
};

YG.Network =
{
	aion:
	{
		Id: 12,
		//Str: 'Aion',
		Url: 'aion.yg.com',
		__Data: { Tooltip:[], Inventory:[], Maps:[]},
		__matchToYg: function(url, network) {return YG.Network.__matchToYg(url, network);},
		objects: {item:1, recipe:2, quest:3, title:4, itemset:5, /*zone:6,*/ npc:7, skill:9, gathersource:10, emote:11, abyssrank:12, legion:14}
	},

	aoc:
	{
		Id: 10,
		Url: 'aoc.yg.com',
		__Data: { Tooltip:[]},
		__matchToYg: function(url, network)
		{
			var regExp = /http:\/\/(([a-z]+)\d*\.yg\.com)\/([a-z]{2}\/)?(\w+)\/.+?\?h=([^&]+)$/i;
			var matches = url.match(regExp);
			var typeId;

			/*lowercase network key as string*/
			network = network.toLowerCase();
			if (matches && (matches.length > 4) && matches[2] && (matches[2].indexOf(network) >= 0))
			{
				/*if (this.objExclusions)
					for (var i = 0; i < this.objExclusions.length; i++)
						if (this.objExclusions[i] == matches[4])
							return;*/

				/*check the object type against the network objects*/
				if (matches[4] && this.objects[matches[4].toLowerCase()])
					typeId = this.objects[matches[4].toLowerCase()];
				if (!typeId)
					return;

				/*default lang is english but since the url code for that is an empty string for that we just use 'en',
				since we can't' have an empty string property to save the english tooltips*/
				if (matches[3])
					matches[3] = matches[3].substr(0,2);
				else
					matches[3] = 'en';

				if (matches[5])
					return {Net: this.Id, Type: typeId, Id: matches[5], Lang: matches[3], Server: matches[1], NetStr: network};
			}
			return;
		},
		objects: {item:1}
	},

	/*wow:
	{
		Id: 11,
		Url: 'wow.yg.com',
		__Data: { Tooltip: []},
		__matchToYg: function(url, network) {return YG.Network.__matchToYg(url, network);},
		objects: {item:1, itemset:2, spell:3, achievement:4}
	},*/

	/**
	 * NOTE:
	 * ffxiv needs to be before ffxi, so when checking on what network we are,
	 * we check first for ffxiv and then for ffxi, thus preventing ffxi links to be matched by
	 * ffxiv network - see YG.Common.__matchToYg function
	 * See also: YG.Network.List for the ordering of networks
	 */
	ffxiv:
	{
		Id: 14,
		Url: 'ffxiv.yg.com',
		__Data: { Tooltip: [], Maps:[]},
		__matchToYg: function(url, network) {return YG.Network.__matchToYg(url, network);},
		objects: {item:1, guildleve:5, skill:3, quest:2, npc:7, recipe:8}
	},

	ffxi:
	{
		Id: 13,
		Url: 'ffxi.yg.com',
		__Data: { Tooltip: []},
		__matchToYg: function(url, network) {return YG.Network.__matchToYg(url, network);},
		objects: {item:1}
	},

	__matchToYg: function(url, network)
	{
		var regExp = /http:\/\/(([a-z]+)\d*\.yg\.com)\/([a-z]{2}\/)?(\w+)\/.+?\?(id=([0-9.]+)|(.*?id=\d+.*?))$/i;
		var matches = url.match(regExp);
		var typeId;

		/*lowercase network key as string*/
		network = network.toLowerCase();
		if (matches && (matches.length > 4) && matches[2] && (matches[2].indexOf(network) >= 0))
		{
			if (!this[network])
				return;

			/*if (this[network].objExclusions)
				for (var i = 0; i < this[network].objExclusions.length; i++)
					if (this[network].objExclusions[i] == matches[4])
						return;*/

			/*check the object type against the network objects*/
			if (matches[4] && this[network].objects[matches[4].toLowerCase()])
				typeId = this[network].objects[matches[4].toLowerCase()];
			if (!typeId)
				return;

			/*default lang is english but since the url code for that is an empty string for that we just use 'en',
			since we can't' have an empty string property to save the enghish tooltips*/
			if (matches[3])
				matches[3] = matches[3].substr(0,2);
			else
				matches[3] = 'en';

			var server = '';
			if (matches[1].toLowerCase() == this[network].Url.toLowerCase())
				server = 'static.ygcdn.com/' + network;
			else
				server = matches[1].replace(network, 'static') + '/' + network;

			if (matches[6])
				return {Net: this[network].Id, Type: typeId, Id: matches[6], Lang: matches[3], Server: server, NetStr: network};
			else
			{
				if (matches[5])
				{
					var getObj = YG.Common.GetUrlParams('?' + matches[5]);
					if (getObj && getObj.id && getObj.sid)
					{
						/*pad 0 to server id until 3 digits and concatenate player with server*/
						var newId = getObj.id.toString() + YG.Common.OPading(getObj.sid, 3);
						return {Net: this[network].Id, Type: typeId, Id: parseInt(newId, 10), Lang: matches[3], Server: matches[1], NetStr: network};
					}
				}
			}
		}
		return;
	},

	/*back linking the ids to the actual objects*/
	List: {12:'aion', 14:'ffxiv', 13:'ffxi', 10:'aoc', 11:'wow'},
	Languages: {en:1, de:1, fr:1, es:1, pl:1, ru:1, ko:1, ja:1},

	/*populates global tooltip data with data received from the server*/
	RegisterData: function(net, type, id, lang, content)
	{
		var data = this.GetTooltipDataObj(net);
		if (!data)
			return;

		if (!data[type])
			data[type] = {};
		if (!data[type][id])
			data[type][id] = {};
		if (!data[type][id][lang])
			data[type][id][lang] = {};
		data[type][id][lang] = content;

		/*data is here, show the tooltip based on the last known mouse position*/
		var obj = YG.Tooltip;
		var tooltipObj = document.getElementById(obj.Id);

		/*we check too see if the same request in still in progress*/
		if (!(obj.LastRequest && (obj.LastRequest.Net == net) && (obj.LastRequest.Lang == lang) && (obj.LastRequest.Type == type) && (obj.LastRequest.Id == id)))
			return;

		/*we show new content only if tooltip is still visible, if not, then the user moved away*/
		if (tooltipObj && (tooltipObj.style.visibility == 'visible'))
			obj.ShowTooltip(obj.Pos.X, obj.Pos.Y, content.tooltip);
	},

	GetTooltipDataObj: function(netId)
	{
		if (this[this.List[netId]] && this[this.List[netId]].__Data.Tooltip)
			return this[this.List[netId]].__Data.Tooltip;
		else
			return;
	}
};

if (!YG.Common)
{

YG.Common =
{
	getHeaderDOM: function()
	{
		/*get header element*/
		var headerArr = document.getElementsByTagName("head");
		if (headerArr.length && headerArr[0])
			return headerArr[0];
	},

	ajaxishRequest: function(url)
	{
		var header = this.getHeaderDOM();
		if (!header)
			return;

		/*place script request inside header so we get the js data from the server*/
		var request = document.createElement("SCRIPT");
		/*add the version value to the request just in case we don't already send something on the GET*/
		if ((url.indexOf('?') == -1) && YG.Versioning)
			request.src = url + '?' + YG.Versioning;
		else
			request.src = url;
		request.type = 'text/javascript';
		header.appendChild(request);
	},

	matchToYg: function(url)
	{
		if (url.indexOf('yg.com') < 0)
			return;

		var info = YG.Network, i;
		if (!info)
			return;

		var regExp = new RegExp('', 'g');
		matchLoop:
		for (var i in info.List)
		{
			/*check to see to what network inside YG this url belongs*/
			//if (url.indexOf(info.List[i].toLowerCase()) < 0)
			regExp.compile(info.List[i].toLowerCase() + '(\\d{2})?\\.');
			if (url.search(regExp) < 0)
				continue matchLoop;

			if (!YG.Network[info.List[i]])
				continue matchLoop;

			/*call the custom url matching function for this particulat network, send in the network key also with the url*/
			if (typeof YG.Network[info.List[i]].__matchToYg == 'function')
				return YG.Network[info.List[i]].__matchToYg(url, info.List[i]);
			else
				return;
		}

		return;
	},

	getText: function(node)
	{
		if (!node)
			return;

		if (document.body.textContent)
			text = node.textContent;
		else if (document.body.innerText)
			text = node.innerText;
		else
			text = node.innerHTML;

		if (text)
			text = YG.Common.trim(text);
		return text ? text : null;
	},

	getFullText: function(node)
	{
		if (!node)
			return;

		if (document.body.textContent)
			text = node.textContent;
		else if (document.body.innerText)
			text = node.innerText;
		else
			text = node.innerHTML;

		return text ? text : null;
	},

	trim: function(text)
	{
		var text = this.trimLeft(text);
		text = this.trimRight(text);
		return text;
	},

	trimLeft: function(text)
	{
		if (text)
			return text.toString().replace(/^\s+/, '');
	},

	trimRight: function(text)
	{
		if (text)
			return text.toString().replace(/\s+$/, '');
	},

	extend: function(destination, source)
	{
		for (var property in source)
		{
			if (destination[property] && (typeof(destination[property]) == 'object')
					&& (destination[property].toString() == '[object Object]') && source[property])
				this.extend(destination[property], source[property]);
			else
				destination[property] = source[property];
		}
		return destination;
	},

	OPading: function(number, digits)
	{
		var str = number.toString()
		while (str.length < digits)
			str = '0' + str;

		return str;
	},

	GetUrlParams: function(url)
	{
		var regEx = /\?(.+)$/i;
		var regEx1 = /(.+)?=(.+)?($|&)/i;
		var matches = url.match(regEx);
		var params = '';

		if (matches && matches[1])
		{
			var query = matches[1];
			var arr = query.split('&');

			if (arr.length > 0)
			{
				for (var i = 0; i < arr.length; i++)
				{
					matches = arr[i].toString().match(regEx1);

					if (matches && matches[1] && matches[2])
						params = params + matches[1].toString() + ': \'' + matches[2].toString() + '\', ';
				}

				params = params.substr(0, params.length - 2);
				eval( 'var obj = {' + params + '};');

				return obj;
			}
		}

		return null;
	},

	Browser:
	{
		Unknown: -1,
		IE: 0,
		FF: 1,
		Opera: 2,
		Chrome: 3,
		Safari: 4,
		IE8: 5,
		Check: function()
		{
			var reg = /firefox/i;

			if (navigator.userAgent.search(reg) >= 0)
				return YG.Common.Browser.FF;
			else
			{
				reg = /msie/i;

				if (navigator.userAgent.search(reg) >= 0)
				{
					reg = /msie 8.0/i;
					if (navigator.userAgent.search(reg) >= 0)
						return YG.Common.Browser.IE8;
					else
						return YG.Common.Browser.IE;
				}
				else
				{
					reg = /opera/i;

					if (navigator.userAgent.search(reg) >= 0)
						return YG.Common.Browser.Opera;
					else
					{
						reg = /chrome/i;

						if (navigator.userAgent.search(reg) >= 0)
							return YG.Common.Browser.Chrome;
						else
						{
							reg = /safari/i;

							if (navigator.userAgent.search(reg) >= 0)
								return YG.Common.Browser.Safari;
							else
								return YG.Common.Browser.Unknown;
						}
					}
				}
			}
		}
	}
};

};


YG.Syndication =
{
	Delay: 300,
	//ContainerCss: 'yg_link_container',
	ProcessingLinks: [],
	Requests: {},
	AnchorsProcessingInProgress: false,
	Settings:
	{
		processedClass: 'yg-xdone',
		maxGet: 1024
	},
	UserSettings:
	{
		excludedClass: 'yg-not',
		removeClasses: null,
		defaultClass: null,
		clearTitle: false
	},

	Init: function()
	{
		var linkType, addName; // anchors = document.links
		var header = YG.Common.getHeaderDOM();
		if (header)
		{
			var request = document.createElement('LINK');
			request.rel = 'stylesheet';
			request.type = 'text/css';
			request.media = 'all';
			request.href = 'http://static.ygcdn.com/css/ex_tooltip.css'+ (YG.Versioning ? '?' + YG.Versioning : '');
			header.appendChild(request);
		}

		if (YG.UserSettings && YG.UserSettings.Syndication)
			YG.Common.extend(this.UserSettings, YG.UserSettings.Syndication);

		if (this.UserSettings.removeClasses && this.UserSettings.removeClasses.length)
		{
			//construct an object for the remove classes property for faster checking of those class names
			this.Settings.removeClassesObj = {};
			for (var i = 0; i < this.UserSettings.removeClasses.length; i++)
				this.Settings.removeClassesObj[this.UserSettings.removeClasses[i]] = 1;
		}
		
		// process links in the document
		this.Regen();
	},
	
	/**
	 * Parses the document for newly added anchors and process them as YG links to add tooltips
	 */
	Regen: function()
	{
		// fetch all the anchors
		var anchors = document.links;
		// re-initialize so we can fill it up and requests
		if(typeof this.Requests == "undefined")
			this.Requests = {};

		this.AnchorsProcessingInProgress = true;
		linksLoop:
		for (var i = 0; i < anchors.length; i++)
		{
			if (anchors[i].href)
			{
				var objInfo = YG.Common.matchToYg(anchors[i].href);
				if (objInfo && objInfo.Net)
				{
					// only process newly added links which NeedsProcessing
					if (!this.NeedsProcessing(anchors[i]))
						continue linksLoop;

					/*check if this is one of the support network languages*/
					if (!YG.Network.Languages[objInfo.Lang])
						continue;

					var data = YG.Network.GetTooltipDataObj(objInfo.Net);
					// no data yet for this links place the ajaxish call
					if (!(data && data[objInfo.Type] && data[objInfo.Type][objInfo.Id] && data[objInfo.Type][objInfo.Id][objInfo.Lang]))
					{
						/*default lang is english but since the url code for that is an empty string for that we just use 'en',
						so switch back to empty string when doing the request*/
						var key = objInfo.Type + '_' + objInfo.Id + ((objInfo.Lang != 'en') ? '_' + objInfo.Lang : '');
						if (!this.Requests[objInfo.Net])
							this.Requests[objInfo.Net] = { server: 'http://' + objInfo.Server + '/syndication/synd_req_ajx.js?', request: []};

						var requestObj = this.Requests[objInfo.Net];
						if (!requestObj[key])
						{
							//requestObj[key] = {nodes:[]};
							var l = requestObj.request.length;
							if (!l)
							{
								requestObj.request[0] = '';
								l = 1;
							}

							if (requestObj.request[l-1].length && ((requestObj.request[l-1] + key + '&').toString().length > this.Settings.maxGet))
								requestObj.request[l] = key + '&';
							else
								requestObj.request[l-1] += key + '&';
						}
					}

					this.ProcessingLinks[this.ProcessingLinks.length] = anchors[i];
				}
			}
		}
		this.AnchorsProcessingInProgress = false;

		if (this.ProcessingLinks.length > 0)
		{
			/*send data request for tooltip info*/
			for (var i in this.Requests)
				if (this.Requests[i].request)
				{
					for (var j=0; j<this.Requests[i].request.length; j++)
						YG.Common.ajaxishRequest(this.Requests[i].server + this.Requests[i].request[j]);
				}

			delete this.Requests;
			/*set in motion processing the links that we care about*/
			this.__timer = setTimeout(function() { YG.Syndication.SetAutostyles();}, this.Delay);
		}
	},

	NeedsProcessing: function(anchor)
	{
		if (anchor.className && ((anchor.className.indexOf(this.Settings.processedClass) >= 0) || (anchor.className.indexOf(this.Settings.exludedClass) >= 0)))
			return false;
		else
			return true;
	},

	SetAutostyles: function()
	{
		if(this.AnchorsProcessingInProgress)
		{
			this.__timer = setTimeout(function() { YG.Syndication.SetAutostyles();}, this.Delay);
			return;
		}

		var newLinks = [], a;

		linksLoop:
		for (var i = 0; i < this.ProcessingLinks.length; i++)
		{
			a = this.ProcessingLinks[i];
			if (a.href)
			{
				var objInfo = YG.Common.matchToYg(a.href);
				if (objInfo && objInfo.Net)
				{
					var data = YG.Network.GetTooltipDataObj(objInfo.Net);
					if (data && data[objInfo.Type] && data[objInfo.Type][objInfo.Id] && data[objInfo.Type][objInfo.Id][objInfo.Lang])
					{
						if (this.__applyAutostyles(a, data[objInfo.Type][objInfo.Id][objInfo.Lang], YG.Network.List[objInfo.Net]))
							continue linksLoop;
					}
				}
			}

			newLinks[newLinks.length] = a;
		}

		this.ProcessingLinks = [];
		if (newLinks.length)
		{
			this.ProcessingLinks = newLinks;
			this.__timer = setTimeout(function() {YG.Syndication.SetAutostyles();}, this.Delay);
		}
		else
			clearTimeout(this.__timer);
	},

	__timer: 0,
	__applyAutostyles: function(anchorObj, objContent, network)
	{
		if (!objContent || !objContent.tooltip)
			return false;

		var className = anchorObj.className;
		/*if no class name apply the default class if any*/
		if (this.UserSettings.defaultClass && (!className || (className.indexOf('yg-') < 0)))
		{
			className = (className ? className + ' ' : '') + this.UserSettings.defaultClass;
			anchorObj.className = className;
		}

		if (!className)
		{
			if (objContent.color)
				anchorObj.className = anchorObj.className + ' ' + objContent.color;

			anchorObj.onmouseover = function(event){ YG.Tooltip.InitTooltip(event);};
			return true;
		}

		/*Class name options: none, full-small, full-medium, full-large, icon-small, icon-medium, icon-large, text, text-color.
		An optional class attribute "name" can be added to so we can do name replacements inside the link ex:full-small name*/
		var linkType = className.split(' ');
		if (linkType.length)
		{
			var doName = false, doNoColor = false, doIcon = false, doNoText = false, iconType, iconClass = '';
			var newClassName = '';
			classNamesLoop:
			for (var i = 0; i < linkType.length; i++)
			{
				switch (linkType[i].toLowerCase())
				{
					case 'yg-nocolor':
							doNoColor = true;
							break;

					case 'yg-notext':
							doNoText = true;
							break;

					case 'yg-name':
							doName = true;
							break;

					case 'yg-iconsmall':
							doIcon = true;
							iconType = 'small';
							iconClass = 'yg-iconsmall';
							break;

					case 'yg-iconmedium':
							doIcon = true;
							iconType = 'medium';
							iconClass = 'yg-iconmedium';
							break;

					case 'yg-iconlarge':
							doIcon = true;
							iconType = 'large';
							iconClass = 'yg-iconlarge';
							break;
				}

				/*skip adding this className to the anchor if it needs to get removed*/
				if (this.Settings.removeClassesObj)
					if (this.Settings.removeClassesObj[linkType[i].toLowerCase()])
							continue classNamesLoop;

				newClassName += linkType[i] + ' ';
			}

			/*add the new composed className*/
			anchorObj.className = newClassName;
			/*change the title to the object name if set*/
			if (this.UserSettings.clearTitle)
				anchorObj.title = '';

			if (!doNoColor && objContent.color)
				anchorObj.className = anchorObj.className + ' ' + objContent.color;

			if (doName && objContent.name)
				anchorObj.innerHTML = objContent.name;

			if (doIcon)
			{
				if (objContent.icon && objContent.icon[iconType])
				{
					if (doNoText)
						anchorObj.innerHTML = '';

					var parent = anchorObj.parentNode;
					var newElement = document.createElement('DIV');
					newElement.style.display = 'inline';

					parent.replaceChild(newElement, anchorObj);
					var copyOfAAgain = anchorObj.cloneNode(true);
					copyOfAAgain.innerHTML = '';
					copyOfAAgain.className = iconClass + ' ' + this.Settings.processedClass + (network ? ' yg-' + network : '');
					copyOfAAgain.style.backgroundImage = 'url(' + objContent.icon[iconType] + ')';
					copyOfAAgain.onmouseover = function(event){ YG.Tooltip.InitTooltip(event);};

					anchorObj.className = anchorObj.className.replace(iconClass, '');
					newElement.appendChild(copyOfAAgain);
					newElement.appendChild(anchorObj);
				}
				else
					anchorObj.className = anchorObj.className + ' yg-iconnotfound';
			}

			anchorObj.onmouseover = function(event){ YG.Tooltip.InitTooltip(event);};
			anchorObj.className = anchorObj.className + ' ' + this.Settings.processedClass;
		}

		return true;
	},

	__addEvent: function (elm, evType, fn, useCapture)
	{
		if (elm.addEventListener)
		{
			elm.addEventListener(evType, fn, useCapture);
			return true;
		}
		else if (elm.attachEvent)
		{
			var r = elm.attachEvent('on' + evType, fn);	return r;
		}
		else
		{
			elm['on' + evType] = fn;
		}
	}
}


/* syndication stuff - checking all the links inside the page and replacing those as needed*/
YG.Syndication.__addEvent(window, 'load', function() {YG.Syndication.Init();}, false);
