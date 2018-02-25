/*
	Paginator_Asmins 3000
	- idea by ecto (ecto.ru)
	- coded by karaboz (karaboz.ru)

	How to implement:
	<div class="Paginator_Asmins" id="Paginator_Asmins_example"></div>
	<script type="text/javascript">
		Paginator_Asmins_example = new Paginator_Asmins('Paginator_Asmins_example', 2048, 10, 1, 'http://www.yourwebsite.com/pages/');
	</script>

	Be sure that width of your Paginator_Asmins does not change after page is loaded
	If it happens you must call Paginator_Asmins.resizePaginator_Asmins(Paginator_Asmins_example) function to redraw Paginator_Asmins

*/

/*
	Paginator_Asmins class
		Paginator_AsminsHolderId - id of the html element where Paginator_Asmins will be placed as innerHTML (String): required
		pagesTotal - number of pages (Number, required)
		pagesSpan - number of pages which are visible at once (Number, required) 
		pageCurrent - the number of current page (Number, required)
		baseUrl - the url of the website (String)
			if baseUrl is 'http://www.yourwebsite.com/pages/' the links on the pages will be:
			http://www.yourwebsite.com/pages/1, http://www.yourwebsite.com/pages/2,	etc
*/
var Paginator_Asmins = function(Paginator_AsminsHolderId, pagesTotal, pagesSpan, pageCurrent, baseUrl){
	if(!document.getElementById(Paginator_AsminsHolderId) || !pagesTotal || !pagesSpan) return false;

	this.inputData = {
		Paginator_AsminsHolderId: Paginator_AsminsHolderId,
		pagesTotal: pagesTotal,
		pagesSpan: pagesSpan < pagesTotal ? pagesSpan : pagesTotal,
		pageCurrent: pageCurrent,
		baseUrl: baseUrl ? baseUrl : 'admin.php?mod=accounts&page='
	};

	this.html = {
		holder: null,

		table: null,
		trPages: null, 
		trScrollBar: null,
		tdsPages: null,

		scrollBar: null,
		scrollThumb: null,
			
		pageCurrentMark: null
	};


	this.prepareHtml();

	this.initScrollThumb();
	this.initPageCurrentMark();
	this.initEvents();

	this.scrollToPageCurrent();
} 

/*
	Set all .html properties (links to dom objects)
*/
Paginator_Asmins.prototype.prepareHtml = function(){

	this.html.holder = document.getElementById(this.inputData.Paginator_AsminsHolderId);
	this.html.holder.innerHTML = this.makePagesTableHtml();

	this.html.table = this.html.holder.getElementsByTagName('table')[0];

	var trPages = this.html.table.getElementsByTagName('tr')[0]; 
	this.html.tdsPages = trPages.getElementsByTagName('td');

	this.html.scrollBar = getElementsByClassName(this.html.table, 'div', 'scroll_bar')[0];
	this.html.scrollThumb = getElementsByClassName(this.html.table, 'div', 'scroll_thumb')[0];
	this.html.pageCurrentMark = getElementsByClassName(this.html.table, 'div', 'current_page_mark')[0];

	// hide scrollThumb if there is no scroll (we see all pages at once)
	if(this.inputData.pagesSpan == this.inputData.pagesTotal){
		addClass(this.html.holder, 'fullsize');
	}
}

/*
	Make html for pages (table) 
*/
Paginator_Asmins.prototype.makePagesTableHtml = function(){
	var tdWidth = (100 / this.inputData.pagesSpan) + '%';

	var html = '' +
	'<table width="100%">' +
		'<tr>' 
			for (var i=1; i<=this.inputData.pagesSpan; i++){
				html += '<td width="' + tdWidth + '"></td>';
			}
			html += '' + 
		'</tr>' +
		'<tr>' +
			'<td colspan="' + this.inputData.pagesSpan + '">' +
				'<div class="scroll_bar">' + 
					'<div class="scroll_trough"></div>' + 
					'<div class="scroll_thumb">' + 
						'<div class="scroll_knob"></div>' + 
					'</div>' + 
					'<div class="current_page_mark"></div>' + 
				'</div>' +
			'</td>' +
		'</tr>' +
	'</table>';

	return html;
}

