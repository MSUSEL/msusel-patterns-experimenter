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

dojo.provide("dojo.reflect");

/*****************************************************************
	reflect.js
	v.1.5.0
	(c) 2003-2004 Thomas R. Trenka, Ph.D.

	Derived from the reflection functions of f(m).
	http://dojotoolkit.org
	http://fm.dept-z.com

	There is a dependency on the variable dJ_global, which
	should always refer to the global object.
******************************************************************/
if(!dj_global){ var dj_global = this; }

dojo.reflect = {} ;
dojo.reflect.$unknownType = function(){ } ;
dojo.reflect.ParameterInfo = function(name, type){ 
	this.name = name ;
	this.type = (type) ? type : dojo.reflect.$unknownType ;
} ;
dojo.reflect.PropertyInfo = function(name, type) { 
	this.name = name ;
	this.type = (type) ? type : dojo.reflect.$unknownType ;
} ;
dojo.reflect.MethodInfo = function(name, fn){
	var parse = function(f) {
		var o = {} ; 
		var s = f.toString() ;
		var param = ((s.substring(s.indexOf('(')+1, s.indexOf(')'))).replace(/\s+/g, "")).split(",") ;
		o.parameters = [] ;
		for (var i = 0; i < param.length; i++) {
			o.parameters.push(new dojo.reflect.ParameterInfo(param[i])) ;
		}
		o.body = (s.substring(s.indexOf('{')+1, s.lastIndexOf('}'))).replace(/(^\s*)|(\s*$)/g, "") ;
		return o ;
	} ;

	var tmp = parse(fn) ;
	var p = tmp.parameters ;
	var body = tmp.body ;
	
	this.name = (name) ? name : "anonymous" ;
	this.getParameters = function(){ return p ; } ;
	this.getNullArgumentsObject = function() {
		var a = [] ;
		for (var i = 0; i < p.length; i++){
			a.push(null);
		}
		return a ;
	} ;
	this.getBody = function() { return body ; } ;
	this.type = Function ;
	this.invoke = function(src, args){ return fn.apply(src, args) ; } ;
} ;

//	Static object that can activate instances of the passed type.
dojo.reflect.Activator = new (function(){
	this.createInstance = function(type, args) {
		switch (typeof(type)) {
			case "function" : { 
				var o = {} ;
				type.apply(o, args) ;
				return o ;
			} ;
			case "string" : {
				var o = {} ;
				(dojo.reflect.Reflector.getTypeFromString(type)).apply(o, args) ;
				return o ;
			} ;
		}
		throw new Error("dojo.reflect.Activator.createInstance(): no such type exists.");
	}
})() ;

dojo.reflect.Reflector = new (function(){
	this.getTypeFromString = function(s) {
		var parts = s.split("."), i = 0, obj = dj_global ; 
		do { obj = obj[parts[i++]] ; } while (i < parts.length && obj) ; 
		return (obj != dj_global) ? obj : null ;
	}; 

	this.typeExists = function(s) {
		var parts = s.split("."), i = 0, obj = dj_global ; 
		do { obj = obj[parts[i++]] ; } while (i < parts.length && obj) ; 
		return (obj && obj != dj_global) ;
	}; 

	this.getFieldsFromType = function(s) { 
		var type = s ;
		if (typeof(s) == "string") {
			type = this.getTypeFromString(s) ;
		}
		var nullArgs = (new dojo.reflect.MethodInfo(type)).getNullArgumentsObject() ;
		return this.getFields(dojo.reflect.Activator.createInstance(s, nullArgs)) ;
	};

	this.getPropertiesFromType = function(s) { 
		var type = s ;
		if (typeof(s) == "string") {
			type = this.getTypeFromString(s);
		}
		var nullArgs = (new dojo.reflect.MethodInfo(type)).getNullArgumentsObject() ;
		return this.getProperties(dojo.reflect.Activator.createInstance(s, nullArgs)) ;
	};

	this.getMethodsFromType = function(s) { 
		var type = s ;
		if (typeof(s) == "string") {
			type = this.getTypeFromString(s) ;
		}
		var nullArgs = (new dojo.reflect.MethodInfo(type)).getNullArgumentsObject() ;
		return this.getMethods(dojo.reflect.Activator.createInstance(s, nullArgs)) ;
	};

	this.getType = function(o) { return o.constructor ; } ;

	this.getFields = function(obj) {
		var arr = [] ;
		for (var p in obj) { 
			if(this.getType(obj[p]) != Function){
				arr.push(new dojo.reflect.PropertyInfo(p, this.getType(obj[p]))) ;
			}else{
				arr.push(new dojo.reflect.MethodInfo(p, obj[p]));
			}
		}
		return arr ;
	};

	this.getProperties = function(obj) {
		var arr = [] ;
		var fi = this.getFields(obj) ;
		for (var i = 0; i < fi.length; i++){
			if (this.isInstanceOf(fi[i], dojo.reflect.PropertyInfo)){
				arr.push(fi[i]) ;
			}
		}
		return arr ;
	};

	this.getMethods = function(obj) {
		var arr = [] ;
		var fi = this.getFields(obj) ;
		for (var i = 0; i < fi.length; i++){
			if (this.isInstanceOf(fi[i], dojo.reflect.MethodInfo)){
				arr.push(fi[i]) ;
			}
		}
		return arr ;
	};

	/*
	this.implements = function(o, type) {
		if (this.isSubTypeOf(o, type)) return false ;
		var f = this.getFieldsFromType(type) ;
		for (var i = 0; i < f.length; i++) {
			if (typeof(o[(f[i].name)]) == "undefined"){
				return false;
			}
		}
		return true ;
	};
	*/

	this.getBaseClass = function(o) {
		if (o.getType().prototype.prototype.constructor){
			return (o.getType()).prototype.prototype.constructor ;
		}
		return Object ;
	} ;

	this.isInstanceOf = function(o, type) { 
		return (this.getType(o) == type) ; 
	};

	this.isSubTypeOf = function(o, type) { 
		return (o instanceof type) ; 
	};

	this.isBaseTypeOf = function(o, type) { 
		return (type instanceof o); 
	};
})();

// back-compat
dojo.provide("dojo.reflect.reflection");
