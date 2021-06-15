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
if(!dojo._hasResource["dojo.data.api.Identity"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.data.api.Identity"] = true;
dojo.provide("dojo.data.api.Identity");
dojo.require("dojo.data.api.Read");

dojo.declare("dojo.data.api.Identity", dojo.data.api.Read, {
	//	summary:
	//		This is an abstract API that data provider implementations conform to.
	//		This file defines methods signatures and intentionally leaves all the
	//		methods unimplemented.

	getFeatures: function(){
		//	summary: 
		//		See dojo.data.api.Read.getFeatures()
		return {
			 'dojo.data.api.Read': true,
			 'dojo.data.api.Identity': true
		};
	},

	getIdentity: function(/* item */ item){
		//	summary:
		//		Returns a unique identifier for an item.  The return value will be
		//		either a string or something that has a toString() method (such as,
		//		for example, a dojox.uuid.Uuid object).
		//	item:
		//		The item from the store from which to obtain its identifier.
		//	exceptions:
		//		Conforming implementations may throw an exception or return null if
		//		item is not an item.
		//	example:
		//	|	var itemId = store.getIdentity(kermit);
		//	|	assert(kermit === store.findByIdentity(store.getIdentity(kermit)));
		throw new Error('Unimplemented API: dojo.data.api.Identity.getIdentity');
		var itemIdentityString = null;
		return itemIdentityString; // string
	},

	getIdentityAttributes: function(/* item */ item){
		//	summary:
		//		Returns an array of attribute names that are used to generate the identity. 
		//		For most stores, this is a single attribute, but for some complex stores
		//		such as RDB backed stores that use compound (multi-attribute) identifiers
		//		it can be more than one.  If the identity is not composed of attributes
		//		on the item, it will return null.  This function is intended to identify
		//		the attributes that comprise the identity so that so that during a render
		//		of all attributes, the UI can hide the the identity information if it 
		//		chooses.
		//	item:
		//		The item from the store from which to obtain the array of public attributes that 
		//		compose the identifier, if any.
		//	example:
		//	|	var itemId = store.getIdentity(kermit);
		//	|	var identifiers = store.getIdentityAttributes(itemId);
		//	|	assert(typeof identifiers === "array" || identifiers === null);
		throw new Error('Unimplemented API: dojo.data.api.Identity.getIdentityAttributes');
		return null; // string
	},


	fetchItemByIdentity: function(/* object */ keywordArgs){
		//	summary:
		//		Given the identity of an item, this method returns the item that has 
		//		that identity through the onItem callback.  Conforming implementations 
		//		should return null if there is no item with the given identity.  
		//		Implementations of fetchItemByIdentity() may sometimes return an item 
		//		from a local cache and may sometimes fetch an item from a remote server, 
		//
		// 	keywordArgs:
		//		An anonymous object that defines the item to locate and callbacks to invoke when the 
		//		item has been located and load has completed.  The format of the object is as follows:
		//		{
		//			identity: string|object,
		//			onItem: Function,
		//			onError: Function,
		//			scope: object
		//		}
		//	The *identity* parameter.
		//		The identity parameter is the identity of the item you wish to locate and load
		//		This attribute is required.  It should be a string or an object that toString() 
		//		can be called on.
		//		
		//	The *onItem* parameter.
		//		Function(item)
		//		The onItem parameter is the callback to invoke when the item has been loaded.  It takes only one
		//		parameter, the item located, or null if none found.
		//
		//	The *onError* parameter.
		//		Function(error)
		//		The onError parameter is the callback to invoke when the item load encountered an error.  It takes only one
		//		parameter, the error object
		//
		//	The *scope* parameter.
		//		If a scope object is provided, all of the callback functions (onItem, 
		//		onError, etc) will be invoked in the context of the scope object.
		//		In the body of the callback function, the value of the "this"
		//		keyword will be the scope object.   If no scope object is provided,
		//		the callback functions will be called in the context of dojo.global.
		//		For example, onItem.call(scope, item, request) vs. 
		//		onItem.call(dojo.global, item, request)
		if (!this.isItemLoaded(keywordArgs.item)) {
			throw new Error('Unimplemented API: dojo.data.api.Identity.fetchItemByIdentity');
		}
	}
});

}