/*
	Set all needed properties for scrollThumb and it's width
*/
Paginator_Asmins.prototype.initScrollThumb = function(){
	this.html.scrollThumb.widthMin = '8'; // minimum width of the scrollThumb (px)
	this.html.scrollThumb.widthPercent = this.inputData.pagesSpan/this.inputData.pagesTotal * 100;

	this.html.scrollThumb.xPosPageCurrent = (this.inputData.pageCurrent - Math.round(this.inputData.pagesSpan/2))/this.inputData.pagesTotal * this.html.table.offsetWidth;
	this.html.scrollThumb.xPos = this.html.scrollThumb.xPosPageCurrent;

	this.html.scrollThumb.xPosMin = 0;
	this.html.scrollThumb.xPosMax;

	this.html.scrollThumb.widthActual;

	this.setScrollThumbWidth();
	
}

Paginator_Asmins.prototype.setScrollThumbWidth = function(){
	// Try to set width in percents
	this.html.scrollThumb.style.width = this.html.scrollThumb.widthPercent + "%";

	// Fix the actual width in px
	this.html.scrollThumb.widthActual = this.html.scrollThumb.offsetWidth;

	// If actual width less then minimum which we set
	if(this.html.scrollThumb.widthActual < this.html.scrollThumb.widthMin){
		this.html.scrollThumb.style.width = this.html.scrollThumb.widthMin + 'px';
	}

	this.html.scrollThumb.xPosMax = this.html.table.offsetWidth - this.html.scrollThumb.widthActual;
}

Paginator_Asmins.prototype.moveScrollThumb = function(){
	this.html.scrollThumb.style.left = this.html.scrollThumb.xPos + "px";
}


/*
	Set all needed properties for pageCurrentMark, it's width and move it
*/
Paginator_Asmins.prototype.initPageCurrentMark = function(){
	this.html.pageCurrentMark.widthMin = '3';
	this.html.pageCurrentMark.widthPercent = 100 / this.inputData.pagesTotal;
	this.html.pageCurrentMark.widthActual;

	this.setPageCurrentPointWidth();
	this.movePageCurrentPoint();
}

Paginator_Asmins.prototype.setPageCurrentPointWidth = function(){
	// Try to set width in percents
	this.html.pageCurrentMark.style.width = this.html.pageCurrentMark.widthPercent + '%';

	// Fix the actual width in px
	this.html.pageCurrentMark.widthActual = this.html.pageCurrentMark.offsetWidth;

	// If actual width less then minimum which we set
	if(this.html.pageCurrentMark.widthActual < this.html.pageCurrentMark.widthMin){
		this.html.pageCurrentMark.style.width = this.html.pageCurrentMark.widthMin + 'px';
	}
}

Paginator_Asmins.prototype.movePageCurrentPoint = function(){
	if(this.html.pageCurrentMark.widthActual < this.html.pageCurrentMark.offsetWidth){
		this.html.pageCurrentMark.style.left = (this.inputData.pageCurrent - 1)/this.inputData.pagesTotal * this.html.table.offsetWidth - this.html.pageCurrentMark.offsetWidth/2 + "px";
	} else {
		this.html.pageCurrentMark.style.left = (this.inputData.pageCurrent - 1)/this.inputData.pagesTotal * this.html.table.offsetWidth + "px";
	}
}



