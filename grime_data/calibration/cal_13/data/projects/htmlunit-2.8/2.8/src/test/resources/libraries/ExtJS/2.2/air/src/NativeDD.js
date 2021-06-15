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
 * @class Ext.air.DragType
 * 
 * Drag drop type constants
 * 
 * @singleton
 */
Ext.air.DragType = {
	/**
	 * Constant for text data
	 */
	TEXT : 'text/plain',
	/**
	 * Constant for html data
	 */
	HTML : 'text/html',
	/**
	 * Constant for url data
	 */
	URL : 'text/uri-list',
	/**
	 * Constant for bitmap data
	 */
	BITMAP : 'image/x-vnd.adobe.air.bitmap',
	/**
	 * Constant for file list data
	 */
	FILES : 'application/x-vnd.adobe.air.file-list'
};


// workaround for DD dataTransfer Clipboard not having hasFormat

Ext.apply(Ext.EventObjectImpl.prototype, {
	hasFormat : function(format){
		if (this.browserEvent.dataTransfer) {
			for (var i = 0, len = this.browserEvent.dataTransfer.types.length; i < len; i++) {
				if(this.browserEvent.dataTransfer.types[i] == format) {
					return true;
				}
			}
		}
		return false;
	},
	
	getData : function(type){
		return this.browserEvent.dataTransfer.getData(type);
	}
});


