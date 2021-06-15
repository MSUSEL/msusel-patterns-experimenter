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

dojo.require("dojo.storage");
dojo.require("dojo.json");
dojo.provide("dojo.storage.dashboard");

dojo.storage.dashboard.StorageProvider = function(){
	this.initialized = false;
}

dojo.inherits(dojo.storage.dashboard.StorageProvider, dojo.storage.StorageProvider);

dojo.lang.extend(dojo.storage.dashboard.StorageProvider, {
	storageOnLoad: function(){
		this.initialized = true;
	},

	set: function(key, value, ns){
		if (ns && widget.system){
			widget.system("/bin/mkdir " + ns);
			var system = widget.system("/bin/echo " + value + " >" + ns + "/" + key);
			if(system.errorString){
				return false;
			}
			return true;
		}

		return widget.setPreferenceForKey(dojo.json.serialize(value), key);
	},

	get: function(key, ns){
		if (ns && widget.system) {
			var system = widget.system("/bin/cat " + ns + "/" + key);
			if(system.errorString){
				return "";
			}
			return system.outputString;
		}

		return dojo.json.evalJSON(widget.preferenceForKey(key));
	}
});

dojo.storage.setProvider(new dojo.storage.dashboard.StorageProvider());