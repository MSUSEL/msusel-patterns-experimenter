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
if(!dojo._hasResource["tests._base.Color"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests._base.Color"] = true;
dojo.provide("tests._base.Color");

(function(){
	var white  = dojo.colorFromString("white").toRgba();
	var maroon = dojo.colorFromString("maroon").toRgba();
	var verifyColor = function(t, source, expected){
		var color = new dojo.Color(source);
		t.is(expected, color.toRgba());
		dojo.forEach(color.toRgba(), function(n){ t.is("number", typeof(n)); });
	}

	doh.register("tests._base.Color", 
		[
			function testColor1(t){ verifyColor(t, "maroon", maroon); },
			function testColor2(t){ verifyColor(t, "white", white); },
			function testColor3(t){ verifyColor(t, "#fff", white); },
			function testColor4(t){ verifyColor(t, "#ffffff", white); },
			function testColor5(t){ verifyColor(t, "rgb(255,255,255)", white); },
			function testColor6(t){ verifyColor(t, "#800000", maroon); },
			function testColor7(t){ verifyColor(t, "rgb(128, 0, 0)", maroon); },
			function testColor8(t){ verifyColor(t, "rgba(128, 0, 0, 0.5)", [128, 0, 0, 0.5]); },
			function testColor9(t){ verifyColor(t, maroon, maroon); },
			function testColor10(t){ verifyColor(t, [1, 2, 3], [1, 2, 3, 1]); },
			function testColor11(t){ verifyColor(t, [1, 2, 3, 0.5], [1, 2, 3, 0.5]); },
			function testColor12(t){ verifyColor(t, dojo.blendColors(new dojo.Color("black"), new dojo.Color("white"), 0.5), [128, 128, 128, 1]); }
		]
	);
})();

}
