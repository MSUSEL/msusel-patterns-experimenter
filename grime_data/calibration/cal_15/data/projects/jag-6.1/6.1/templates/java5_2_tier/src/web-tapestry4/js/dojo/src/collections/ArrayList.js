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

dojo.provide("dojo.collections.ArrayList");
dojo.require("dojo.collections.Collections");

dojo.collections.ArrayList = function(arr){
	var items = [];
	if (arr) items = items.concat(arr);
	this.count = items.length;
	this.add = function(obj){
		items.push(obj);
		this.count = items.length;
	};
	this.addRange = function(a){
		if (a.getIterator) {
			var e = a.getIterator();
			while (!e.atEnd) {
				this.add(e.current);
				e.moveNext();
			}
			this.count = items.length;
		} else {
			for (var i=0; i<a.length; i++){
				items.push(a[i]);
			}
			this.count = items.length;
		}
	};
	this.clear = function(){
		items.splice(0, items.length);
		this.count = 0;
	};
	this.clone = function(){
		return new dojo.collections.ArrayList(items);
	};
	this.contains = function(obj){
		for (var i = 0; i < items.length; i++){
			if (items[i] == obj) {
				return true;
			}
		}
		return false;
	};
	this.getIterator = function(){
		return new dojo.collections.Iterator(items);
	};
	this.indexOf = function(obj){
		for (var i = 0; i < items.length; i++){
			if (items[i] == obj) {
				return i;
			}
		}
		return -1;
	};
	this.insert = function(i, obj){
		items.splice(i,0,obj);
		this.count = items.length;
	};
	this.item = function(k){
		return items[k];
	};
	this.remove = function(obj){
		var i = this.indexOf(obj);
		if (i >=0) {
			items.splice(i,1);
		}
		this.count = items.length;
	};
	this.removeAt = function(i){
		items.splice(i,1);
		this.count = items.length;
	};
	this.reverse = function(){
		items.reverse();
	};
	this.sort = function(fn){
		if (fn){
			items.sort(fn);
		} else {
			items.sort();
		}
	};
	this.setByIndex = function(i, obj){
		items[i]=obj;
		this.count=items.length;
	};
	this.toArray = function(){
		return [].concat(items);
	}
	this.toString = function(){
		return items.join(",");
	};
};
