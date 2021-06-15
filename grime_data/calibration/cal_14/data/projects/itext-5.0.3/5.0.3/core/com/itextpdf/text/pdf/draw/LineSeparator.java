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

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfContentByte;

import com.itextpdf.text.BaseColor;

/**
 * Element that draws a solid line from left to right.
 * Can be added directly to a document or column.
 * Can also be used to create a separator chunk.
 * @author	Paulo Soares
 * @since	2.1.2
 */
public class LineSeparator extends VerticalPositionMark {
	
    /** The thickness of the line. */
    protected float lineWidth = 1;
    /** The width of the line as a percentage of the available page width. */
    protected float percentage = 100;
    /** The color of the line. */
    protected BaseColor lineColor;
    /** The alignment of the line. */
    protected int alignment = Element.ALIGN_CENTER;
    
    /**
     * Creates a new instance of the LineSeparator class.
     * @param lineWidth		the thickness of the line
     * @param percentage	the width of the line as a percentage of the available page width
     * @param lineColor			the color of the line
     * @param align			the alignment
     * @param offset		the offset of the line relative to the current baseline (negative = under the baseline)
     */
    public LineSeparator(float lineWidth, float percentage, BaseColor lineColor, int align, float offset) {
        this.lineWidth = lineWidth;
        this.percentage = percentage;
        this.lineColor = lineColor;
        this.alignment = align;
        this.offset = offset;
    }

    /**
     * Creates a new instance of the LineSeparator class with
     * default values: lineWidth 1 user unit, width 100%, centered with offset 0.
     */
    public LineSeparator() {
    }

    /**
     * @see com.itextpdf.text.pdf.draw.DrawInterface#draw(com.itextpdf.text.pdf.PdfContentByte, float, float, float, float, float)
     */
    public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
        canvas.saveState();
        drawLine(canvas, llx, urx, y);
        canvas.restoreState();
    }

    /**
     * Draws a horizontal line.
     * @param canvas	the canvas to draw on
     * @param leftX		the left x coordinate
     * @param rightX	the right x coordindate
     * @param y			the y coordinate
     */
    public void drawLine(PdfContentByte canvas, float leftX, float rightX, float y) {
    	float w;
        if (getPercentage() < 0)
            w = -getPercentage();
        else
            w = (rightX - leftX) * getPercentage() / 100.0f;
        float s;
        switch (getAlignment()) {
            case Element.ALIGN_LEFT:
                s = 0;
                break;
            case Element.ALIGN_RIGHT:
                s = rightX - leftX - w;
                break;
            default:
                s = (rightX - leftX - w) / 2;
                break;
        }
        canvas.setLineWidth(getLineWidth());
        if (getLineColor() != null)
            canvas.setColorStroke(getLineColor());
        canvas.moveTo(s + leftX, y + offset);
        canvas.lineTo(s + w + leftX, y + offset);
        canvas.stroke();
    }
    
    /**
     * Getter for the line width.
     * @return	the thickness of the line that will be drawn.
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Setter for the line width.
     * @param lineWidth	the thickness of the line that will be drawn.
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Setter for the width as a percentage of the available width.
     * @return	a width percentage
     */
    public float getPercentage() {
        return percentage;
    }

    /**
     * Setter for the width as a percentage of the available width.
     * @param percentage	a width percentage
     */
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    /**
     * Getter for the color of the line that will be drawn.
     * @return	a color
     */
    public BaseColor getLineColor() {
        return lineColor;
    }

    /**
     * Setter for the color of the line that will be drawn.
     * @param color	a color
     */
    public void setLineColor(BaseColor color) {
        this.lineColor = color;
    }

    /**
     * Getter for the alignment of the line.
     * @return	an alignment value
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * Setter for the alignment of the line.
     * @param align	an alignment value
     */
    public void setAlignment(int align) {
        this.alignment = align;
    }
}