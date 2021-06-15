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
package com.itextpdf.text.pdf.parser;

import java.awt.geom.Rectangle2D;

/**
 * Allows you to find the rectangle that contains all the text in a page.
 * @since 5.0.2
 */
public class TextMarginFinder implements RenderListener {
    private Rectangle2D.Float textRectangle = null;
    
	/**
	 * Method invokes by the PdfContentStreamProcessor.
	 * Passes a TextRenderInfo for every text chunk that is encountered.
	 * We'll use this object to obtain coordinates.
	 * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(com.itextpdf.text.pdf.parser.TextRenderInfo)
	 */
	public void renderText(TextRenderInfo renderInfo) {
		if (textRectangle == null)
		    textRectangle = renderInfo.getDescentLine().getBoundingRectange();
		else
		    textRectangle.add(renderInfo.getDescentLine().getBoundingRectange());
		
		textRectangle.add(renderInfo.getAscentLine().getBoundingRectange());

	}

	/**
	 * Getter for the left margin.
	 * @return the X position of the left margin
	 */
	public float getLlx() {
	    return textRectangle.x;
	}

	/**
	 * Getter for the bottom margin.
	 * @return the Y position of the bottom margin
	 */
	public float getLly() {
        return textRectangle.y;
	}

	/**
	 * Getter for the right margin.
	 * @return the X position of the right margin
	 */
	public float getUrx() {
		return textRectangle.x + textRectangle.width;
	}

	/**
	 * Getter for the top margin.
	 * @return the Y position of the top margin
	 */
	public float getUry() {
		return textRectangle.y + textRectangle.height;
	}

	/**
	 * Gets the width of the text block.
	 * @return a width
	 */
	public float getWidth() {
		return textRectangle.width;
	}
	
	/**
	 * Gets the height of the text block.
	 * @return a height
	 */
	public float getHeight() {
		return textRectangle.height;
	}
	
	/**
	 * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
	 */
	public void beginTextBlock() {
        // do nothing
	}

	/**
	 * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
	 */
	public void endTextBlock() {
        // do nothing
	}

	/**
	 * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
	 */
	public void renderImage(ImageRenderInfo renderInfo) {
	    // do nothing
	}
}
