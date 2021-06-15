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
if(!dojo._hasResource["dojo.NodeList-fx"]){ //_hasResource checks added by build. Do not use _hasResource directly in your code.
dojo._hasResource["dojo.NodeList-fx"] = true;
dojo.provide("dojo.NodeList-fx");
dojo.require("dojo.fx");

dojo.extend(dojo.NodeList, {
	_anim: function(obj, method, args){
		var anims = [];
		args = args||{};
		this.forEach(function(item){
			var tmpArgs = { node: item };
			dojo.mixin(tmpArgs, args);
			anims.push(obj[method](tmpArgs));
		});
		return dojo.fx.combine(anims); // dojo._Animation
	},

	wipeIn: function(args){
		//	summary:
		//		wipe in all elements of this NodeList. Returns an instance of dojo._Animation
		//	example:
		//		Fade in all tables with class "blah":
		//		|	dojo.query("table.blah").wipeIn().play();
		return this._anim(dojo.fx, "wipeIn", args); // dojo._Animation
	},

	wipeOut: function(args){
		//	summary:
		//		wipe out all elements of this NodeList. Returns an instance of dojo._Animation
		//	example:
		//		Wipe out all tables with class "blah":
		//		|	dojo.query("table.blah").wipeOut().play();
		return this._anim(dojo.fx, "wipeOut", args); // dojo._Animation
	},

	slideTo: function(args){
		//	summary:
		//		slide all elements of the node list to the specified place.
		//		Returns an instance of dojo._Animation
		//	example:
		//		|	Move all tables with class "blah" to 300/300:
		//		|	dojo.query("table.blah").slideTo({
		//		|		left: 40,
		//		|		top: 50
		//		|	}).play();
		return this._anim(dojo.fx, "slideTo", args); // dojo._Animation
	},


	fadeIn: function(args){
		//	summary:
		//		fade in all elements of this NodeList. Returns an instance of dojo._Animation
		//	example:
		//		Fade in all tables with class "blah":
		//		|	dojo.query("table.blah").fadeIn().play();
		return this._anim(dojo, "fadeIn", args); // dojo._Animation
	},

	fadeOut: function(args){
		//	summary:
		//		fade out all elements of this NodeList. Returns an instance of dojo._Animation
		//	example:
		//		Fade out all elements with class "zork":
		//		|	dojo.query(".zork").fadeOut().play();
		//	example:
		//		Fade them on a delay and do something at the end:
		//		|	var fo = dojo.query(".zork").fadeOut();
		//		|	dojo.connect(fo, "onEnd", function(){ /*...*/ });
		//		|	fo.play();
		return this._anim(dojo, "fadeOut", args); // dojo._Animation
	},

	animateProperty: function(args){
		//	summary:
		//		see dojo.animateProperty(). Animate all elements of this
		//		NodeList across the properties specified.
		//	example:
		//	|	dojo.query(".zork").animateProperty({
		//	|		duration: 500,
		//	|		properties: { 
		//	|			color:		{ start: "black", end: "white" },
		//	|			left:		{ end: 300 } 
		//	|		} 
		//	|	}).play();
		return this._anim(dojo, "animateProperty", args); // dojo._Animation
	}
});

}
