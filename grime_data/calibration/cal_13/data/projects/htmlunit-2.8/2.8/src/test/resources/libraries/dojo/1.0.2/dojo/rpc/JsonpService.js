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
if(!dojo._hasResource["dojo.rpc.JsonpService"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.rpc.JsonpService"] = true;
dojo.provide("dojo.rpc.JsonpService");
dojo.require("dojo.rpc.RpcService");
dojo.require("dojo.io.script");

dojo.declare("dojo.rpc.JsonpService", dojo.rpc.RpcService, {
	// summary:
	//	Generic JSONP service.  Minimally extends RpcService to allow 
	//	easy definition of nearly any JSONP style service. Example
	//	SMD files exist in dojox.data

	constructor: function(args, requiredArgs){
		if(this.required) {
			if(requiredArgs){
				dojo.mixin(this.required, requiredArgs);
			}

			dojo.forEach(this.required, function(req){
				if(req=="" || req==undefined){
					throw new Error("Required Service Argument not found: "+req); 
				}
			});
		}		
	},

	strictArgChecks: false,

	bind: function(method, parameters, deferredRequestHandler, url){
		//summary:
		//              JSONP bind method. Takes remote method, parameters,
		//              deferred, and a url, calls createRequest to make a JSON-RPC
		//              envelope and passes that off with bind.
		//      method: string
		//              The name of the method we are calling
		//      parameters: array
		//              The parameters we are passing off to the method
		//      deferredRequestHandler: deferred
		//              The Deferred object for this particular request

		var def = dojo.io.script.get({
			url: url||this.serviceUrl,
			callbackParamName: this.callbackParamName||"callback",
			content: this.createRequest(parameters),
			timeout: this.timeout,
			handleAs: "json",	
			preventCache: true
		});
		def.addCallbacks(this.resultCallback(deferredRequestHandler), this.errorCallback(deferredRequestHandler));
	},

	createRequest: function(parameters){
		// summary:
		//      create a JSONP req
		//      params: array
		//              The array of parameters for this request;

		var params = (dojo.isArrayLike(parameters) && parameters.length==1) ?
				parameters[0] : {};
		dojo.mixin(params,this.required);
		return params;
	}
});

}
