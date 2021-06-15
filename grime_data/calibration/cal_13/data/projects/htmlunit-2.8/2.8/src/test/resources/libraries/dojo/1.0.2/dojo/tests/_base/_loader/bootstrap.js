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
if(!dojo._hasResource["tests._base._loader.bootstrap"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests._base._loader.bootstrap"] = true;
dojo.provide("tests._base._loader.bootstrap");

tests.register("tests._base._loader.bootstrap", 
	[

		function hasConsole(t){
			t.assertTrue("console" in dojo.global);
			t.assertTrue("assert" in console);
			t.assertEqual("function", typeof console.assert);
		},

		function hasDjConfig(t){
			t.assertTrue("djConfig" in dojo.global);
		},

		{
			name: "getObject",
			setUp: function(){
				//Set an object in global scope.
				dojo.global.globalValue = {
					color: "blue",
					size: 20
				};
				
				//Set up an object in a specific scope.
				this.foo = {
					bar: {
						color: "red",
						size: 100
					}
				};
			},
			runTest: function(t){
				//Test for existing object using global as root path.
				var globalVar = dojo.getObject("globalValue");
				t.is("object", (typeof globalVar));
				t.assertEqual("blue", globalVar.color);
				t.assertEqual(20, globalVar.size);
				t.assertEqual("blue", dojo.getObject("globalValue.color"));
				
				//Test for non-existent object using global as root path.
				//Then create it.
				t.assertFalse(dojo.getObject("something.thatisNew"));
				t.assertTrue(typeof(dojo.getObject("something.thatisNew", true)) == "object");
				
				//Test for existing object using another object as root path.
				var scopedVar = dojo.getObject("foo.bar", false, this);
				t.assertTrue(typeof(scopedVar) == "object");
				t.assertEqual("red", scopedVar.color);
				t.assertEqual(100, scopedVar.size);
				t.assertEqual("red", dojo.getObject("foo.bar.color", true, this));
				
				//Test for existing object using another object as root path.
				//Then create it.
				t.assertFalse(dojo.getObject("something.thatisNew", false, this));
				t.assertTrue(typeof(dojo.getObject("something.thatisNew", true, this)) == "object");
			},
			tearDown: function(){
				//Clean up global object that should not exist if
				//the test is re-run.
				try{
					delete dojo.global.something;
					delete this.something;
				}catch(e){}
			}
		},
		
		{
			name: "exists",
			setUp: function(){
				this.foo = {
					bar: {}
				};
			},
			runTest: function(t){
				t.assertTrue(dojo.exists("foo.bar", this));
				t.assertFalse(dojo.exists("foo.bar"));
			}
		},

		function evalWorks(t){
			t.assertTrue(dojo.eval("(true)"));
			t.assertFalse(dojo.eval("(false)"));
		}
	]
);

}
