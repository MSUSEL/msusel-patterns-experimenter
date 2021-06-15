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

dojo.require("dojo.widget.Checkbox");
dojo.provide("dojo.widget.html.Checkbox");

// FIXME: the input doesn't get taken out of the tab list (i think)
// FIXME: the image doesn't get into the tab list (needs to steal the tabindex value from the input)

dojo.widget.html.Checkbox = function(){
	dojo.widget.HtmlWidget.call(this);
}

dojo.inherits(dojo.widget.html.Checkbox, dojo.widget.HtmlWidget);

dojo.lang.extend(dojo.widget.html.Checkbox, {
	widgetType: "Checkbox",

	_testImg: null,

	_events: [
		"onclick",
		"onfocus",
		"onblur",
		"onselect",
		"onchange",
		"onclick",
		"ondblclick",
		"onmousedown",
		"onmouseup",
		"onmouseover",
		"onmousemove",
		"onmouseout",
		"onkeypress",
		"onkeydown",
		"onkeyup"
	],

	srcOn: dojo.uri.dojoUri('src/widget/templates/check_on.gif'),
	srcOff: dojo.uri.dojoUri('src/widget/templates/check_off.gif'),

	fillInTemplate: function(){

		// FIXME: if images are disabled, we DON'T want to swap out the element
		// we can use the usual 'load image to check' trick
		// i don't know what image we can check yet, so we'll skip this for now...

		// this._testImg = document.createElement("img");
		// document.body.appendChild(this._testImg);
		// this._testImg.src = "spacer.gif?cachebust=" + new Date().valueOf();
		// dojo.connect(this._testImg, 'onload', this, 'onImagesLoaded');

		this.onImagesLoaded();
	},

	onImagesLoaded: function(){

		// FIXME: if we actually check for loading images, remove the thing here
		// document.body.removeChild(this._testImg);

		// 'hide' the checkbox
		this.domNode.style.position = "absolute";
		this.domNode.style.left = "-9000px";

		// create a replacement image
		this.imgNode = document.createElement("img");
		dojo.html.addClass(this.imgNode, "dojoHtmlCheckbox");
		this.updateImgSrc();
		dojo.event.connect(this.imgNode, 'onclick', this, 'onClick');
		dojo.event.connect(this.domNode, 'onchange', this, 'onChange');
		this.domNode.parentNode.insertBefore(this.imgNode, this.domNode.nextSibling)

		// real ugly - make sure the image has all the events that the checkbox did
		for(var i=0; i<this._events.length; i++){
			if(this.domNode[this._events[i]]){
				dojo.event.connect(	this.imgNode, this._events[i], 
									this.domNode[this._events[i]]);
			}
		}
	},

	onClick: function(){

		this.domNode.checked = !this.domNode.checked ? true : false;
		this.updateImgSrc();
	},

	onChange: function(){

		this.updateImgSrc();
	},

	updateImgSrc: function(){

		this.imgNode.src = this.domNode.checked ? this.srcOn : this.srcOff;
	}
});

