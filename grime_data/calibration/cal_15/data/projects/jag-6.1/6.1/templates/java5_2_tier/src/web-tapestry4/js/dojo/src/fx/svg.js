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

dojo.provide("dojo.fx.svg");

dojo.require("dojo.svg");
dojo.require("dojo.animation.*");
dojo.require("dojo.event.*");

dojo.fx.svg.fadeOut = function(node, duration, callback){
	return dojo.fx.svg.fade(node, duration, dojo.svg.getOpacity(node), 0, callback);
};
dojo.fx.svg.fadeIn = function(node, duration, callback){
	return dojo.fx.svg.fade(node, duration, dojo.svg.getOpacity(node), 1, callback);
};
dojo.fx.svg.fadeHide = function(node, duration, callback){
	if(!duration) { duration = 150; } // why not have a default?
	return dojo.fx.svg.fadeOut(node, duration, function(node) {
		if(typeof callback == "function") { callback(node); }
	});
};
dojo.fx.svg.fadeShow = function(node, duration, callback){
	if(!duration) { duration = 150; } // why not have a default?
	return dojo.fx.svg.fade(node, duration, 0, 1, callback);
};
dojo.fx.svg.fade = function(node, duration, startOpac, endOpac, callback){
	var anim = new dojo.animation.Animation(
		new dojo.math.curves.Line([startOpac],[endOpac]),
		duration,
		0
	);
	dojo.event.connect(anim, "onAnimate", function(e){
		dojo.svg.setOpacity(node, e.x);
	});
	if (callback) {
		dojo.event.connect(anim, "onEnd", function(e){
			callback(node, anim);
		});
	};
	anim.play(true);
	return anim;
};

/////////////////////////////////////////////////////////////////////////////////////////
//	TODO
/////////////////////////////////////////////////////////////////////////////////////////

//	SLIDES
dojo.fx.svg.slideTo = function(node, endCoords, duration, callback) { };
dojo.fx.svg.slideBy = function(node, coords, duration, callback) { };
dojo.fx.svg.slide = function(node, startCoords, endCoords, duration, callback) { 
	var anim = new dojo.animation.Animation(
		new dojo.math.curves.Line([startCoords],[endCoords]),
		duration,
		0
	);
	dojo.event.connect(anim, "onAnimate", function(e){
		dojo.svg.setCoords(node, {x: e.x, y: e.y });
	});
	if (callback) {
		dojo.event.connect(anim, "onEnd", function(e){
			callback(node, anim);
		});
	};
	anim.play(true);
	return anim;
};

//	COLORS
dojo.fx.svg.colorFadeIn = function(node, startRGB, duration, delay, callback) { };
dojo.fx.svg.highlight = dojo.fx.svg.colorFadeIn;
dojo.fx.svg.colorFadeFrom = dojo.fx.svg.colorFadeIn;

dojo.fx.svg.colorFadeOut = function(node, endRGB, duration, delay, callback) { };
dojo.fx.svg.unhighlight = dojo.fx.svg.colorFadeOut;
dojo.fx.svg.colorFadeTo = dojo.fx.svg.colorFadeOut;

dojo.fx.svg.colorFade = function(node, startRGB, endRGB, duration, callback, dontPlay) { };

//	WIPES
dojo.fx.svg.wipeIn = function(node, duration, callback, dontPlay) { };
dojo.fx.svg.wipeInToHeight = function(node, duration, height, callback, dontPlay) { }
dojo.fx.svg.wipeOut = function(node, duration, callback, dontPlay) { };

//	Explode and Implode
dojo.fx.svg.explode = function(startNode, endNode, duration, callback) { };
dojo.fx.svg.explodeFromBox = function(startCoords, endNode, duration, callback) { };
dojo.fx.svg.implode = function(startNode, endNode, duration, callback) { };
dojo.fx.svg.implodeToBox = function(startNode, endCoords, duration, callback) { };
dojo.fx.svg.Exploder = function(triggerNode, boxNode) { };

//	html mixes in, we want SVG to remain separate
