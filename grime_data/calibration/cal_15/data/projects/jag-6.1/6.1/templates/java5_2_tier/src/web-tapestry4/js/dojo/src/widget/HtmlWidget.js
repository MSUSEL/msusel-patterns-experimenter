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

dojo.provide("dojo.widget.HtmlWidget");
dojo.require("dojo.widget.DomWidget");
dojo.require("dojo.html");
dojo.require("dojo.lang.extras");
dojo.require("dojo.lang.func");

dojo.widget.HtmlWidget = function(args){
	// mixin inheritance
	dojo.widget.DomWidget.call(this);
}

dojo.inherits(dojo.widget.HtmlWidget, dojo.widget.DomWidget);

dojo.lang.extend(dojo.widget.HtmlWidget, {
	widgetType: "HtmlWidget",

	templateCssPath: null,
	templatePath: null,
	allowResizeX: true,
	allowResizeY: true,

	resizeGhost: null,
	initialResizeCoords: null,

	// for displaying/hiding widget
	toggle: "plain",
	toggleDuration: 150,

	animationInProgress: false,

	initialize: function(args, frag){
	},

	postMixInProperties: function(args, frag){
		// now that we know the setting for toggle, get toggle object
		this.toggleObj =
			dojo.widget.HtmlWidget.toggle[this.toggle.toLowerCase()] ||
			dojo.widget.HtmlWidget.toggle.plain;
	},

	getContainerHeight: function(){
		// NOTE: container height must be returned as the INNER height
		dj_unimplemented("dojo.widget.HtmlWidget.getContainerHeight");
	},

	getContainerWidth: function(){
		return this.parent.domNode.offsetWidth;
	},

	setNativeHeight: function(height){
		var ch = this.getContainerHeight();
	},

	resizeSoon: function(){
		if(this.isVisible()){
			dojo.lang.setTimeout(this, this.onResized, 0);
		}
	},

	createNodesFromText: function(txt, wrap){
		return dojo.html.createNodesFromText(txt, wrap);
	},

	_old_buildFromTemplate: dojo.widget.DomWidget.prototype.buildFromTemplate,

	buildFromTemplate: function(args, frag){
		if(dojo.widget.DomWidget.templates[this.widgetType]){
			var ot = dojo.widget.DomWidget.templates[this.widgetType];
			dojo.widget.DomWidget.templates[this.widgetType] = {};
		}
		if(args["templatecsspath"]){
			args["templateCssPath"] = args["templatecsspath"];
		}
		if(args["templatepath"]){
			args["templatePath"] = args["templatepath"];
		}
		dojo.widget.buildFromTemplate(this, args["templatePath"], args["templateCssPath"]);
		this._old_buildFromTemplate(args, frag);
		dojo.widget.DomWidget.templates[this.widgetType] = ot;
	},

	destroyRendering: function(finalize){
		try{
			var tempNode = this.domNode.parentNode.removeChild(this.domNode);
			if(!finalize){
				dojo.event.browser.clean(tempNode);
			}
			delete tempNode;
		}catch(e){ /* squelch! */ }
	},

	// Displaying/hiding the widget

	isVisible: function(){
		return dojo.style.isVisible(this.domNode);
	},

	doToggle: function(){
		this.isVisible() ? this.hide() : this.show();
	},

	show: function(){
		this.animationInProgress=true;
		this.toggleObj.show(this.domNode, this.toggleDuration, this.explodeSrc,
			dojo.lang.hitch(this, this.onShow));
	},

	onShow: function(){
		this.animationInProgress=false;
	},

	hide: function(){
		this.animationInProgress=true;
		this.toggleObj.hide(this.domNode, this.toggleDuration, this.explodeSrc,
			dojo.lang.hitch(this, this.onHide));
	},

	onHide: function(){
		this.animationInProgress=false;
	}
});


/**** 
	Strategies for displaying/hiding widget
*****/

dojo.widget.HtmlWidget.toggle={}

dojo.widget.HtmlWidget.toggle.plain = {
	show: function(node, duration, explodeSrc, callback){
		dojo.style.show(node);
		if(dojo.lang.isFunction(callback)){ callback(); }
	},

	hide: function(node, duration, explodeSrc, callback){
		dojo.html.hide(node);
		if(dojo.lang.isFunction(callback)){ callback(); }
	}
}

dojo.widget.HtmlWidget.toggle.fade = {
	show: function(node, duration, explodeSrc, callback){
		dojo.fx.html.fadeShow(node, duration, callback);
	},

	hide: function(node, duration, explodeSrc, callback){
		dojo.fx.html.fadeHide(node, duration, callback);
	}
}

dojo.widget.HtmlWidget.toggle.wipe = {
	show: function(node, duration, explodeSrc, callback){
		dojo.fx.html.wipeIn(node, duration, callback);
	},

	hide: function(node, duration, explodeSrc, callback){
		dojo.fx.html.wipeOut(node, duration, callback);
	}
}

dojo.widget.HtmlWidget.toggle.explode = {
	show: function(node, duration, explodeSrc, callback){
		dojo.fx.html.explode(explodeSrc||[0,0,0,0], node, duration, callback);
	},

	hide: function(node, duration, explodeSrc, callback){
		dojo.fx.html.implode(node, explodeSrc||[0,0,0,0], duration, callback);
	}
}
