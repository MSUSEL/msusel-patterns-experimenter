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
	var each = tinymce.each;

	/**
	 * This class contains simple cookie manangement functions.
	 *
	 * @class tinymce.util.Cookie
	 * @static
	 */
	tinymce.create('static tinymce.util.Cookie', {
		/**
		 * Parses the specified query string into an name/value object.
		 *
		 * @method getHash
		 * @param {String} n String to parse into a n Hashtable object.
		 * @return {Object} Name/Value object with items parsed from querystring.
		 */
		getHash : function(n) {
			var v = this.get(n), h;

			if (v) {
				each(v.split('&'), function(v) {
					v = v.split('=');
					h = h || {};
					h[unescape(v[0])] = unescape(v[1]);
				});
			}

			return h;
		},

		/**
		 * Sets a hashtable name/value object to a cookie.
		 *
		 * @method setHash
		 * @param {String} n Name of the cookie.
		 * @param {Object} v Hashtable object to set as cookie.
		 * @param {Date} e Optional date object for the expiration of the cookie.
		 * @param {String} p Optional path to restrict the cookie to.
		 * @param {String} d Optional domain to restrict the cookie to.
		 * @param {String} s Is the cookie secure or not.
		 */
		setHash : function(n, v, e, p, d, s) {
			var o = '';

			each(v, function(v, k) {
				o += (!o ? '' : '&') + escape(k) + '=' + escape(v);
			});

			this.set(n, o, e, p, d, s);
		},

		/**
		 * Gets the raw data of a cookie by name.
		 *
		 * @method get
		 * @param {String} n Name of cookie to retrive.
		 * @return {String} Cookie data string.
		 */
		get : function(n) {
			var c = document.cookie, e, p = n + "=", b;

			// Strict mode
			if (!c)
				return;

			b = c.indexOf("; " + p);

			if (b == -1) {
				b = c.indexOf(p);

				if (b != 0)
					return null;
			} else
				b += 2;

			e = c.indexOf(";", b);

			if (e == -1)
				e = c.length;

			return unescape(c.substring(b + p.length, e));
		},

		/**
		 * Sets a raw cookie string.
		 *
		 * @method set
		 * @param {String} n Name of the cookie.
		 * @param {String} v Raw cookie data.
		 * @param {Date} e Optional date object for the expiration of the cookie.
		 * @param {String} p Optional path to restrict the cookie to.
		 * @param {String} d Optional domain to restrict the cookie to.
		 * @param {String} s Is the cookie secure or not.
		 */
		set : function(n, v, e, p, d, s) {
			document.cookie = n + "=" + escape(v) +
				((e) ? "; expires=" + e.toGMTString() : "") +
				((p) ? "; path=" + escape(p) : "") +
				((d) ? "; domain=" + d : "") +
				((s) ? "; secure" : "");
		},

		/**
		 * Removes/deletes a cookie by name.
		 *
		 * @method remove
		 * @param {String} n Cookie name to remove/delete.
		 * @param {Strong} p Optional path to remove the cookie from.
		 */
		remove : function(n, p) {
			var d = new Date();

			d.setTime(d.getTime() - 1000);

			this.set(n, '', d, p, d);
		}
	});
})();