/*
	Drag, click and resize events
*/
Paginator_Asmins.prototype.initEvents = function(){
	var _this = this;

	this.html.scrollThumb.onmousedown = function(e){
		if (!e) var e = window.event;
		e.cancelBubble = true;
		if (e.stopPropagation) e.stopPropagation();

		var dx = getMousePosition(e).x - this.xPos;
		document.onmousemove = function(e){
			if (!e) var e = window.event;
			_this.html.scrollThumb.xPos = getMousePosition(e).x - dx;

			// the first: draw pages, the second: move scrollThumb (it was logically but ie sucks!)
			_this.moveScrollThumb();
			_this.drawPages();
			
			
		}
		document.onmouseup = function(){
			document.onmousemove = null;
			_this.enableSelection();
		}
		_this.disableSelection();
	}

	this.html.scrollBar.onmousedown = function(e){
		if (!e) var e = window.event;
		if(matchClass(_this.Paginator_AsminsBox, 'fullsize')) return;
		
		_this.html.scrollThumb.xPos = getMousePosition(e).x - getPageX(_this.html.scrollBar) - _this.html.scrollThumb.offsetWidth/2;
		
		_this.moveScrollThumb();
		_this.drawPages();
		
		
	}

	// Comment the row beneath if you set Paginator_Asmins width fixed
	addEvent(window, 'resize', function(){Paginator_Asmins.resizePaginator_Asmins(_this)});
}

/*
	Redraw current span of pages
*/
Paginator_Asmins.prototype.drawPages = function(){
	var percentFromLeft = this.html.scrollThumb.xPos/(this.html.table.offsetWidth);
	var cellFirstValue = Math.round(percentFromLeft * this.inputData.pagesTotal);
	
	var html = "";
	// drawing pages control the position of the scrollThumb on the edges!
	if(cellFirstValue < 1){
		cellFirstValue = 1;
		this.html.scrollThumb.xPos = 0;
		this.moveScrollThumb();
	} else if(cellFirstValue >= this.inputData.pagesTotal - this.inputData.pagesSpan) {
		cellFirstValue = this.inputData.pagesTotal - this.inputData.pagesSpan + 1;
		this.html.scrollThumb.xPos = this.html.table.offsetWidth - this.html.scrollThumb.offsetWidth;
		this.moveScrollThumb();
	}

	

	for(var i=0; i<this.html.tdsPages.length; i++){
		var cellCurrentValue = cellFirstValue + i;
		if(cellCurrentValue == this.inputData.pageCurrent){
			html = "<span>" + "<strong>" + cellCurrentValue + "</strong>" + "</span>";
		} else {
			html = "<span>" + "<a href='" + this.inputData.baseUrl + cellCurrentValue + "'>" + cellCurrentValue + "</a>" + "</span>";
		}
		this.html.tdsPages[i].innerHTML = html;
	}
}

/*
	Scroll to current page
*/
Paginator_Asmins.prototype.scrollToPageCurrent = function(){
	this.html.scrollThumb.xPosPageCurrent = (this.inputData.pageCurrent - Math.round(this.inputData.pagesSpan/2))/this.inputData.pagesTotal * this.html.table.offsetWidth;
	this.html.scrollThumb.xPos = this.html.scrollThumb.xPosPageCurrent;
	
	this.moveScrollThumb();
	this.drawPages();
	
}



Paginator_Asmins.prototype.disableSelection = function(){
	document.onselectstart = function(){
		return false;
	}
	this.html.scrollThumb.focus();	
}

Paginator_Asmins.prototype.enableSelection = function(){
	document.onselectstart = function(){
		return true;
	}
}

/*
	Function is used when Paginator_Asmins was resized (window.onresize fires it automatically)
	Use it when you change Paginator_Asmins with DHTML
	Do not use it if you set fixed width of Paginator_Asmins
*/
Paginator_Asmins.resizePaginator_Asmins = function (Paginator_AsminsObj){

	Paginator_AsminsObj.setPageCurrentPointWidth();
	Paginator_AsminsObj.movePageCurrentPoint();

	Paginator_AsminsObj.setScrollThumbWidth();
	Paginator_AsminsObj.scrollToPageCurrent();
}




/*
	Global functions which are used
*/
function getElementsByClassName(objParentNode, strNodeName, strClassName){
	var nodes = objParentNode.getElementsByTagName(strNodeName);
	if(!strClassName){
		return nodes;	
	}
	var nodesWithClassName = [];
	for(var i=0; i<nodes.length; i++){
		if(matchClass( nodes[i], strClassName )){
			nodesWithClassName[nodesWithClassName.length] = nodes[i];
		}	
	}
	return nodesWithClassName;
}


