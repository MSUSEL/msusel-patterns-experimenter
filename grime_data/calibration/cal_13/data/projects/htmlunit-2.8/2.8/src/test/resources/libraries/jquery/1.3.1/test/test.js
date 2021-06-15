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
load( "build/js/writeFile.js", "build/js/parse.js" );

function addParams(name, params) {
	if(params.length > 0) {
		name += "(";
		for ( var i = 0; i < params.length; i++) {
			name += params[i].type + ", ";
		}
		return name.substring(0, name.length - 2) + ")";
	} else {
		return name + "()";
	}
}
function addTestWrapper(name, test) {
	return 'test("' + name + '", function() {\n' + test + '\n});';
}

var dir = arguments[1];
var jq = parse( read(arguments[0]) );

var testFile = [];

String.prototype.decode = function() {
	return this.replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&amp;/g, "&");
};

for ( var i = 0; i < jq.length; i++ ) {
	if ( jq[i].tests.length > 0 ) {
		var method = jq[i];
		var name = addParams(method.name, method.params);
		for(var j = 0; j < method.tests.length; j++) {
			if(j > 0) {
				name += "x";
			}
			testFile[testFile.length] = addTestWrapper(name, method.tests[j].decode()) + "\n";
		}
	}
}

var indexFile = readFile( "build/test/index.html" );
writeFile( dir + "/index.html", indexFile.replace( /{TESTS}/g, testFile.join("\n") ) );
