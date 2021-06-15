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

dojo.provide("dojo.widget.html.Tooltip");
dojo.require("dojo.widget.html.ContentPane");
dojo.require("dojo.widget.Tooltip");
dojo.require("dojo.uri");
dojo.require("dojo.widget.*");
dojo.require("dojo.event");
dojo.require("dojo.style");
dojo.require("dojo.html");

dojo.widget.html.Tooltip = function(){
	// mix in the tooltip properties
	dojo.widget.Tooltip.call(this);
	dojo.widget.html.ContentPane.call(this);
}
dojo.inherits(dojo.widget.html.Tooltip, dojo.widget.html.ContentPane);
dojo.lang.extend(dojo.widget.html.Tooltip, {

	// Constructor arguments (should these be in tooltip.js rather than html/tooltip.js???)
	caption: "",
	delay: 500,
	connectId: "",

	templatePath: dojo.uri.dojoUri("src/widget/templates/HtmlTooltipTemplate.html"),
	templateCssPath: dojo.uri.dojoUri("src/widget/templates/HtmlTooltipTemplate.css"),

	connectNode: null,

	hovering: false,
	displayed: false,

	fillInTemplate: function(args, frag){
		if(this.caption != ""){
			this.domNode.appendChild(document.createTextNode(this.caption));
		}
		this.connectNode = dojo.byId(this.connectId);
		
		// IE bug workaround
		this.bgIframe = new dojo.html.BackgroundIframe();
		
		dojo.widget.html.Tooltip.superclass.fillInTemplate.call(this, args, frag);
	},
	
	postCreate: function(args, frag){
		// The domnode was appended to my parent widget's domnode, but the positioning
		// only works if the domnode is a child of document.body
		document.body.appendChild(this.domNode);

		var self = this;
		this.timerEvent = function () { self.display.apply(self); };
		dojo.event.connect(this.connectNode, "onmouseover", this, "onMouseOver");
		dojo.widget.html.Tooltip.superclass.postCreate.call(this, args, frag);
	},
	
	onMouseOver: function(e) {
		// ignore duplicate events
		if(this.hovering){ return; }

		this.timerEventId = setTimeout(this.timerEvent, this.delay);
		dojo.event.connect(document.documentElement, "onmousemove", this, "onMouseMove");
		this.hovering=true;		
	},

	onMouseMove: function(e) {
		if(!this.hovering){ return; }

		// Have the mouse been moved off the element?
		// Note: can't use onMouseOut because the "explode" effect causes
		// spurious onMouseOut/onMouseOver events (due to interference from outline)
		if( !dojo.html.overElement(this.connectNode, e) ){
			if ( this.timerEventId ) {
				clearTimeout(this.timerEventId);
				delete this.timerEventId;
			}
			dojo.event.disconnect(document.documentElement, "onmousemove", this, "onMouseMove");
			this.hovering=false;
			this.erase();
			return;
		}

		this.mouseX = e.pageX || e.clientX + document.body.scrollLeft;
		this.mouseY = e.pageY || e.clientY + document.body.scrollTop;
	},

	display: function() {
		if ( this.displayed ) { return; }

		this.domNode.style.top = this.mouseY + 15 + "px";
		this.domNode.style.left = this.mouseX + 10 + "px";

		// if rendering using explosion effect, need to set explosion source
		this.explodeSrc = [this.mouseX, this.mouseY];

		this.show();
		this.bgIframe.show(this.domNode);

		this.displayed=true;
	},

	onShow: function() {
		// for explode effect, have to display the iframe after the effect completes
		this.bgIframe.show(this.domNode);
		dojo.widget.html.Tooltip.superclass.onShow.call(this);
	},

	erase: function() {
		if ( this.displayed ) {
			this.hide();
			this.bgIframe.hide();
			this.displayed=false;
		}
	}
});
