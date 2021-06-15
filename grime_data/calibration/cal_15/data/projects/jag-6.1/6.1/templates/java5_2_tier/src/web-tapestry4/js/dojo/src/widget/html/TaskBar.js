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

dojo.provide("dojo.widget.html.TaskBar");
dojo.provide("dojo.widget.html.TaskBarItem");

dojo.require("dojo.widget.*");
dojo.require("dojo.widget.FloatingPane");
dojo.require("dojo.widget.HtmlWidget");
dojo.require("dojo.event");

// Icon associated w/a floating pane
dojo.widget.html.TaskBarItem = function(){
	dojo.widget.TaskBarItem.call(this);
	dojo.widget.HtmlWidget.call(this);
}
dojo.inherits(dojo.widget.html.TaskBarItem, dojo.widget.HtmlWidget);

dojo.lang.extend(dojo.widget.html.TaskBarItem, {
	// constructor arguments
	iconSrc: '',
	caption: 'Untitled',
	window: null,
	templatePath: dojo.uri.dojoUri("src/widget/templates/HtmlTaskBarItemTemplate.html"),
	templateCssPath: dojo.uri.dojoUri("src/widget/templates/HtmlTaskBar.css"),

	fillInTemplate: function() {
		if ( this.iconSrc != '' ) {
			var img = document.createElement("img");
			img.src = this.iconSrc;
			this.domNode.appendChild(img);
		}
		this.domNode.appendChild(document.createTextNode(this.caption));
		dojo.html.disableSelection(this.domNode);
	},

	postCreate: function() {
		this.window=dojo.widget.getWidgetById(this.windowId);
		this.window.explodeSrc = this.domNode;
		dojo.event.connect(this.window, "destroy", this, "destroy")
	},

	onClick: function() {
		if (this.window.windowState != "minimized") {
			this.window.bringToTop();
		} else {
			this.window.restoreWindow();
		}
	}
});

// Collection of widgets in a bar, like Windows task bar
dojo.widget.html.TaskBar = function(){

	dojo.widget.html.FloatingPane.call(this);
	dojo.widget.TaskBar.call(this);
	this.titleBarDisplay = "none";
}

dojo.inherits(dojo.widget.html.TaskBar, dojo.widget.html.FloatingPane);

dojo.lang.extend(dojo.widget.html.TaskBar, {
	addChild: function(child) {
		var tbi = dojo.widget.createWidget("TaskBarItem",{windowId:child.widgetId, caption: child.title, iconSrc: child.iconSrc} );
		dojo.widget.html.TaskBar.superclass.addChild.call(this,tbi);
	}
});