function addClass( objNode, strNewClass ) {
	replaceClass( objNode, strNewClass, '' );
}

function removeClass( objNode, strCurrClass ) {
	replaceClass( objNode, '', strCurrClass );
}

function replaceClass( objNode, strNewClass, strCurrClass ) {
	var strOldClass = strNewClass;
	if ( strCurrClass && strCurrClass.length ){
		strCurrClass = strCurrClass.replace( /\s+(\S)/g, '|$1' );
		if ( strOldClass.length ) strOldClass += '|';
		strOldClass += strCurrClass;
	}
	objNode.className = objNode.className.replace( new RegExp('(^|\\s+)(' + strOldClass + ')($|\\s+)', 'g'), '$1' );
	objNode.className += ( (objNode.className.length)? ' ' : '' ) + strNewClass;
}

function matchClass( objNode, strCurrClass ) {
	return ( objNode && objNode.className.length && objNode.className.match( new RegExp('(^|\\s+)(' + strCurrClass + ')($|\\s+)') ) );
}


function addEvent(objElement, strEventType, ptrEventFunc) {
	if (objElement.addEventListener)
		objElement.addEventListener(strEventType, ptrEventFunc, false);
	else if (objElement.attachEvent)
		objElement.attachEvent('on' + strEventType, ptrEventFunc);
}
function removeEvent(objElement, strEventType, ptrEventFunc) {
	if (objElement.removeEventListener) objElement.removeEventListener(strEventType, ptrEventFunc, false);
		else if (objElement.detachEvent) objElement.detachEvent('on' + strEventType, ptrEventFunc);
}


function getPageY( oElement ) {
	var iPosY = oElement.offsetTop;
	while ( oElement.offsetParent != null ) {
		oElement = oElement.offsetParent;
		iPosY += oElement.offsetTop;
		if (oElement.tagName == 'BODY') break;
	}
	return iPosY;
}

function getPageX( oElement ) {
	var iPosX = oElement.offsetLeft;
	while ( oElement.offsetParent != null ) {
		oElement = oElement.offsetParent;
		iPosX += oElement.offsetLeft;
		if (oElement.tagName == 'BODY') break;
	}
	return iPosX;
}

function getMousePosition(e) {
	if (e.pageX || e.pageY){
		var posX = e.pageX;
		var posY = e.pageY;
	}else if (e.clientX || e.clientY) 	{
		var posX = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
		var posY = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
	}
	return {x:posX, y:posY}	
}




var Paginator_Legion = function(Paginator_LegionHolderId, pagesTotal, pagesSpan, pageCurrent, baseUrl){
	if(!document.getElementById(Paginator_LegionHolderId) || !pagesTotal || !pagesSpan) return false;

	this.inputData = {
		Paginator_LegionHolderId: Paginator_LegionHolderId,
		pagesTotal: pagesTotal,
		pagesSpan: pagesSpan < pagesTotal ? pagesSpan : pagesTotal,
		pageCurrent: pageCurrent,
		baseUrl: baseUrl ? baseUrl : 'admin.php?mod=legion&page='
	};

	this.html = {
		holder: null,

		table: null,
		trPages: null, 
		trScrollBar: null,
		tdsPages: null,

		scrollBar: null,
		scrollThumb: null,
			
		pageCurrentMark: null
	};


	this.prepareHtml();

	this.initScrollThumb();
	this.initPageCurrentMark();
	this.initEvents();

	this.scrollToPageCurrent();
} 

