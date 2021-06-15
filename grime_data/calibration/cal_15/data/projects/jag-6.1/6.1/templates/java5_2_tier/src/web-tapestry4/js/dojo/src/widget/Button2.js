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

dojo.provide("dojo.widget.Button2");
dojo.provide("dojo.widget.DropDownButton2");
dojo.provide("dojo.widget.ComboButton2");
dojo.require("dojo.widget.Widget");

dojo.widget.tags.addParseTreeHandler("dojo:button2");
dojo.widget.tags.addParseTreeHandler("dojo:dropdownbutton2");
dojo.widget.tags.addParseTreeHandler("dojo:combobutton2");

dojo.widget.Button2 = function(){
}
dojo.lang.extend(dojo.widget.Button2, {
	widgetType: "Button2",
	isContainer: true,

	// Constructor arguments
	caption: "",
	disabled: false,
	onClick: function(){ }
});

dojo.widget.DropDownButton2 = function(){
}
dojo.inherits(dojo.widget.DropDownButton2, dojo.widget.Button2);
dojo.lang.extend(dojo.widget.DropDownButton2, {
	widgetType: "DropDownButton2",
	isContainer: true,

	// constructor arguments
	menuId: ''
});

dojo.widget.ComboButton2 = function(){
}
dojo.inherits(dojo.widget.ComboButton2, dojo.widget.Button2);
dojo.lang.extend(dojo.widget.ComboButton2, {
	widgetType: "ComboButton2",
	isContainer: true,

	// constructor arguments
	menuId: ''
});

dojo.requireAfterIf("html", "dojo.widget.html.Button2");

