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
	tinymce.create('tinymce.plugins.SearchReplacePlugin', {
		init : function(ed, url) {
			function open(m) {
				ed.windowManager.open({
					file : url + '/searchreplace.htm',
					width : 420 + parseInt(ed.getLang('searchreplace.delta_width', 0)),
					height : 160 + parseInt(ed.getLang('searchreplace.delta_height', 0)),
					inline : 1,
					auto_focus : 0
				}, {
					mode : m,
					search_string : ed.selection.getContent({format : 'text'}),
					plugin_url : url
				});
			};

			// Register commands
			ed.addCommand('mceSearch', function() {
				open('search');
			});

			ed.addCommand('mceReplace', function() {
				open('replace');
			});

			// Register buttons
			ed.addButton('search', {title : 'searchreplace.search_desc', cmd : 'mceSearch'});
			ed.addButton('replace', {title : 'searchreplace.replace_desc', cmd : 'mceReplace'});

			ed.addShortcut('ctrl+f', 'searchreplace.search_desc', 'mceSearch');
		},

		getInfo : function() {
			return {
				longname : 'Search/Replace',
				author : 'Moxiecode Systems AB',
				authorurl : 'http://tinymce.moxiecode.com',
				infourl : 'http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/searchreplace',
				version : tinymce.majorVersion + "." + tinymce.minorVersion
			};
		}
	});

	// Register plugin
	tinymce.PluginManager.add('searchreplace', tinymce.plugins.SearchReplacePlugin);
})();