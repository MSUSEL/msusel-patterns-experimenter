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

dojo.provide("dojo.text.textDirectory");
dojo.provide("dojo.text.textDirectory.Property");
dojo.provide("dojo.text.textDirectory.tokenise");
dojo.require("dojo.string");

/* adapted from Paul Sowden's iCalendar work */

dojo.textDirectoryTokeniser = function () {}

/*
 * This class parses a single line from a text/directory file
 * and returns an object with four named values; name, group, params
 * and value. name, group and value are strings containing the original
 * tokens unaltered and values is an array containing name/value pairs
 * or a single name token packed into arrays.
 */
dojo.textDirectoryTokeniser.Property = function (line) {
	// split into name/value pair
	var left = dojo.string.trim(line.substring(0, line.indexOf(':')));
	var right = dojo.string.trim(line.substr(line.indexOf(':') + 1));

	// seperate name and paramters	
	var parameters = dojo.string.splitEscaped(left,';');
	this.name = parameters[0]
	parameters.splice(0, 1);

	// parse paramters
	this.params = [];
	for (var i = 0; i < parameters.length; i++) {
		var arr = parameters[i].split("=");
		var key = dojo.string.trim(arr[0].toUpperCase());
		
		if (arr.length == 1) { this.params.push([key]); continue; }
		
		var values = dojo.string.splitEscaped(arr[1],',');
		for (var j = 0; j < values.length; j++) {
			if (dojo.string.trim(values[j]) != '') {
				this.params.push([key, dojo.string.trim(values[j])]);
			}
		}
	}

	// seperate group
	if (this.name.indexOf('.') > 0) {
		var arr = this.name.split('.');
		this.group = arr[0];
		this.name = arr[1];
	}
	
	// don't do any parsing, leave to implementation
	this.value = right;
}


// tokeniser, parses into an array of properties.
dojo.textDirectoryTokeniser.tokenise = function (text) {
	// normlize to one propterty per line and parse
	nText = dojo.string.normalizeNewlines(text,"\n");
	nText = nText.replace(/\n[ \t]/g, '');
	nText = nText.replace(/\x00/g, '');
		
	var lines = nText.split("\n");
	var properties = []

	for (var i = 0; i < lines.length; i++) {
		if (dojo.string.trim(lines[i]) == '') { continue; }
		var prop = new dojo.textDirectoryTokeniser.Property(lines[i]);
		properties.push(prop);
	}
	return properties;
}
