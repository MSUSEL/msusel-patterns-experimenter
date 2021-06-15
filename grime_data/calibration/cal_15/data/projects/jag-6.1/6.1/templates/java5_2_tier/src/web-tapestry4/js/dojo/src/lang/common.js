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

dojo.provide("dojo.lang.common");

dojo.require("dojo.lang");

dojo.lang.mixin = function(obj, props){
	var tobj = {};
	for(var x in props){
		if(typeof tobj[x] == "undefined" || tobj[x] != props[x]) {
			obj[x] = props[x];
		}
	}
	// IE doesn't recognize custom toStrings in for..in
	if(dojo.render.html.ie && dojo.lang.isFunction(props["toString"]) && props["toString"] != obj["toString"]) {
		obj.toString = props.toString;
	}
	return obj;
}

dojo.lang.extend = function(ctor, props){
	this.mixin(ctor.prototype, props);
}

/**
 * See if val is in arr. Call signatures:
 *  find(array, value, identity) // recommended
 *  find(value, array, identity)
**/
dojo.lang.find = function(arr, val, identity, findLast){
	// support both (arr, val) and (val, arr)
	if(!dojo.lang.isArrayLike(arr) && dojo.lang.isArrayLike(val)) {
		var a = arr;
		arr = val;
		val = a;
	}
	var isString = dojo.lang.isString(arr);
	if(isString) { arr = arr.split(""); }

	if(findLast) {
		var step = -1;
		var i = arr.length - 1;
		var end = -1;
	} else {
		var step = 1;
		var i = 0;
		var end = arr.length;
	}
	if(identity){
		while(i != end) {
			if(arr[i] === val){ return i; }
			i += step;
		}
	}else{
		while(i != end) {
			if(arr[i] == val){ return i; }
			i += step;
		}
	}
	return -1;
}

dojo.lang.indexOf = dojo.lang.find;

dojo.lang.findLast = function(arr, val, identity) {
	return dojo.lang.find(arr, val, identity, true);
}

dojo.lang.lastIndexOf = dojo.lang.findLast;

dojo.lang.inArray = function(arr, val){
	return dojo.lang.find(arr, val) > -1;
}

/**
 * Partial implmentation of is* functions from
 * http://www.crockford.com/javascript/recommend.html
 * NOTE: some of these may not be the best thing to use in all situations
 * as they aren't part of core JS and therefore can't work in every case.
 * See WARNING messages inline for tips.
 *
 * The following is* functions are fairly "safe"
 */

dojo.lang.isObject = function(wh) {
	return typeof wh == "object" || dojo.lang.isArray(wh) || dojo.lang.isFunction(wh);
}

dojo.lang.isArray = function(wh) {
	return (wh instanceof Array || typeof wh == "array");
}

dojo.lang.isArrayLike = function(wh) {
	if(dojo.lang.isString(wh)){ return false; }
	if(dojo.lang.isFunction(wh)){ return false; } // keeps out built-in ctors (Number, String, ...) which have length properties
	if(dojo.lang.isArray(wh)){ return true; }
	if(typeof wh != "undefined" && wh
		&& dojo.lang.isNumber(wh.length) && isFinite(wh.length)){ return true; }
	return false;
}

dojo.lang.isFunction = function(wh) {
	return (wh instanceof Function || typeof wh == "function");
}

dojo.lang.isString = function(wh) {
	return (wh instanceof String || typeof wh == "string");
}

dojo.lang.isAlien = function(wh) {
	return !dojo.lang.isFunction() && /\{\s*\[native code\]\s*\}/.test(String(wh));
}

dojo.lang.isBoolean = function(wh) {
	return (wh instanceof Boolean || typeof wh == "boolean");
}

/**
 * The following is***() functions are somewhat "unsafe". Fortunately,
 * there are workarounds the the language provides and are mentioned
 * in the WARNING messages.
 *
 * WARNING: In most cases, isNaN(wh) is sufficient to determine whether or not
 * something is a number or can be used as such. For example, a number or string
 * can be used interchangably when accessing array items (arr["1"] is the same as
 * arr[1]) and isNaN will return false for both values ("1" and 1). Should you
 * use isNumber("1"), that will return false, which is generally not too useful.
 * Also, isNumber(NaN) returns true, again, this isn't generally useful, but there
 * are corner cases (like when you want to make sure that two things are really
 * the same type of thing). That is really where isNumber "shines".
 *
 * RECOMMENDATION: Use isNaN(wh) when possible
 */
dojo.lang.isNumber = function(wh) {
	return (wh instanceof Number || typeof wh == "number");
}

/**
 * WARNING: In some cases, isUndefined will not behave as you
 * might expect. If you do isUndefined(foo) and there is no earlier
 * reference to foo, an error will be thrown before isUndefined is
 * called. It behaves correctly if you scope yor object first, i.e.
 * isUndefined(foo.bar) where foo is an object and bar isn't a
 * property of the object.
 *
 * RECOMMENDATION: Use `typeof foo == "undefined"` when possible
 *
 * FIXME: Should isUndefined go away since it is error prone?
 */
dojo.lang.isUndefined = function(wh) {
	return ((wh == undefined)&&(typeof wh == "undefined"));
}

// end Crockford functions
