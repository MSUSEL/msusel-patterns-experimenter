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

dojo.require("dojo.widget.Menu");
dojo.provide("dojo.widget.html.Menu");

/* HtmlMenu
 ***********/
 
dojo.widget.html.Menu = function(){
	dojo.widget.html.Menu.superclass.constructor.call(this);
	this.items = [];
}
dojo.inherits(dojo.widget.html.Menu, dojo.widget.HtmlWidget);

dojo.lang.extend(dojo.widget.html.Menu, {
	widgetType: "Menu",
	isContainer: true,

	// copy children widgets output directly to parent (this node), to avoid
	// errors trying to insert an <li> under a <div>
	snarfChildDomOutput: true,

	templateString: '<ul></ul>',
	templateCssPath: dojo.uri.dojoUri("src/widget/templates/Menu.css"),
	
	fillInTemplate: function (args, frag){
		//dojo.widget.HtmlMenu.superclass.fillInTemplate.apply(this, arguments);
		this.domNode.className = "dojoMenu";
	},
	
 
	_register: function (item ) {
		dojo.event.connect(item, "onSelect", this, "onSelect");
		this.items.push(item);
	},

	push: function (item) {
		this.domNode.appendChild(item.domNode);
		this._register(item);
	}

});

