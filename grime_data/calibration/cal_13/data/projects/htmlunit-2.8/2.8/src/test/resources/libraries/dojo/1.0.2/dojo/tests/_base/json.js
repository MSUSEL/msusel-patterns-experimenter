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
if(!dojo._hasResource["tests._base.json"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests._base.json"] = true;
dojo.provide("tests._base.json");

tests.register("tests._base.json", 
	[
		//Not testing dojo.toJson() on its own since Rhino will output the object properties in a different order.
		//Still valid json, but just in a different order than the source string.

		// take a json-compatible object, convert it to a json string, then put it back into json.
		function toAndFromJson(t){
			var testObj = {a:"a", b:1, c:"c", d:"d", e:{e1:"e1", e2:2}, f:[1,2,3], g:"g",h:{h1:{h2:{h3:"h3"}}}};

			var mirrorObj = dojo.fromJson(dojo.toJson(testObj));
			t.assertEqual("a", mirrorObj.a);
			t.assertEqual(1, mirrorObj.b);
			t.assertEqual("c", mirrorObj.c);
			t.assertEqual("d", mirrorObj.d);
			t.assertEqual("e1", mirrorObj.e.e1);
			t.assertEqual(2, mirrorObj.e.e2);
			t.assertEqual(1, mirrorObj.f[0]);
			t.assertEqual(2, mirrorObj.f[1]);
			t.assertEqual(3, mirrorObj.f[2]);
			t.assertEqual("g", mirrorObj.g);
			t.assertEqual("h3", mirrorObj.h.h1.h2.h3);
		}
	]
);


}
