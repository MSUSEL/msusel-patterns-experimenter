/**
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
package com.itextpdf.text;

/**
 * Interface implemented by Element objects that can potentially consume
 * a lot of memory. Objects implementing the LargeElement interface can
 * be added to a Document more than once. If you have invoked setComplete(false),
 * they will be added partially and the content that was added will be
 * removed until you've invoked setComplete(true);
 * @since	iText 2.0.8
 */

public interface LargeElement extends Element {
	
	/**
	 * If you invoke setComplete(false), you indicate that the content
	 * of the object isn't complete yet; it can be added to the document
	 * partially, but more will follow. If you invoke setComplete(true),
	 * you indicate that you won't add any more data to the object.
	 * @since	iText 2.0.8
	 * @param	complete	false if you'll be adding more data after
	 * 						adding the object to the document.
	 */
	public void setComplete(boolean complete);
	
	/**
	 * Indicates if the element is complete or not.
	 * @since	iText 2.0.8
	 * @return	indicates if the element is complete according to the user.
	 */
	public boolean isComplete();
	
	/**
	 * Flushes the content that has been added.
	 */
	public void flushContent();
}
