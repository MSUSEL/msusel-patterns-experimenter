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
package com.itextpdf.text.pdf.draw;

import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Element that draws a dotted line from left to right.
 * Can be added directly to a document or column.
 * Can also be used to create a separator chunk.
 * @since	2.1.2 
 */
public class DottedLineSeparator extends LineSeparator {

	/** the gap between the dots. */
	protected float gap = 5;
	
	/**
	 * @see com.itextpdf.text.pdf.draw.DrawInterface#draw(com.itextpdf.text.pdf.PdfContentByte, float, float, float, float, float)
	 */
	public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
		canvas.saveState();
		canvas.setLineWidth(lineWidth);
		canvas.setLineCap(PdfContentByte.LINE_CAP_ROUND);
		canvas.setLineDash(0, gap, gap / 2);
        drawLine(canvas, llx, urx, y);
		canvas.restoreState();
	}

	/**
	 * Getter for the gap between the center of the dots of the dotted line.
	 * @return	the gap between the center of the dots
	 */
	public float getGap() {
		return gap;
	}

	/**
	 * Setter for the gap between the center of the dots of the dotted line.
	 * @param	gap	the gap between the center of the dots
	 */
	public void setGap(float gap) {
		this.gap = gap;
	}

}