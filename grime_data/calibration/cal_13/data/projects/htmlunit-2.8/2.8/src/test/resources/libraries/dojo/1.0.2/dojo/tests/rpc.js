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
if(!dojo._hasResource["tests.rpc"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests.rpc"] = true;
dojo.provide("tests.rpc");

dojo.require("dojo.rpc.RpcService");
dojo.require("dojo.rpc.JsonService");
dojo.require("dojo.rpc.JsonpService");

doh.register("tests.rpc", 
	[ 

		{
			name: "JsonRPC-EchoTest",
			timeout: 2000,
			setUp: function(){

				var testSmd = {
					serviceURL:"../../dojo/tests/resources/test_JsonRPCMediator.php",
					methods:[
						{
							name:"myecho",
							parameters:[
								{
									name:"somestring",
									type:"STRING"
								}
							]
						}
					]	
				}
			
				this.svc = new dojo.rpc.JsonService(testSmd);
			},
			runTest: function(){
				var d = new doh.Deferred();
				var td = this.svc.myecho("RPC TEST");

				if (window.location.protocol=="file:") {
					var err= new Error("This Test requires a webserver and PHP and will fail intentionally if loaded from file://");
					d.errback(err);
					return d;
				}

				td.addCallbacks(function(result) {
					if(result=="<P>RPC TEST</P>"){
						return true;
					}else{
						return new Error("JsonRpc-EchoTest test failed, resultant content didn't match");
					}
				}, function(result){
					return new Error(result);
				});

				td.addBoth(d, "callback");

				return d;
			}

		},

		{
			name: "JsonRPC-EmptyParamTest",
			timeout: 2000,
			setUp: function(){
				var testSmd={
					serviceURL:"../../dojo/tests/resources/test_JsonRPCMediator.php",
					methods:[ { name:"contentB" } ]	
				}
			
				this.svc = new dojo.rpc.JsonService(testSmd);
			},
			runTest: function(){
				var d = new doh.Deferred();
				var td = this.svc.contentB();

				if (window.location.protocol=="file:") {
					var err= new Error("This Test requires a webserver and PHP and will fail intentionally if loaded from file://");
					d.errback(err);
					return d;
				}

				td.addCallbacks(function(result){
					if(result=="<P>Content B</P>"){
						return true;
					}else{
						return new Error("JsonRpc-EmpytParamTest test failed, resultant content didn't match");
					}
				}, function(result){
					return new Error(result);
				});

				td.addBoth(d, "callback");

				return d;
			}
		},

		{
			name: "JsonRPC_SMD_Loading_test",
			setUp: function(){
				this.svc = new dojo.rpc.JsonService("../../dojo/tests/resources/testClass.smd");
			},
			runTest: function(){

				if (this.svc.objectName="testClass") {
					return true;
				} else {
					return new Error("Error loading and/or parsing an smd file");
				}
			}
		},

		{
			name: "JsonP_test",
			timeout: 10000,
			setUp: function(){
				this.svc = new dojo.rpc.JsonpService(dojo.moduleUrl("dojox.rpc","yahoo.smd"), {appid: "foo"});
			},
			runTest: function(){
				var d = new doh.Deferred();

				if (window.location.protocol=="file:") {
					var err= new Error("This Test requires a webserver and will fail intentionally if loaded from file://");
					d.errback(err);
					return d;
				}

				var td = this.svc.webSearch({query:"dojotoolkit"});

				td.addCallbacks(function(result){
					return true;
					if (result["ResultSet"]["Result"][0]["DisplayUrl"]=="dojotoolkit.org/") {
						return true;
					}else{
						return new Error("JsonRpc_SMD_Loading_Test failed, resultant content didn't match");
					}
				}, function(result){
					return new Error(result);
				});

				td.addBoth(d, "callback");

				return d;
			}
		}
	]
);



}
