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
/* Fix for Opera, which does not seem to include the map function on Array's */
if(!Array.prototype.map){
    Array.prototype.map = function(fun){
	var len = this.length;
	if(typeof fun != "function"){
	    throw new TypeError();
	}
	var res = new Array(len);
	var thisp = arguments[1];
	for(var i = 0; i < len; i++){
	    if(i in this){
		res[i] = fun.call(thisp, this[i], i, this);
	    }
	}
        return res;
     };
}

/* Paging Memory Proxy, allows to use paging grid with in memory dataset */
Ext.data.PagingMemoryProxy = function(data) {
	Ext.data.PagingMemoryProxy.superclass.constructor.call(this);
	this.data = data;
};

Ext.extend(Ext.data.PagingMemoryProxy, Ext.data.MemoryProxy, {
	load : function(params, reader, callback, scope, arg) {
		params = params || {};
		var result;
		try {
			result = reader.readRecords(this.data);
		}catch(e){
			this.fireEvent("loadexception", this, arg, null, e);
			callback.call(scope, null, arg, false);
			return;
		}
		
		// filtering
		if (params.filter!==undefined) {
			result.records = result.records.filter(function(el){
			    if (typeof(el)=="object"){
					var att = params.filterCol || 0;
					return String(el.data[att]).match(params.filter)?true:false;
			    } else {
					return String(el).match(params.filter)?true:false;
			    }
			});
			result.totalRecords = result.records.length;
		}
		
		// sorting
		if (params.sort!==undefined) {
		    // use integer as params.sort to specify column, since arrays are not named
		    // params.sort=0; would also match a array without columns
		    var dir = String(params.dir).toUpperCase() == "DESC" ? -1 : 1;
        	var fn = function(v1, v2){
                return v1 > v2 ? 1 : (v1 < v2 ? -1 : 0);
            };
		    result.records.sort(function(a, b) {
				var v = 0;
				if (typeof(a)=="object"){
				    v = fn(a.data[params.sort], b.data[params.sort]) * dir;
				} else {
				    v = fn(a, b) * dir;
				}
				if (v==0) {
				    v = (a.index < b.index ? -1 : 1);
				}
				return v;
		    });
		}

		// paging (use undefined cause start can also be 0 (thus false))
		if (params.start!==undefined && params.limit!==undefined) {
			result.records = result.records.slice(params.start, params.start+params.limit);
		}
		
		callback.call(scope, result, arg, true);
	}
});
