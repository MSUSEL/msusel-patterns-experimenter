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
if(!dojo._hasResource["tests._base.array"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests._base.array"] = true;
dojo.provide("tests._base.array");

tests.register("tests._base.array", 
	[
		function testIndexOf(t){
			var foo = [128, 256, 512];
			var bar = ["aaa", "bbb", "ccc"];
			
			t.assertTrue(dojo.indexOf([45, 56, 85], 56) == 1);
			t.assertTrue(dojo.indexOf([Number, String, Date], String) == 1);
			t.assertTrue(dojo.indexOf(foo, foo[1]) == 1);
			t.assertTrue(dojo.indexOf(foo, foo[2]) == 2);
			t.assertTrue(dojo.indexOf(bar, bar[1]) == 1);
			t.assertTrue(dojo.indexOf(bar, bar[2]) == 2);
			
			foo.push(bar);
			t.assertTrue(dojo.indexOf(foo, bar) == 3);
		},

		function testIndexOfFromIndex(t){
			var foo = [128, 256, 512];
			var bar = ["aaa", "bbb", "ccc"];
			
			// FIXME: what happens w/ negative indexes?
			t.assertEqual(-1, dojo.indexOf([45, 56, 85], 56, 2));
			t.assertEqual(1, dojo.indexOf([45, 56, 85], 56, 1));
		},

		function testLastIndexOf(t){
			var foo = [128, 256, 512];
			var bar = ["aaa", "bbb", "aaa", "ccc"];
			
			t.assertTrue(dojo.indexOf([45, 56, 85], 56) == 1);
			t.assertTrue(dojo.indexOf([Number, String, Date], String) == 1);
			t.assertTrue(dojo.lastIndexOf(foo, foo[1]) == 1);
			t.assertTrue(dojo.lastIndexOf(foo, foo[2]) == 2);
			t.assertTrue(dojo.lastIndexOf(bar, bar[1]) == 1);
			t.assertTrue(dojo.lastIndexOf(bar, bar[2]) == 2);
			t.assertTrue(dojo.lastIndexOf(bar, bar[0]) == 2);
		},

		function testLastIndexOfFromIndex(t){
			// FIXME: what happens w/ negative indexes?
			t.assertEqual(1, dojo.lastIndexOf([45, 56, 85], 56, 1));
			t.assertEqual(-1, dojo.lastIndexOf([45, 56, 85], 85, 1));
		},

		function testForEach(t){
			var foo = [128, "bbb", 512];
			dojo.forEach(foo, function(elt, idx, array){
				switch(idx){
					case 0: t.assertEqual(128, elt); break;
					case 1: t.assertEqual("bbb", elt); break;
					case 2: t.assertEqual(512, elt); break;
					default: t.assertTrue(false);
				}
			});

			var noException = true;
			try{
				dojo.forEach(undefined, function(){});
			}catch(e){
				noException = false;
			}
			t.assertTrue(noException);
		},

		function testForEach_str(t){
			var bar = 'abc';
			dojo.forEach(bar, function(elt, idx, array){
				switch(idx){
					case 0: t.assertEqual("a", elt); break;
					case 1: t.assertEqual("b", elt); break;
					case 2: t.assertEqual("c", elt); break;
					default: t.assertTrue(false);
				}
			});
		},
		// FIXME: test forEach w/ a NodeList()?

		function testEvery(t){
			var foo = [128, "bbb", 512];

			t.assertTrue(
				dojo.every(foo, function(elt, idx, array){
					t.assertEqual(Array, array.constructor);
					t.assertTrue(dojo.isArray(array));
					t.assertTrue(typeof idx == "number");
					if(idx == 1){ t.assertEqual("bbb" , elt); }
					return true;
				})
			);

			t.assertTrue(
				dojo.every(foo, function(elt, idx, array){
					switch(idx){
						case 0: t.assertEqual(128, elt); return true;
						case 1: t.assertEqual("bbb", elt); return true;
						case 2: t.assertEqual(512, elt); return true;
						default: return false;
					}
				})
			);

			t.assertFalse(
				dojo.every(foo, function(elt, idx, array){
					switch(idx){
						case 0: t.assertEqual(128, elt); return true;
						case 1: t.assertEqual("bbb", elt); return true;
						case 2: t.assertEqual(512, elt); return false;
						default: return true;
					}
				})
			);

		},

		function testEvery_str(t){
			var bar = 'abc';
			t.assertTrue(
				dojo.every(bar, function(elt, idx, array){
					switch(idx){
						case 0: t.assertEqual("a", elt); return true;
						case 1: t.assertEqual("b", elt); return true;
						case 2: t.assertEqual("c", elt); return true;
						default: return false;
					}
				})
			);

			t.assertFalse(
				dojo.every(bar, function(elt, idx, array){
					switch(idx){
						case 0: t.assertEqual("a", elt); return true;
						case 1: t.assertEqual("b", elt); return true;
						case 2: t.assertEqual("c", elt); return false;
						default: return true;
					}
				})
			);
		},
		// FIXME: test NodeList for every()?

		function testSome(t){
			var foo = [128, "bbb", 512];
			t.assertTrue(
				dojo.some(foo, function(elt, idx, array){
					t.assertEqual(3, array.length);
					return true;
				})
			);

			t.assertTrue(
				dojo.some(foo, function(elt, idx, array){
					if(idx < 1){ return true; }
					return false;
				})
			);

			t.assertFalse(
				dojo.some(foo, function(elt, idx, array){
					return false;
				})
			);

			t.assertTrue(
				dojo.some(foo, function(elt, idx, array){
					t.assertEqual(Array, array.constructor);
					t.assertTrue(dojo.isArray(array));
					t.assertTrue(typeof idx == "number");
					if(idx == 1){ t.assertEqual("bbb" , elt); }
					return true;
				})
			);
		},

		function testSome_str(t){
			var bar = 'abc';
			t.assertTrue(
				dojo.some(bar, function(elt, idx, array){
					t.assertEqual(3, array.length);
					switch(idx){
						case 0: t.assertEqual("a", elt); return true;
						case 1: t.assertEqual("b", elt); return true;
						case 2: t.assertEqual("c", elt); return true;
						default: return false;
					}
				})
			);

			t.assertTrue(
				dojo.some(bar, function(elt, idx, array){
					switch(idx){
						case 0: t.assertEqual("a", elt); return true;
						case 1: t.assertEqual("b", elt); return true;
						case 2: t.assertEqual("c", elt); return false;
						default: return true;
					}
				})
			);

			t.assertFalse(
				dojo.some(bar, function(elt, idx, array){
					return false;
				})
			);
		},
		// FIXME: need to add scoping tests for all of these!!!

		function testFilter(t){
			var foo = ["foo", "bar", 10];

			t.assertEqual(["foo"],
				dojo.filter(foo, function(elt, idx, array){
					return idx < 1;
				})
			);

			t.assertEqual(["foo"],
				dojo.filter(foo, function(elt, idx, array){
					return elt == "foo";
				})
			);

			t.assertEqual([],
				dojo.filter(foo, function(elt, idx, array){
					return false;
				})
			);

			t.assertEqual([10],
				dojo.filter(foo, function(elt, idx, array){
					return typeof elt == "number";
				})
			);
		},

		function testFilter_str(t){
			var foo = "thinger blah blah blah";
			t.assertEqual(["t", "h", "i"],
				dojo.filter(foo, function(elt, idx, array){
					return idx < 3;
				})
			);

			t.assertEqual([],
				dojo.filter(foo, function(elt, idx, array){
					return false;
				})
			);
		},

		function testMap(t){
			t.assertEqual([],
				dojo.map([], function(){ return true; })
			);

			t.assertEqual([1, 2, 3],
				dojo.map(["cat", "dog", "mouse"], function(elt, idx, array){
					return idx+1;
				})
			);
		}
	]
);


}
