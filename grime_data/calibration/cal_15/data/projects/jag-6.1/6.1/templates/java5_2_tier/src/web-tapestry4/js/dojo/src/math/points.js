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

dojo.provide("dojo.math.points");
dojo.require("dojo.math");

// TODO: add a Point class?
dojo.math.points = {
	translate: function(a, b) {
		if( a.length != b.length ) {
			dojo.raise("dojo.math.translate: points not same size (a:[" + a + "], b:[" + b + "])");
		}
		var c = new Array(a.length);
		for(var i = 0; i < a.length; i++) {
			c[i] = a[i] + b[i];
		}
		return c;
	},

	midpoint: function(a, b) {
		if( a.length != b.length ) {
			dojo.raise("dojo.math.midpoint: points not same size (a:[" + a + "], b:[" + b + "])");
		}
		var c = new Array(a.length);
		for(var i = 0; i < a.length; i++) {
			c[i] = (a[i] + b[i]) / 2;
		}
		return c;
	},

	invert: function(a) {
		var b = new Array(a.length);
		for(var i = 0; i < a.length; i++) { b[i] = -a[i]; }
		return b;
	},

	distance: function(a, b) {
		return Math.sqrt(Math.pow(b[0]-a[0], 2) + Math.pow(b[1]-a[1], 2));
	}
};