/*
	Set all .html properties (links to dom objects)
*/
Paginator_Legion.prototype.prepareHtml = function(){

	this.html.holder = document.getElementById(this.inputData.Paginator_LegionHolderId);
	this.html.holder.innerHTML = this.makePagesTableHtml();

	this.html.table = this.html.holder.getElementsByTagName('table')[0];

	var trPages = this.html.table.getElementsByTagName('tr')[0]; 
	this.html.tdsPages = trPages.getElementsByTagName('td');

	this.html.scrollBar = getElementsByClassName(this.html.table, 'div', 'scroll_bar')[0];
	this.html.scrollThumb = getElementsByClassName(this.html.table, 'div', 'scroll_thumb')[0];
	this.html.pageCurrentMark = getElementsByClassName(this.html.table, 'div', 'current_page_mark')[0];

	// hide scrollThumb if there is no scroll (we see all pages at once)
	if(this.inputData.pagesSpan == this.inputData.pagesTotal){
		addClass(this.html.holder, 'fullsize');
	}
}

/*
	Make html for pages (table) 
*/
Paginator_Legion.prototype.makePagesTableHtml = function(){
	var tdWidth = (100 / this.inputData.pagesSpan) + '%';

	var html = '' +
	'<table width="100%">' +
		'<tr>' 
			for (var i=1; i<=this.inputData.pagesSpan; i++){
				html += '<td width="' + tdWidth + '"></td>';
			}
			html += '' + 
		'</tr>' +
		'<tr>' +
			'<td colspan="' + this.inputData.pagesSpan + '">' +
				'<div class="scroll_bar">' + 
					'<div class="scroll_trough"></div>' + 
					'<div class="scroll_thumb">' + 
						'<div class="scroll_knob"></div>' + 
					'</div>' + 
					'<div class="current_page_mark"></div>' + 
				'</div>' +
			'</td>' +
		'</tr>' +
	'</table>';

	return html;
}

/*
	Set all needed properties for scrollThumb and it's width
*/
Paginator_Legion.prototype.initScrollThumb = function(){
	this.html.scrollThumb.widthMin = '8'; // minimum width of the scrollThumb (px)
	this.html.scrollThumb.widthPercent = this.inputData.pagesSpan/this.inputData.pagesTotal * 100;

	this.html.scrollThumb.xPosPageCurrent = (this.inputData.pageCurrent - Math.round(this.inputData.pagesSpan/2))/this.inputData.pagesTotal * this.html.table.offsetWidth;
	this.html.scrollThumb.xPos = this.html.scrollThumb.xPosPageCurrent;

	this.html.scrollThumb.xPosMin = 0;
	this.html.scrollThumb.xPosMax;

	this.html.scrollThumb.widthActual;

	this.setScrollThumbWidth();
	
}

Paginator_Legion.prototype.setScrollThumbWidth = function(){
	// Try to set width in percents
	this.html.scrollThumb.style.width = this.html.scrollThumb.widthPercent + "%";

	// Fix the actual width in px
	this.html.scrollThumb.widthActual = this.html.scrollThumb.offsetWidth;

	// If actual width less then minimum which we set
	if(this.html.scrollThumb.widthActual < this.html.scrollThumb.widthMin){
		this.html.scrollThumb.style.width = this.html.scrollThumb.widthMin + 'px';
	}

	this.html.scrollThumb.xPosMax = this.html.table.offsetWidth - this.html.scrollThumb.widthActual;
}

Paginator_Legion.prototype.moveScrollThumb = function(){
	this.html.scrollThumb.style.left = this.html.scrollThumb.xPos + "px";
}


/*
	Set all needed properties for pageCurrentMark, it's width and move it
*/
Paginator_Legion.prototype.initPageCurrentMark = function(){
	this.html.pageCurrentMark.widthMin = '3';
	this.html.pageCurrentMark.widthPercent = 100 / this.inputData.pagesTotal;
	this.html.pageCurrentMark.widthActual;

	this.setPageCurrentPointWidth();
	this.movePageCurrentPoint();
}

