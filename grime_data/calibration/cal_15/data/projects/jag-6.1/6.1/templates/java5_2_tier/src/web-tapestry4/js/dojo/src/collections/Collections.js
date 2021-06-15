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

dojo.provide("dojo.collections.Collections");

dojo.collections = {Collections:true};
dojo.collections.DictionaryEntry = function(k,v){
	this.key = k;
	this.value = v;
	this.valueOf = function(){ return this.value; };
	this.toString = function(){ return this.value; };
}

dojo.collections.Iterator = function(a){
	var obj = a;
	var position = 0;
	this.atEnd = (position>=obj.length-1);
	this.current = obj[position];
	this.moveNext = function(){
		if(++position>=obj.length){
			this.atEnd = true;
		}
		if(this.atEnd){
			return false;
		}
		this.current=obj[position];
		return true;
	}
	this.reset = function(){
		position = 0;
		this.atEnd = false;
		this.current = obj[position];
	}
}

dojo.collections.DictionaryIterator = function(obj){
	var arr = [] ;	//	Create an indexing array
	for (var p in obj) {
		arr.push(obj[p]);	//	fill it up
	}
	var position = 0 ;
	this.atEnd = (position>=arr.length-1);
	this.current = arr[position]||null ;
	this.entry = this.current||null ;
	this.key = (this.entry)?this.entry.key:null ;
	this.value = (this.entry)?this.entry.value:null ;
	this.moveNext = function() { 
		if (++position>=arr.length) {
			this.atEnd = true ;
		}
		if(this.atEnd){
			return false;
		}
		this.entry = this.current = arr[position] ;
		if (this.entry) {
			this.key = this.entry.key ;
			this.value = this.entry.value ;
		}
		return true;
	} ;
	this.reset = function() { 
		position = 0 ; 
		this.atEnd = false ;
		this.current = arr[position]||null ;
		this.entry = this.current||null ;
		this.key = (this.entry)?this.entry.key:null ;
		this.value = (this.entry)?this.entry.value:null ;
	} ;
};
