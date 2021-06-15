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

dojo.provide("dojo.rpc.JsonService");
dojo.require("dojo.rpc.RpcService");
dojo.require("dojo.io.*");
dojo.require("dojo.json");
dojo.require("dojo.lang");

dojo.rpc.JsonService = function(args){
	// passing just the URL isn't terribly useful. It's expected that at
	// various times folks will want to specify:
	//	- just the serviceUrl (for use w/ remoteCall())
	//	- the text of the SMD to evaluate
	// 	- a raw SMD object
	//	- the SMD URL
	if(args){
		if(dojo.lang.isString(args)){
			// we assume it's an SMD file to be processed, since this was the
			// earlier function signature

			// FIXME: also accept dojo.uri.Uri objects?
			this.connect(args);
		}else{
			// otherwise we assume it's an arguments object with the following
			// (optional) properties:
			//	- serviceUrl
			//	- strictArgChecks
			//	- smdUrl
			//	- smdStr
			//	- smdObj
			if(args["smdUrl"]){
				this.connect(args.smdUrl);
			}
			if(args["smdStr"]){
				this.processSmd(dj_eval("("+args.smdStr+")"));
			}
			if(args["smdObj"]){
				this.processSmd(args.smdObj);
			}
			if(args["serviceUrl"]){
				this.serviceUrl = args.serviceUrl;
			}
			if(args["strictArgChecks"]){
				this.strictArgChecks = args.strictArgChecks;
			}
		}
	}
}

dojo.inherits(dojo.rpc.JsonService, dojo.rpc.RpcService);

dojo.lang.extend(dojo.rpc.JsonService, {

	bustCache: false,

	lastSubmissionId: 0,

	callRemote: function(method, params){
		var deferred = new dojo.rpc.Deferred();
		this.bind(method, params, deferred);
		return deferred;
	},

	bind: function(method, parameters, deferredRequestHandler){
		dojo.io.bind({
			url: this.serviceUrl,
			postContent: this.createRequest(method, parameters),
			method: "POST",
			mimetype: "text/json",
			load: this.resultCallback(deferredRequestHandler),
			preventCache:this.bustCache 
		});
	},

	createRequest: function(method, params){
		var req = { "params": params, "method": method, "id": this.lastSubmissionId++ };
		var data = dojo.json.serialize(req);
		dojo.debug("JsonService: JSON-RPC Request: " + data);
		return data;
	},

	parseResults: function(obj){
		if(obj["result"]){
			return obj["result"];
		}else{
			return obj;
		}
	}
});
