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
Script: Browser.js
	Specs for Browser.js

License:
	MIT-style license.
*/

describe('$exec', {

	'should evaluate on global scope': function(){
		$exec.call($exec, 'var execSpec = 42');
		value_of(window.execSpec).should_be(42);
	},

	'should return the evaluated script': function(){
		value_of($exec('$empty();')).should_be('$empty();');
	}

});

describe('Document', {

	'should hold the parent window': function(){
		value_of(document.window).should_be(window);
	},

	'should hold the head element': function(){
		value_of(document.head.tagName.toLowerCase()).should_be('head');
	}

});

describe('Window', {

	'should set the Element prototype': function(){
		value_of($defined(window.Element.prototype)).should_be_true();
	}

});