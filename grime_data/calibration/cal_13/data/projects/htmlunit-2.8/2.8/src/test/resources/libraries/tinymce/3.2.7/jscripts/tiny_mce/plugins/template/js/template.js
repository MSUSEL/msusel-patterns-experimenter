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
tinyMCEPopup.requireLangPack();

var TemplateDialog = {
	preInit : function() {
		var url = tinyMCEPopup.getParam("template_external_list_url");

		if (url != null)
			document.write('<sc'+'ript language="javascript" type="text/javascript" src="' + tinyMCEPopup.editor.documentBaseURI.toAbsolute(url) + '"></sc'+'ript>');
	},

	init : function() {
		var ed = tinyMCEPopup.editor, tsrc, sel, x, u;

 		tsrc = ed.getParam("template_templates", false);
 		sel = document.getElementById('tpath');

		// Setup external template list
		if (!tsrc && typeof(tinyMCETemplateList) != 'undefined') {
			for (x=0, tsrc = []; x<tinyMCETemplateList.length; x++)
				tsrc.push({title : tinyMCETemplateList[x][0], src : tinyMCETemplateList[x][1], description : tinyMCETemplateList[x][2]});
		}

		for (x=0; x<tsrc.length; x++)
			sel.options[sel.options.length] = new Option(tsrc[x].title, tinyMCEPopup.editor.documentBaseURI.toAbsolute(tsrc[x].src));

		this.resize();
		this.tsrc = tsrc;
	},

	resize : function() {
		var w, h, e;

		if (!self.innerWidth) {
			w = document.body.clientWidth - 50;
			h = document.body.clientHeight - 160;
		} else {
			w = self.innerWidth - 50;
			h = self.innerHeight - 170;
		}

		e = document.getElementById('templatesrc');

		if (e) {
			e.style.height = Math.abs(h) + 'px';
			e.style.width  = Math.abs(w - 5) + 'px';
		}
	},

	loadCSSFiles : function(d) {
		var ed = tinyMCEPopup.editor;

		tinymce.each(ed.getParam("content_css", '').split(','), function(u) {
			d.write('<link href="' + ed.documentBaseURI.toAbsolute(u) + '" rel="stylesheet" type="text/css" />');
		});
	},

	selectTemplate : function(u, ti) {
		var d = window.frames['templatesrc'].document, x, tsrc = this.tsrc;

		if (!u)
			return;

		d.body.innerHTML = this.templateHTML = this.getFileContents(u);

		for (x=0; x<tsrc.length; x++) {
			if (tsrc[x].title == ti)
				document.getElementById('tmpldesc').innerHTML = tsrc[x].description || '';
		}
	},

 	insert : function() {
		tinyMCEPopup.execCommand('mceInsertTemplate', false, {
			content : this.templateHTML,
			selection : tinyMCEPopup.editor.selection.getContent()
		});

		tinyMCEPopup.close();
	},

	getFileContents : function(u) {
		var x, d, t = 'text/plain';

		function g(s) {
			x = 0;

			try {
				x = new ActiveXObject(s);
			} catch (s) {
			}

			return x;
		};

		x = window.ActiveXObject ? g('Msxml2.XMLHTTP') || g('Microsoft.XMLHTTP') : new XMLHttpRequest();

		// Synchronous AJAX load file
		x.overrideMimeType && x.overrideMimeType(t);
		x.open("GET", u, false);
		x.send(null);

		return x.responseText;
	}
};

TemplateDialog.preInit();
tinyMCEPopup.onInit.add(TemplateDialog.init, TemplateDialog);
