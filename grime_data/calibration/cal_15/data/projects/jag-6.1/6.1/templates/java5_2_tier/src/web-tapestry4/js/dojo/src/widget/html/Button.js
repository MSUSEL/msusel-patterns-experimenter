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

dojo.provide("dojo.widget.html.Button");
dojo.require("dojo.widget.Button");

dojo.deprecated("dojo.widget.Button",  "use dojo.widget.Button2", "0.4");

dojo.widget.html.Button = function(){
	// mix in the button properties
	dojo.widget.Button.call(this);
	dojo.widget.HtmlWidget.call(this);
}
dojo.inherits(dojo.widget.html.Button, dojo.widget.HtmlWidget);
dojo.lang.extend(dojo.widget.html.Button, {

	templatePath: dojo.uri.dojoUri("src/widget/templates/HtmlButtonTemplate.html"),
	templateCssPath: dojo.uri.dojoUri("src/widget/templates/HtmlButtonTemplate.css"),

	label: "",
	labelNode: null,
	containerNode: null,

	postCreate: function(args, frag){
		this.labelNode = this.containerNode;
		/*
		if(this.label != "undefined"){
			this.domNode.appendChild(document.createTextNode(this.label));
		}
		*/
	},
	
	onMouseOver: function(e){
		dojo.html.addClass(this.domNode, "dojoButtonHover");
		dojo.html.removeClass(this.domNode, "dojoButtonNoHover");
	},
	
	onMouseOut: function(e){
		dojo.html.removeClass(this.domNode, "dojoButtonHover");
		dojo.html.addClass(this.domNode, "dojoButtonNoHover");
	},

	// By default, when I am clicked, click the item (link) inside of me.
	// By default, a button is a disguised link.
	// Todo: support actual submit and reset buttons.
	onClick: function (e) {
		var child = dojo.dom.getFirstChildElement(this.domNode);
		if(child){
			if(child.click){
				child.click();
			}else if(child.href){
				location.href = child.href;
			}
		}
	}
});
