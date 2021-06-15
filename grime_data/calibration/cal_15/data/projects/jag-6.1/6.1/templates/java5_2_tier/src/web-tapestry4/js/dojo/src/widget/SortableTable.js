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

dojo.provide("dojo.widget.SortableTable");
dojo.require("dojo.widget.*");
dojo.requireAfterIf("html", "dojo.widget.html.SortableTable");
dojo.widget.tags.addParseTreeHandler("dojo:sortableTable");

//	set up the general widget
dojo.widget.SortableTable=function(){
	dojo.widget.Widget.call(this);
	this.widgetType="SortableTable";
	this.isContainer=false;

	//	custom properties
	this.enableMultipleSelect=false;
	this.maximumNumberOfSelections=1;
	this.enableAlternateRows=false;
	this.minRows=0;	//	0 means ignore.
	this.defaultDateFormat="#M/#d/#yyyy";
	this.data=[];
	this.selected=null
	this.columns=[];
	this.sortIndex=0;		//	index of the column sorted on, first is the default.
	this.sortDirection=0;	//	0==asc, 1==desc
	this.sortFunctions={};	//	you can add to this if needed.
};
dojo.inherits(dojo.widget.SortableTable, dojo.widget.Widget);
