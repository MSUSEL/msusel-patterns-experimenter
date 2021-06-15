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
if(!dojo._hasResource["tests.string"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests.string"] = true;
dojo.provide("tests.string");

dojo.require("dojo.string");

tests.register("tests.string", 
	[
		function test_string_pad(t){
			t.is("00001", dojo.string.pad("1", 5));
			t.is("000001", dojo.string.pad("000001", 5));
			t.is("10000", dojo.string.pad("1", 5, null, true));
		},

		function test_string_substitute(t){
			t.is("File 'foo.html' is not found in directory '/temp'.", dojo.string.substitute("File '${0}' is not found in directory '${1}'.", ["foo.html","/temp"]));
			t.is("File 'foo.html' is not found in directory '/temp'.", dojo.string.substitute("File '${name}' is not found in directory '${info.dir}'.", {name: "foo.html", info: {dir: "/temp"}}));
			// Verify that an error is thrown!
			t.assertError(Error, dojo.string, "substitute", ["${x}", {y:1}]);
		},
		
		function test_string_trim(t){
			t.is("astoria", dojo.string.trim("   \f\n\r\t      astoria           "));
			t.is("astoria", dojo.string.trim("astoria                            "));
			t.is("astoria", dojo.string.trim("                            astoria"));
			t.is("astoria", dojo.string.trim("astoria"));
		}
	]
);

}
