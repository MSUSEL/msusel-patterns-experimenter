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
if(!dojo._hasResource["dojo._base._loader.loader_debug"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo._base._loader.loader_debug"] = true;
dojo.provide("dojo._base._loader.loader_debug");

//Override dojo.provide, so we can trigger the next
//script tag for the next local module. We can only add one
//at a time because there are browsers that execute script tags
//in the order that the code is received, and not in the DOM order.
dojo.nonDebugProvide = dojo.provide;

dojo.provide = function(resourceName){
	var dbgQueue = dojo["_xdDebugQueue"];
	if(dbgQueue && dbgQueue.length > 0 && resourceName == dbgQueue["currentResourceName"]){
		//Set a timeout so the module can be executed into existence. Normally the
		//dojo.provide call in a module is the first line. Don't want to risk attaching
		//another script tag until the current one finishes executing.
		window.setTimeout("dojo._xdDebugFileLoaded('" + resourceName + "')", 1);
	}

	return dojo.nonDebugProvide.apply(dojo, arguments);
}

dojo._xdDebugFileLoaded = function(resourceName){
	var dbgQueue = this._xdDebugQueue;
	
	if(resourceName && resourceName == dbgQueue.currentResourceName){
		dbgQueue.shift();
	}

	if(dbgQueue.length == 0){
		dbgQueue.currentResourceName = null;
		this._xdNotifyLoaded();
	}else{
		dbgQueue.currentResourceName = dbgQueue[0].resourceName;
		var element = document.createElement("script");
		element.type = "text/javascript";
		element.src = dbgQueue[0].resourcePath;
		document.getElementsByTagName("head")[0].appendChild(element);
	}		
}

}
