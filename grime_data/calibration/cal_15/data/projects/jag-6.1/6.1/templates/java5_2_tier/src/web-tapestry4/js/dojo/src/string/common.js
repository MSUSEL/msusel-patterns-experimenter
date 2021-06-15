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

dojo.provide("dojo.string.common");

dojo.require("dojo.string");

/**
 * Trim whitespace from 'str'. If 'wh' > 0,
 * only trim from start, if 'wh' < 0, only trim
 * from end, otherwise trim both ends
 */
dojo.string.trim = function(str, wh){
	if(!str.replace){ return str; }
	if(!str.length){ return str; }
	var re = (wh > 0) ? (/^\s+/) : (wh < 0) ? (/\s+$/) : (/^\s+|\s+$/g);
	return str.replace(re, "");
}

/**
 * Trim whitespace at the beginning of 'str'
 */
dojo.string.trimStart = function(str) {
	return dojo.string.trim(str, 1);
}

/**
 * Trim whitespace at the end of 'str'
 */
dojo.string.trimEnd = function(str) {
	return dojo.string.trim(str, -1);
}

/**
 * Return 'str' repeated 'count' times, optionally
 * placing 'separator' between each rep
 */
dojo.string.repeat = function(str, count, separator) {
	var out = "";
	for(var i = 0; i < count; i++) {
		out += str;
		if(separator && i < count - 1) {
			out += separator;
		}
	}
	return out;
}

/**
 * Pad 'str' to guarantee that it is at least 'len' length
 * with the character 'c' at either the start (dir=1) or
 * end (dir=-1) of the string
 */
dojo.string.pad = function(str, len/*=2*/, c/*='0'*/, dir/*=1*/) {
	var out = String(str);
	if(!c) {
		c = '0';
	}
	if(!dir) {
		dir = 1;
	}
	while(out.length < len) {
		if(dir > 0) {
			out = c + out;
		} else {
			out += c;
		}
	}
	return out;
}

/** same as dojo.string.pad(str, len, c, 1) */
dojo.string.padLeft = function(str, len, c) {
	return dojo.string.pad(str, len, c, 1);
}

/** same as dojo.string.pad(str, len, c, -1) */
dojo.string.padRight = function(str, len, c) {
	return dojo.string.pad(str, len, c, -1);
}
