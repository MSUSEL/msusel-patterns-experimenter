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
/**
 * This class is used to dispatch event to observers/listeners.
 * All internal events inside TinyMCE uses this class.
 *
 * @class tinymce.util.Dispatcher
 */
tinymce.create('tinymce.util.Dispatcher', {
	scope : null,
	listeners : null,

	/**
	 * Constructs a new event dispatcher object.
	 *
	 * @constructor
	 * @method Dispatcher
	 * @param {Object} s Optional default execution scope for all observer functions.
	 */
	Dispatcher : function(s) {
		this.scope = s || this;
		this.listeners = [];
	},

	/**
	 * Add an observer function to be executed when a dispatch call is done.
	 *
	 * @method add
	 * @param {function} cb Callback function to execute when a dispatch event occurs.
	 * @param {Object} s Optional execution scope, defaults to the one specified in the class constructor.
	 * @return {function} Returns the same function as the one passed on.
	 */
	add : function(cb, s) {
		this.listeners.push({cb : cb, scope : s || this.scope});

		return cb;
	},

	/**
	 * Add an observer function to be executed to the top of the list of observers.
	 *
	 * @method addToTop
	 * @param {function} cb Callback function to execute when a dispatch event occurs.
	 * @param {Object} s Optional execution scope, defaults to the one specified in the class constructor.
	 * @return {function} Returns the same function as the one passed on.
	 */
	addToTop : function(cb, s) {
		this.listeners.unshift({cb : cb, scope : s || this.scope});

		return cb;
	},

	/**
	 * Removes an observer function.
	 *
	 * @method remove
	 * @param {function} cb Observer function to remove.
	 * @return {function} The same function that got passed in or null if it wasn't found.
	 */
	remove : function(cb) {
		var l = this.listeners, o = null;

		tinymce.each(l, function(c, i) {
			if (cb == c.cb) {
				o = cb;
				l.splice(i, 1);
				return false;
			}
		});

		return o;
	},

	/**
	 * Dispatches an event to all observers/listeners.
	 *
	 * @method dispatch
	 * @param {Object} .. Any number of arguments to dispatch.
	 * @return {Object} Last observer functions return value.
	 */
	dispatch : function() {
		var s, a = arguments, i, li = this.listeners, c;

		// Needs to be a real loop since the listener count might change while looping
		// And this is also more efficient
		for (i = 0; i<li.length; i++) {
			c = li[i];
			s = c.cb.apply(c.scope, a);

			if (s === false)
				break;
		}

		return s;
	}

	/**#@-*/
});
