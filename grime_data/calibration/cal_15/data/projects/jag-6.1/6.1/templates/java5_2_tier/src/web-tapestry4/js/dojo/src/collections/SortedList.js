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

dojo.provide("dojo.collections.SortedList");
dojo.require("dojo.collections.Collections");

dojo.collections.SortedList = function(dictionary){
	var _this = this;
	var items = {};
	var q = [];
	var sorter = function(a,b){
		if (a.key > b.key) return 1;
		if (a.key < b.key) return -1;
		return 0;
	};
	var build = function(){
		q = [];
		var e = _this.getIterator();
		while (!e.atEnd) {
			q.push(e.entry);
			e.moveNext();
		}
		q.sort(sorter);
	};

	this.count = q.length;
	this.add = function(k,v){
		if (!items[k]) {
			items[k] = new dojo.collections.DictionaryEntry(k,v);
			this.count = q.push(items[k]);
			q.sort(sorter);
		}
	};
	this.clear = function(){
		items = {};
		q = [];
		this.count = q.length;
	};
	this.clone = function(){
		return new dojo.collections.SortedList(this);
	};
	this.contains = this.containsKey = function(k){
		return (items[k] != null);
	};
	this.containsValue = function(o){
		var e = this.getIterator();
		while (!e.atEnd){
			if (e.value == o) return true;
			e.moveNext();
		}
		return false;
	};
	this.copyTo = function(arr, i){
		var e = this.getIterator();
		var idx = i;
		while (!e.atEnd){
			arr.splice(idx, 0, e.entry);
			idx++;
			e.moveNext();
		}
	};
	this.getByIndex = function(i){
		return q[i].value;
	};
	this.getIterator = function(){
		return new dojo.collections.DictionaryIterator(items);
	};
	this.getKey = function(i){
		return q[i].key;
	};
	this.getKeyList = function(){
		var arr = [];
		var e = this.getIterator();
		while (!e.atEnd){
			arr.push(e.key);
			e.moveNext();
		}
		return arr;
	};
	this.getValueList = function(){
		var arr = [];
		var e = this.getIterator();
		while (!e.atEnd){
			arr.push(e.value);
			e.moveNext();
		}
		return arr;
	};
	this.indexOfKey = function(k){
		for (var i = 0; i < q.length; i++){
			if (q[i].key == k) {
				return i;
			}
		}
		return -1;
	};
	this.indexOfValue = function(o){
		for (var i = 0; i < q.length; i++){
			if (q[i].value == o) {
				return i;
			}
		}
		return -1;
	};
	this.item = function(k){
		return items[k];
	};

	this.remove = function(k){
		delete items[k];
		build();
		this.count = q.length;
	};
	
	this.removeAt = function(i){
		delete items[q[i].key];
		build();
		this.count = q.length;
	};

	this.replace = function(k,v){
		if (!items[k]) {
			this.add(k,v);
			return false; // returning false because we added rather than replaced
		} else {
			items[k] = new dojo.collections.DictionaryEntry(k,v);
			q.sort(sorter);
			return true; // returning true because we replaced a value
		}
	};

	this.setByIndex = function(i,o){
		items[q[i].key].value = o;
		build();
		this.count = q.length;
	};

	if (dictionary){
		var e = dictionary.getIterator();
		while (!e.atEnd) {
			q[q.length] = items[e.key] = new dojo.collections.DictionaryEntry(e.key, e.value);
			e.moveNext();
		}
		q.sort(sorter);
	}
}
