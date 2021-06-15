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

dojo.provide("dojo.animation.AnimationSequence");
dojo.require("dojo.animation.AnimationEvent");
dojo.require("dojo.animation.Animation");

dojo.animation.AnimationSequence = function(repeatCount){
	this._anims = [];
	this.repeatCount = repeatCount || 0;
}

dojo.lang.extend(dojo.animation.AnimationSequence, {
	repeateCount: 0,

	_anims: [],
	_currAnim: -1,

	onBegin: null,
	onEnd: null,
	onNext: null,
	handler: null,

	add: function() {
		for(var i = 0; i < arguments.length; i++) {
			this._anims.push(arguments[i]);
			arguments[i]._animSequence = this;
		}
	},

	remove: function(anim) {
		for(var i = 0; i < this._anims.length; i++) {
			if( this._anims[i] == anim ) {
				this._anims[i]._animSequence = null;
				this._anims.splice(i, 1);
				break;
			}
		}
	},

	removeAll: function() {
		for(var i = 0; i < this._anims.length; i++) {
			this._anims[i]._animSequence = null;
		}
		this._anims = [];
		this._currAnim = -1;
	},

	clear: function() {
		this.removeAll();
	},

	play: function(gotoStart) {
		if( this._anims.length == 0 ) { return; }
		if( gotoStart || !this._anims[this._currAnim] ) {
			this._currAnim = 0;
		}
		if( this._anims[this._currAnim] ) {
			if( this._currAnim == 0 ) {
				var e = {type: "begin", animation: this._anims[this._currAnim]};
				if(typeof this.handler == "function") { this.handler(e); }
				if(typeof this.onBegin == "function") { this.onBegin(e); }
			}
			this._anims[this._currAnim].play(gotoStart);
		}
	},

	pause: function() {
		if( this._anims[this._currAnim] ) {
			this._anims[this._currAnim].pause();
		}
	},

	playPause: function() {
		if( this._anims.length == 0 ) { return; }
		if( this._currAnim == -1 ) { this._currAnim = 0; }
		if( this._anims[this._currAnim] ) {
			this._anims[this._currAnim].playPause();
		}
	},

	stop: function() {
		if( this._anims[this._currAnim] ) {
			this._anims[this._currAnim].stop();
		}
	},

	status: function() {
		if( this._anims[this._currAnim] ) {
			return this._anims[this._currAnim].status();
		} else {
			return "stopped";
		}
	},

	_setCurrent: function(anim) {
		for(var i = 0; i < this._anims.length; i++) {
			if( this._anims[i] == anim ) {
				this._currAnim = i;
				break;
			}
		}
	},

	_playNext: function() {
		if( this._currAnim == -1 || this._anims.length == 0 ) { return; }
		this._currAnim++;
		if( this._anims[this._currAnim] ) {
			var e = {type: "next", animation: this._anims[this._currAnim]};
			if(typeof this.handler == "function") { this.handler(e); }
			if(typeof this.onNext == "function") { this.onNext(e); }
			this._anims[this._currAnim].play(true);
		} else {
			var e = {type: "end", animation: this._anims[this._anims.length-1]};
			if(typeof this.handler == "function") { this.handler(e); }
			if(typeof this.onEnd == "function") { this.onEnd(e); }
			if(this.repeatCount > 0) {
				this._currAnim = 0;
				this.repeatCount--;
				this._anims[this._currAnim].play(true);
			} else if(this.repeatCount == -1) {
				this._currAnim = 0;
				this._anims[this._currAnim].play(true);
			} else {
				this._currAnim = -1;
			}
		}
	}
});
