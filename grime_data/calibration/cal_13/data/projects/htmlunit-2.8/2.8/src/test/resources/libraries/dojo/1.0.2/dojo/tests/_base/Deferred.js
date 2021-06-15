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
if(!dojo._hasResource["tests._base.Deferred"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests._base.Deferred"] = true;
dojo.provide("tests._base.Deferred");

doh.register("tests._base.Deferred", 
	[

		function callback(t){
			var nd = new dojo.Deferred();
			var cnt = 0;
			nd.addCallback(function(res){
				doh.debug("debug from dojo.Deferred callback");
				return res;
			});
			nd.addCallback(function(res){
				// t.debug("val:", res);
				cnt+=res;
				return cnt;
			});
			nd.callback(5);
			// t.debug("cnt:", cnt);
			t.assertEqual(cnt, 5);
		},

		function errback(t){
			var nd = new dojo.Deferred();
			var cnt = 0;
			nd.addErrback(function(val){
				return ++cnt;
			});
			nd.errback();
			t.assertEqual(cnt, 1);
		},

		function callbackTwice(t){
			var nd = new dojo.Deferred();
			var cnt = 0;
			nd.addCallback(function(res){
				return ++cnt;
			});
			nd.callback();
			t.assertEqual(cnt, 1);
			var thrown = false;
			try{
				nd.callback();
			}catch(e){
				thrown = true;
			}
			t.assertTrue(thrown);
		},

		function addBoth(t){
			var nd = new dojo.Deferred();
			var cnt = 0;
			nd.addBoth(function(res){
				return ++cnt;
			});
			nd.callback();
			t.assertEqual(cnt, 1);

			// nd.callback();
			// t.debug(cnt);
			// t.assertEqual(cnt, 1);
		}
	]
);

}
