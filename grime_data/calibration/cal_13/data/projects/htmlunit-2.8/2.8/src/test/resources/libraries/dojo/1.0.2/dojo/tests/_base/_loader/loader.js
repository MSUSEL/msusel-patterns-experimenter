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
if(!dojo._hasResource["tests._base._loader.loader"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["tests._base._loader.loader"] = true;
dojo.provide("tests._base._loader.loader");

tests.register("tests._base._loader.loader", 
	[
		function baseUrl(t){
			var originalBaseUrl = djConfig["baseUrl"] || "./";

			t.assertEqual(originalBaseUrl, dojo.baseUrl);
		},
		
		function modulePaths(t){
			dojo.registerModulePath("mycoolmod", "../some/path/mycoolpath");
			dojo.registerModulePath("mycoolmod.widget", "http://some.domain.com/another/path/mycoolpath/widget");

			t.assertEqual("../some/path/mycoolpath/util", dojo._getModuleSymbols("mycoolmod.util").join("/"));
			t.assertEqual("http://some.domain.com/another/path/mycoolpath/widget", dojo._getModuleSymbols("mycoolmod.widget").join("/"));
			t.assertEqual("http://some.domain.com/another/path/mycoolpath/widget/thingy", dojo._getModuleSymbols("mycoolmod.widget.thingy").join("/"));
		},
		
		function moduleUrls(t){
			dojo.registerModulePath("mycoolmod", "some/path/mycoolpath");
			dojo.registerModulePath("mycoolmod2", "/some/path/mycoolpath2");
			dojo.registerModulePath("mycoolmod.widget", "http://some.domain.com/another/path/mycoolpath/widget");


			var basePrefix = dojo.baseUrl;
			//dojo._Uri will strip off "./" characters, so do the same here
			if(basePrefix == "./"){
				basePrefix = "";
			}
			
			t.assertEqual(basePrefix + "some/path/mycoolpath/my/favorite.html",
				dojo.moduleUrl("mycoolmod", "my/favorite.html").toString());
			t.assertEqual(basePrefix + "some/path/mycoolpath/my/favorite.html",
				dojo.moduleUrl("mycoolmod.my", "favorite.html").toString());

			t.assertEqual("/some/path/mycoolpath2/my/favorite.html",
				dojo.moduleUrl("mycoolmod2", "my/favorite.html").toString());
			t.assertEqual("/some/path/mycoolpath2/my/favorite.html",
				dojo.moduleUrl("mycoolmod2.my", "favorite.html").toString());

			t.assertEqual("http://some.domain.com/another/path/mycoolpath/widget/my/favorite.html",
				dojo.moduleUrl("mycoolmod.widget", "my/favorite.html").toString());
			t.assertEqual("http://some.domain.com/another/path/mycoolpath/widget/my/favorite.html",
				dojo.moduleUrl("mycoolmod.widget.my", "favorite.html").toString());
		}
	]
);

}
