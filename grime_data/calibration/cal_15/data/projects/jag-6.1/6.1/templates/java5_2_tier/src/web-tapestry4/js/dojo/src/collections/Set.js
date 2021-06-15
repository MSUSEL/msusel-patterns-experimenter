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

dojo.provide("dojo.collections.Set");
dojo.require("dojo.collections.Collections");
dojo.require("dojo.collections.ArrayList");

//	straight up sets are based on arrays or array-based collections.
dojo.collections.Set = new function(){
	this.union = function(setA, setB){
		if (setA.constructor == Array) var setA = new dojo.collections.ArrayList(setA);
		if (setB.constructor == Array) var setB = new dojo.collections.ArrayList(setB);
		if (!setA.toArray || !setB.toArray) dojo.raise("Set operations can only be performed on array-based collections.");
		var result = new dojo.collections.ArrayList(setA.toArray());
		var e = setB.getIterator();
		while (!e.atEnd){
			if (!result.contains(e.current)) result.add(e.current);
		}
		return result;
	};
	this.intersection = function(setA, setB){
		if (setA.constructor == Array) var setA = new dojo.collections.ArrayList(setA);
		if (setB.constructor == Array) var setB = new dojo.collections.ArrayList(setB);
		if (!setA.toArray || !setB.toArray) dojo.raise("Set operations can only be performed on array-based collections.");
		var result = new dojo.collections.ArrayList();
		var e = setB.getIterator();
		while (!e.atEnd){
			if (setA.contains(e.current)) result.add(e.current);
			e.moveNext();
		}
		return result;
	};
	//	returns everything in setA that is not in setB.
	this.difference = function(setA, setB){
		if (setA.constructor == Array) var setA = new dojo.collections.ArrayList(setA);
		if (setB.constructor == Array) var setB = new dojo.collections.ArrayList(setB);
		if (!setA.toArray || !setB.toArray) dojo.raise("Set operations can only be performed on array-based collections.");
		var result = new dojo.collections.ArrayList();
		var e = setA.getIterator();
		while (!e.atEnd){
			if (!setB.contains(e.current)) result.add(e.current);
			e.moveNext();
		}
		return result;
	};
	this.isSubSet = function(setA, setB) {
		if (setA.constructor == Array) var setA = new dojo.collections.ArrayList(setA);
		if (setB.constructor == Array) var setB = new dojo.collections.ArrayList(setB);
		if (!setA.toArray || !setB.toArray) dojo.raise("Set operations can only be performed on array-based collections.");
		var e = setA.getIterator();
		while (!e.atEnd){
			if (!setB.contains(e.current)) return false;
			e.moveNext();
		}
		return true;
	};
	this.isSuperSet = function(setA, setB){
		if (setA.constructor == Array) var setA = new dojo.collections.ArrayList(setA);
		if (setB.constructor == Array) var setB = new dojo.collections.ArrayList(setB);
		if (!setA.toArray || !setB.toArray) dojo.raise("Set operations can only be performed on array-based collections.");
		var e = setB.getIterator();
		while (!e.atEnd){
			if (!setA.contains(e.current)) return false;
			e.moveNext();
		}
		return true;
	};
}();
