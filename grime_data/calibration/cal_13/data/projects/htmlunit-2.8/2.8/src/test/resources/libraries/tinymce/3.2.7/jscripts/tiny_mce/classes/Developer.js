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
(function() {
	var EditorManager = tinymce.EditorManager, each = tinymce.each, DOM = tinymce.DOM;

	/**
	 * This class patches in various development features. This class is only available for the dev version of TinyMCE.
	 */
	tinymce.create('static tinymce.Developer', {
		_piggyBack : function() {
			var t = this, em = tinymce.EditorManager, lo = false;

			// Makes sure that XML language pack is used instead of JS files
			t._runBefore(em, 'init', function(s) {
				var par = new tinymce.xml.Parser({async : false}), lng = s.language || "en", i18n = tinymce.EditorManager.i18n, sl = tinymce.ScriptLoader;

				if (!s.translate_mode)
					return;

				if (lo)
					return;

				lo = true;

				// Common language loaded
				sl.markDone(tinymce.baseURL + '/langs/' + lng + '.js');

				// Theme languages loaded
				sl.markDone(tinymce.baseURL + '/themes/simple/langs/' + lng + '.js');
				sl.markDone(tinymce.baseURL + '/themes/advanced/langs/' + lng + '.js');

				// All plugin packs loaded
				each(s.plugins.split(','), function(p) {
					sl.markDone(tinymce.baseURL + '/plugins/' + p + '/langs/' + lng + '.js');
				});

				// Load XML language pack
				par.load(tinymce.baseURL + '/langs/' + lng + '.xml', function(doc, ex) {
					var c;

					if (!doc) {
						alert(ex.message);
						return;
					}

					if (doc.documentElement.nodeName == 'parsererror') {
						alert('Parse error!!');
						return;
					}

					c = doc.getElementsByTagName('language')[0].getAttribute("code");

					each(doc.getElementsByTagName('group'), function(g) {
						var gn = g.getAttribute("target"), o = {};

						// Build object from XML items
						each(g.getElementsByTagName('item'), function(it) {
							var itn = it.getAttribute("name");

							if (gn == "common")
								i18n[c + '.' + itn] = par.getText(it);
							else
								i18n[c + '.' + gn + "." + itn] = par.getText(it);
						});
					});
				}, {
					async : false
				});
			});
		},

		_runBefore : function(o, n, f) {
			var e = o[n];

			o[n] = function() {
				var s = f.apply(o, arguments);

				if (s !== false)
					return e.apply(o, arguments);
			};
		}
	});

	tinymce.Developer._piggyBack();
})();

