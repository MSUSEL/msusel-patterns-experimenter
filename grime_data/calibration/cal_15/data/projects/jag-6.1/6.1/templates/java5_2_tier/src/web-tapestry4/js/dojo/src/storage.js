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

// FIXME: should we require JSON here?
dojo.require("dojo.lang.*");
dojo.provide("dojo.storage");
dojo.provide("dojo.storage.StorageProvider");

dojo.storage = new function(){
	this.provider = null;

	// similar API as with dojo.io.addTransport()
	this.setProvider = function(obj){
		this.provider = obj;
	}

	this.set = function(key, value, namespace){
		// FIXME: not very expressive, doesn't have a way of indicating queuing
		if(!this.provider){
			return false;
		}
		return this.provider.set(key, value, namespace);
	}

	this.get = function(key, namespace){
		if(!this.provider){
			return false;
		}
		return this.provider.get(key, namespace);
	}

	this.remove = function(key, namespace){
		return this.provider.remove(key, namespace);
	}
}

dojo.storage.StorageProvider = function(){
}

dojo.lang.extend(dojo.storage.StorageProvider, {
	namespace: "*",
	initialized: false,

	free: function(){
		dojo.unimplemented("dojo.storage.StorageProvider.free");
		return 0;
	},

	freeK: function(){
		return dojo.math.round(this.free()/1024, 0);
	},

	set: function(key, value, namespace){
		dojo.unimplemented("dojo.storage.StorageProvider.set");
	},

	get: function(key, namespace){
		dojo.unimplemented("dojo.storage.StorageProvider.get");
	},

	remove: function(key, value, namespace){
		dojo.unimplemented("dojo.storage.StorageProvider.set");
	}

});