Paginator_Legion.prototype.setPageCurrentPointWidth = function(){
	// Try to set width in percents
	this.html.pageCurrentMark.style.width = this.html.pageCurrentMark.widthPercent + '%';

	// Fix the actual width in px
	this.html.pageCurrentMark.widthActual = this.html.pageCurrentMark.offsetWidth;

	// If actual width less then minimum which we set
	if(this.html.pageCurrentMark.widthActual < this.html.pageCurrentMark.widthMin){
		this.html.pageCurrentMark.style.width = this.html.pageCurrentMark.widthMin + 'px';
	}
}

Paginator_Legion.prototype.movePageCurrentPoint = function(){
	if(this.html.pageCurrentMark.widthActual < this.html.pageCurrentMark.offsetWidth){
		this.html.pageCurrentMark.style.left = (this.inputData.pageCurrent - 1)/this.inputData.pagesTotal * this.html.table.offsetWidth - this.html.pageCurrentMark.offsetWidth/2 + "px";
	} else {
		this.html.pageCurrentMark.style.left = (this.inputData.pageCurrent - 1)/this.inputData.pagesTotal * this.html.table.offsetWidth + "px";
	}
}



/*
	Drag, click and resize events
*/
Paginator_Legion.prototype.initEvents = function(){
	var _this = this;

	this.html.scrollThumb.onmousedown = function(e){
		if (!e) var e = window.event;
		e.cancelBubble = true;
		if (e.stopPropagation) e.stopPropagation();

		var dx = getMousePosition(e).x - this.xPos;
		document.onmousemove = function(e){
			if (!e) var e = window.event;
			_this.html.scrollThumb.xPos = getMousePosition(e).x - dx;

			// the first: draw pages, the second: move scrollThumb (it was logically but ie sucks!)
			_this.moveScrollThumb();
			_this.drawPages();
			
			
		}
		document.onmouseup = function(){
			document.onmousemove = null;
			_this.enableSelection();
		}
		_this.disableSelection();
	}

	this.html.scrollBar.onmousedown = function(e){
		if (!e) var e = window.event;
		if(matchClass(_this.Paginator_LegionBox, 'fullsize')) return;
		
		_this.html.scrollThumb.xPos = getMousePosition(e).x - getPageX(_this.html.scrollBar) - _this.html.scrollThumb.offsetWidth/2;
		
		_this.moveScrollThumb();
		_this.drawPages();
		
		
	}

	// Comment the row beneath if you set Paginator_Legion width fixed
	addEvent(window, 'resize', function(){Paginator_Legion.resizePaginator_Legion(_this)});
}

/*
	Redraw current span of pages
*/
Paginator_Legion.prototype.drawPages = function(){
	var percentFromLeft = this.html.scrollThumb.xPos/(this.html.table.offsetWidth);
	var cellFirstValue = Math.round(percentFromLeft * this.inputData.pagesTotal);
	
	var html = "";
	// drawing pages control the position of the scrollThumb on the edges!
	if(cellFirstValue < 1){
		cellFirstValue = 1;
		this.html.scrollThumb.xPos = 0;
		this.moveScrollThumb();
	} else if(cellFirstValue >= this.inputData.pagesTotal - this.inputData.pagesSpan) {
		cellFirstValue = this.inputData.pagesTotal - this.inputData.pagesSpan + 1;
		this.html.scrollThumb.xPos = this.html.table.offsetWidth - this.html.scrollThumb.offsetWidth;
		this.moveScrollThumb();
	}

	

	for(var i=0; i<this.html.tdsPages.length; i++){
		var cellCurrentValue = cellFirstValue + i;
		if(cellCurrentValue == this.inputData.pageCurrent){
			html = "<span>" + "<strong>" + cellCurrentValue + "</strong>" + "</span>";
		} else {
			html = "<span>" + "<a href='" + this.inputData.baseUrl + cellCurrentValue + "'>" + cellCurrentValue + "</a>" + "</span>";
		}
		this.html.tdsPages[i].innerHTML = html;
	}
}

/*
	Scroll to current page
*/
Paginator_Legion.prototype.scrollToPageCurrent = function(){
	this.html.scrollThumb.xPosPageCurrent = (this.inputData.pageCurrent - Math.round(this.inputData.pagesSpan/2))/this.inputData.pagesTotal * this.html.table.offsetWidth;
	this.html.scrollThumb.xPos = this.html.scrollThumb.xPosPageCurrent;
	
	this.moveScrollThumb();
	this.drawPages();
	
}



