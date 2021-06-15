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
if(!dojo._hasResource["tests.AdapterRegistry"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests.AdapterRegistry"] = true;
dojo.provide("tests.AdapterRegistry");
dojo.require("dojo.AdapterRegistry");

doh.register("tests.AdapterRegistry", 
	[
		function ctor(t){
			var taa = new dojo.AdapterRegistry();
			t.is(0, taa.pairs.length);
			t.f(taa.returnWrappers);

			var taa = new dojo.AdapterRegistry(true);
			t.t(taa.returnWrappers);
		},

		function register(t){
			var taa = new dojo.AdapterRegistry();
			taa.register("blah", 
				function(str){ return str == "blah"; },
				function(){ return "blah"; }
			);
			t.is(1, taa.pairs.length);
			t.is("blah", taa.pairs[0][0]);

			taa.register("thinger");
			taa.register("prepend", null, null, true, true);
			t.is("prepend", taa.pairs[0][0]);
			t.t(taa.pairs[0][3]);
		},

		/*
		function match(t){
		},
		*/

		function noMatch(t){
			var taa = new dojo.AdapterRegistry();
			var threw = false;
			try{
				taa.match("blah");
			}catch(e){
				threw = true;
			}
			t.t(threw);
		},

		function returnWrappers(t){
			var taa = new dojo.AdapterRegistry();
			taa.register("blah", 
				function(str){ return str == "blah"; },
				function(){ return "blah"; }
			);
			t.is("blah", taa.match("blah"));

			taa.returnWrappers = true;
			t.is("blah", taa.match("blah")());
		},

		function unregister(t){
			var taa = new dojo.AdapterRegistry();
			taa.register("blah", 
				function(str){ return str == "blah"; },
				function(){ return "blah"; }
			);
			taa.register("thinger");
			taa.register("prepend", null, null, true, true);
			taa.unregister("prepend");
			t.is(2, taa.pairs.length);
			t.is("blah", taa.pairs[0][0]);
		}
	]
);

}
