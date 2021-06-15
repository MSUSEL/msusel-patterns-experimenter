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
import com.itextpdf.text.BaseColor;

/**
 * These two methods are used by FactoryProperties (for HTMLWorker).
 * It's implemented by FontFactoryImp.
 * @since	iText 5.0
 */
public interface FontProvider {
	/**
	 * Checks if a certain font is registered.
	 *
	 * @param   fontname    the name of the font that has to be checked.
	 * @return  true if the font is found
	 */
	public boolean isRegistered(String fontname);

	/**
	 * Constructs a <CODE>Font</CODE>-object.
	 *
	 * @param	fontname    the name of the font
	 * @param	encoding    the encoding of the font
	 * @param       embedded    true if the font is to be embedded in the PDF
	 * @param	size	    the size of this font
	 * @param	style	    the style of this font
	 * @param	color	    the <CODE>Color</CODE> of this font.
	 * @return the Font constructed based on the parameters
	 */
	public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color);
}