Paginator_Legion.prototype.disableSelection = function(){
	document.onselectstart = function(){
		return false;
	}
	this.html.scrollThumb.focus();	
}

Paginator_Legion.prototype.enableSelection = function(){
	document.onselectstart = function(){
		return true;
	}
}

/*
	Function is used when Paginator_Legion was resized (window.onresize fires it automatically)
	Use it when you change Paginator_Legion with DHTML
	Do not use it if you set fixed width of Paginator_Legion
*/
Paginator_Legion.resizePaginator_Legion = function (Paginator_LegionObj){

	Paginator_LegionObj.setPageCurrentPointWidth();
	Paginator_LegionObj.movePageCurrentPoint();

	Paginator_LegionObj.setScrollThumbWidth();
	Paginator_LegionObj.scrollToPageCurrent();
}




/*
	Global functions which are used
*/
function getElementsByClassName(objParentNode, strNodeName, strClassName){
	var nodes = objParentNode.getElementsByTagName(strNodeName);
	if(!strClassName){
		return nodes;	
	}
	var nodesWithClassName = [];
	for(var i=0; i<nodes.length; i++){
		if(matchClass( nodes[i], strClassName )){
			nodesWithClassName[nodesWithClassName.length] = nodes[i];
		}	
	}
	return nodesWithClassName;
}


function addClass( objNode, strNewClass ) {
	replaceClass( objNode, strNewClass, '' );
}

function removeClass( objNode, strCurrClass ) {
	replaceClass( objNode, '', strCurrClass );
}

function replaceClass( objNode, strNewClass, strCurrClass ) {
	var strOldClass = strNewClass;
	if ( strCurrClass && strCurrClass.length ){
		strCurrClass = strCurrClass.replace( /\s+(\S)/g, '|$1' );
		if ( strOldClass.length ) strOldClass += '|';
		strOldClass += strCurrClass;
	}
	objNode.className = objNode.className.replace( new RegExp('(^|\\s+)(' + strOldClass + ')($|\\s+)', 'g'), '$1' );
	objNode.className += ( (objNode.className.length)? ' ' : '' ) + strNewClass;
}

function matchClass( objNode, strCurrClass ) {
	return ( objNode && objNode.className.length && objNode.className.match( new RegExp('(^|\\s+)(' + strCurrClass + ')($|\\s+)') ) );
}


function addEvent(objElement, strEventType, ptrEventFunc) {
	if (objElement.addEventListener)
		objElement.addEventListener(strEventType, ptrEventFunc, false);
	else if (objElement.attachEvent)
		objElement.attachEvent('on' + strEventType, ptrEventFunc);
}
function removeEvent(objElement, strEventType, ptrEventFunc) {
	if (objElement.removeEventListener) objElement.removeEventListener(strEventType, ptrEventFunc, false);
		else if (objElement.detachEvent) objElement.detachEvent('on' + strEventType, ptrEventFunc);
}


function getPageY( oElement ) {
	var iPosY = oElement.offsetTop;
	while ( oElement.offsetParent != null ) {
		oElement = oElement.offsetParent;
		iPosY += oElement.offsetTop;
		if (oElement.tagName == 'BODY') break;
	}
	return iPosY;
}

function getPageX( oElement ) {
	var iPosX = oElement.offsetLeft;
	while ( oElement.offsetParent != null ) {
		oElement = oElement.offsetParent;
		iPosX += oElement.offsetLeft;
		if (oElement.tagName == 'BODY') break;
	}
	return iPosX;
}

function getMousePosition(e) {
	if (e.pageX || e.pageY){
		var posX = e.pageX;
		var posY = e.pageY;
	}else if (e.clientX || e.clientY) 	{
		var posX = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
		var posY = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
	}
	return {x:posX, y:posY}	
}