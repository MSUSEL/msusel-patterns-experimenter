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

import java.util.ArrayList;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Helper class implementing the DrawInterface. Can be used to add
 * horizontal or vertical separators. Won't draw anything unless
 * you implement the draw method.
 * @since	2.1.2
 */

public class VerticalPositionMark implements DrawInterface, Element {

    /** Another implementation of the DrawInterface; its draw method will overrule LineSeparator.draw(). */
    protected DrawInterface drawInterface = null;

    /** The offset for the line. */
    protected float offset = 0;

	/**
	 * Creates a vertical position mark that won't draw anything unless
	 * you define a DrawInterface.
	 */
	public VerticalPositionMark() {
	}

	/**
	 * Creates a vertical position mark that won't draw anything unless
	 * you define a DrawInterface.
	 * @param	drawInterface	the drawInterface for this vertical position mark.
	 * @param	offset			the offset for this vertical position mark.
	 */
	public VerticalPositionMark(DrawInterface drawInterface, float offset) {
		this.drawInterface = drawInterface;
		this.offset = offset;
	}

	/**
	 * @see com.itextpdf.text.pdf.draw.DrawInterface#draw(com.itextpdf.text.pdf.PdfContentByte, float, float, float, float, float)
	 */
	public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
		if (drawInterface != null) {
			drawInterface.draw(canvas, llx, lly, urx, ury, y + offset);
		}
	}

    /**
     * @see com.itextpdf.text.Element#process(com.itextpdf.text.ElementListener)
     */
    public boolean process(ElementListener listener) {
		try {
			return listener.add(this);
		} catch (DocumentException e) {
			return false;
		}
    }

    /**
     * @see com.itextpdf.text.Element#type()
     */
    public int type() {
        return Element.YMARK;
    }

    /**
     * @see com.itextpdf.text.Element#isContent()
     */
    public boolean isContent() {
        return true;
    }

    /**
     * @see com.itextpdf.text.Element#isNestable()
     */
    public boolean isNestable() {
        return false;
    }

    /**
     * @see com.itextpdf.text.Element#getChunks()
     */
    public ArrayList<Chunk> getChunks() {
    	ArrayList<Chunk> list = new ArrayList<Chunk>();
    	list.add(new Chunk(this, true));
        return list;
    }

    /**
     * Getter for the interface with the overruling draw() method.
     * @return	a DrawInterface implementation
     */
    public DrawInterface getDrawInterface() {
        return drawInterface;
    }

    /**
     * Setter for the interface with the overruling draw() method.
     * @param drawInterface a DrawInterface implementation
     */
    public void setDrawInterface(DrawInterface drawInterface) {
        this.drawInterface = drawInterface;
    }

    /**
     * Getter for the offset relative to the baseline of the current line.
     * @return	an offset
     */
    public float getOffset() {
        return offset;
    }

    /**
     * Setter for the offset. The offset is relative to the current
     * Y position. If you want to underline something, you have to
     * choose a negative offset.
     * @param offset	an offset
     */
    public void setOffset(float offset) {
        this.offset = offset;
    }
}
