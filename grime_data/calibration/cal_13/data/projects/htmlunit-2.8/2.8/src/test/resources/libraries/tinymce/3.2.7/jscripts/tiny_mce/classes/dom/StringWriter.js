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
(function(tinymce) {
	/**
	 * This class writes nodes into a string.
	 * @class tinymce.dom.StringWriter
	 */
	tinymce.create('tinymce.dom.StringWriter', {
		str : null,
		tags : null,
		count : 0,
		settings : null,
		indent : null,

		/**
		 * Constructs a new StringWriter.
		 *
		 * @constructor
		 * @method StringWriter
		 * @param {Object} s Optional settings object.
		 */
		StringWriter : function(s) {
			this.settings = tinymce.extend({
				indent_char : ' ',
				indentation : 0
			}, s);

			this.reset();
		},

		/**
		 * Resets the writer so it can be reused the contents of the writer is cleared.
		 *
		 * @method reset
		 */
		reset : function() {
			this.indent = '';
			this.str = "";
			this.tags = [];
			this.count = 0;
		},

		/**
		 * Writes the start of an element like for example: <tag.
		 *
		 * @method writeStartElement
		 * @param {String} n Name of element to write.
		 */
		writeStartElement : function(n) {
			this._writeAttributesEnd();
			this.writeRaw('<' + n);
			this.tags.push(n);
			this.inAttr = true;
			this.count++;
			this.elementCount = this.count;
		},

		/**
		 * Writes an attrubute like for example: myattr="valie"
		 *
		 * @method writeAttribute
		 * @param {String} n Attribute name to write.
		 * @param {String} v Attribute value to write.
		 */
		writeAttribute : function(n, v) {
			var t = this;

			t.writeRaw(" " + t.encode(n) + '="' + t.encode(v) + '"');
		},

		/**
		 * Write the end of a element. This will add a short end for elements with out children like for example a img element.
		 *
		 * @method writeEndElement
		 */
		writeEndElement : function() {
			var n;

			if (this.tags.length > 0) {
				n = this.tags.pop();

				if (this._writeAttributesEnd(1))
					this.writeRaw('</' + n + '>');

				if (this.settings.indentation > 0)
					this.writeRaw('\n');
			}
		},

		/**
		 * Writes the end of a element. This will add a full end to the element even if it didn't have any children.
		 *
		 * @method writeFullEndElement
		 */
		writeFullEndElement : function() {
			if (this.tags.length > 0) {
				this._writeAttributesEnd();
				this.writeRaw('</' + this.tags.pop() + '>');

				if (this.settings.indentation > 0)
					this.writeRaw('\n');
			}
		},

		/**
		 * Writes a text node value.
		 *
		 * @method writeText
		 * @param {String} v Value to append as a text node.
		 */
		writeText : function(v) {
			this._writeAttributesEnd();
			this.writeRaw(this.encode(v));
			this.count++;
		},

		/**
		 * Writes a CDATA section.
		 *
		 * @method writeCDATA
		 * @param {String} v Value to write in CDATA.
		 */
		writeCDATA : function(v) {
			this._writeAttributesEnd();
			this.writeRaw('<![CDATA[' + v + ']]>');
			this.count++;
		},

		/**
		 * Writes a comment.
		 *
		 * @method writeComment
		 * @param {String} v Value of the comment.
		 */
		writeComment : function(v) {
			this._writeAttributesEnd();
			this.writeRaw('<!-- ' + v + '-->');
			this.count++;
		},

		/**
		 * String writer specific function. Enables you to write raw contents to the string.
		 *
		 * @method writeRaw
		 * @param {String} v String with raw contents to write.
		 */
		writeRaw : function(v) {
			this.str += v;
		},

		/**
		 * String writer specific method. This encodes the raw entities of a string.
		 *
		 * @method encode
		 * @param {String} s String to encode.
		 * @return {String} String with entity encoding of the raw elements like <>&".
		 */
		encode : function(s) {
			return s.replace(/[<>&"]/g, function(v) {
				switch (v) {
					case '<':
						return '&lt;';

					case '>':
						return '&gt;';

					case '&':
						return '&amp;';

					case '"':
						return '&quot;';
				}

				return v;
			});
		},

		/**
		 * Returns a string representation of the elements/nodes written.
		 *
		 * @method getContent
		 * @return {String} String representation of the written elements/nodes.
		 */
		getContent : function() {
			return this.str;
		},

		_writeAttributesEnd : function(s) {
			if (!this.inAttr)
				return;

			this.inAttr = false;

			if (s && this.elementCount == this.count) {
				this.writeRaw(' />');
				return false;
			}

			this.writeRaw('>');

			return true;
		}
	});
})(tinymce);
