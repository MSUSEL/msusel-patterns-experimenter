/*
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
/*
	Copyright (c) 2004-2005, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.widget.ResizeHandle");
dojo.provide("dojo.widget.html.ResizeHandle");

dojo.require("dojo.widget.*");
dojo.require("dojo.html");
dojo.require("dojo.style");
dojo.require("dojo.dom");
dojo.require("dojo.event");

dojo.widget.html.ResizeHandle = function(){
	dojo.widget.HtmlWidget.call(this);
}

dojo.inherits(dojo.widget.html.ResizeHandle, dojo.widget.HtmlWidget);

dojo.lang.extend(dojo.widget.html.ResizeHandle, {
	widgetType: "ResizeHandle",

	isSizing: false,
	startPoint: null,
	startSize: null,

	grabImg: null,

	targetElmId: '',
	imgSrc: dojo.uri.dojoUri("src/widget/templates/grabCorner.gif"),

	templateCssPath: dojo.uri.dojoUri("src/widget/templates/HtmlResizeHandle.css"),
	templateString: '<div dojoAttachPoint="domNode"><img dojoAttachPoint="grabImg" /></div>',

	fillInTemplate: function(){

		dojo.style.insertCssFile(this.templateCssPath);

		dojo.html.addClass(this.domNode, 'dojoHtmlResizeHandle');
		dojo.html.addClass(this.grabImg, 'dojoHtmlResizeHandleImage');

		this.grabImg.src = this.imgSrc;
	},

	postCreate: function(){
		dojo.event.connect(this.domNode, "onmousedown", this, "beginSizing");
	},

	beginSizing: function(e){
		if (this.isSizing){ return false; }

		this.targetElm = dojo.widget.byId(this.targetElmId);
		if (!this.targetElm){ return; }

		var screenX = window.event ? window.event.clientX : e.pageX;
		var screenY = window.event ? window.event.clientY : e.pageY;

		this.isSizing = true;
		this.startPoint  = {'x':e.clientX, 'y':e.clientY};
		this.startSize  = {'w':dojo.style.getOuterWidth(this.targetElm.domNode), 'h':dojo.style.getOuterHeight(this.targetElm.domNode)};

		dojo.event.kwConnect({
			srcObj: document.body, 
			srcFunc: "onmousemove",
			targetObj: this,
			targetFunc: "changeSizing",
			rate: 25
		});
		dojo.event.connect(document.body, "onmouseup", this, "endSizing");

		e.preventDefault();
	},

	changeSizing: function(e){
		// On IE, if you move the mouse above/to the left of the object being resized,
		// sometimes clientX/Y aren't set, apparently.  Just ignore the event.
		if(!e.clientX  || !e.clientY){ return; }
		var dx = this.startPoint.x - e.clientX;
		var dy = this.startPoint.y - e.clientY;
		this.targetElm.resizeTo(this.startSize.w - dx, this.startSize.h - dy);

		e.preventDefault();
	},

	endSizing: function(e){
		dojo.event.disconnect(document.body, "onmousemove", this, "changeSizing");
		dojo.event.disconnect(document.body, "onmouseup", this, "endSizing");

		this.isSizing = false;
	}


});

dojo.widget.tags.addParseTreeHandler("dojo:ResizeHandle");
