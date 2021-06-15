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
Script: Builder.js
	Automatically includes MooTools files right from the project folder.

License:
	MIT-style license.

Note:
	If you use this script in your own page, you must be out of your mind.
*/

var Builder = {

	root: '../',

	paths: {
		source: 'Source',
		specs: 'Specs'
	},

	included: {
		source: {},
		specs: {},
		docs: {}
	},

	scripts: {
		source: {
			'Core'      : ['Core', 'Browser'],
			'Native'    : ['Array', 'Function', 'Number', 'String', 'Hash', 'Event'],
			'Class'     : ['Class', 'Class.Extras'],
			'Element'   : ['Element', 'Element.Event', 'Element.Style', 'Element.Dimensions'],
			'Utilities' : ['Selectors', 'DomReady', 'JSON', 'Cookie', 'Swiff'],
			'Fx'        : ['Fx', 'Fx.CSS', 'Fx.Tween', 'Fx.Morph', 'Fx.Transitions'],
			'Request'   : ['Request', 'Request.HTML', 'Request.JSON']
		},

		specs: {
			'Core'      : ['Core', 'Browser'],
			'Native'    : ['Array', 'String', 'Function', 'Number', 'Hash'],
			'Class'     : ['Class', 'Class.Extras'],
			'Element'   : ['Element', 'Element.Style'],
			'Utilities' : ['Selectors']
		}
	},

	initialize: function(root){
		if (root) this.root = root;
		this.includeType('source');
		return this;
	},

	getFolder: function(type, file){
		var scripts = this.scripts[type];
		for (var folder in scripts){
			for (var i = 0; i < scripts[folder].length; i++){
				var script = scripts[folder][i];
				if (script == file) return folder;
			}
		}
		return false;
	},

	getRequest: function(){
		var pairs = window.location.search.substring(1).split('&');
		var obj = {};
		for (var i = 0, l = pairs.length; i < l; i++){
			var pair = pairs[i].split('=');
			obj[pair[0]] = pair[1];
		}
		return obj;
	},

	includeFile: function(type, folder, file){
		folder = folder || this.getFolder(type, file);
		if (!folder) return false;
		this.included[type][folder] = this.included[type][folder] || [];
		var files = this.included[type][folder];
		for (var i = 0; i < files.length; i++){
			if (files[i] == file) return false;
		}
		this.included[type][folder].push(file);
		return document.writeln('<script type="text/javascript" src="' + this.root + this.paths[type] + '/' + folder + '/' + file + '.js?' + new Date().getTime() + '"></script>');
	},

	includeFolder: function(type, folder){
		var scripts = this.scripts[type][folder];
		for (var i = 0, l = scripts.length; i < l; i++) this.includeFile(type, folder, scripts[i]);
	},

	includeType: function(type){
		for (var folder in this.scripts[type]) this.includeFolder(type, folder);
	},

	includeRequest: function(type){
		var req = this.getRequest();
		if (!req.files && !req.folders) return false;
		var files = (req.files) ? req.files.split('+') : [];
		var folders = (req.folders) ? req.folders.split('+') : [];
		for (var j = 0; j < files.length; j++) this.includeFile(type, null, files[j]);
		for (var i = 0; i < folders.length; i++) this.includeFolder(type, folders[i]);
		return true;
	}

};