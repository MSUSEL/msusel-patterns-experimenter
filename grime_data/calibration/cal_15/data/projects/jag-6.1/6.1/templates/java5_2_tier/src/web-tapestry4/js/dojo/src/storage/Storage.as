/**
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
import flash.external.ExternalInterface;

class Storage {
	static var app : Storage;
	var store: SharedObject;
	static var started: Boolean = false;
	
	public function Storage(){
		ExternalInterface.addCallback("set", null, set);
		ExternalInterface.addCallback("get", null, get);
		ExternalInterface.addCallback("free", null, free);
	}

	public function set(key, value, namespace){
		var primeForReHide = false;
		store = SharedObject.getLocal(namespace);
		store.onStatus = function(status){
			// ExternalInterface.call("alert", status.code == "SharedObject.Flush.Failed");
			// ExternalInterface.call("alert", status.code == "SharedObject.Flush.Success");
			if(primeForReHide){
				primeForReHide = false;
				ExternalInterface.call("dojo.storage.provider.hideStore");
			}
		}
		store.data[key] = value;
		var ret = store.flush();
		if(typeof ret == "string"){
			ExternalInterface.call("dojo.storage.provider.unHideStore");
			primeForReHide = true;
		}
		return store.getSize(namespace);
	}

	public function get(key, namespace){
		store = SharedObject.getLocal(namespace);
		return store.data[key];
	}

	public function free(namespace){
		return SharedObject.getDiskUsage(namespace);
	}

	static function main(mc){
		app = new Storage();
		if(!started){
			ExternalInterface.call("dojo.storage.provider.storageOnLoad");
			started = true;
		}
	}
}

