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
package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.BaseColor;

/**
 * Implements the pattern.
 */

public final class PdfPatternPainter extends PdfTemplate {
    
    float xstep, ystep;
    boolean stencil = false;
    BaseColor defaultColor;
    
    /**
     *Creates a <CODE>PdfPattern</CODE>.
     */
    
    private PdfPatternPainter() {
        super();
        type = TYPE_PATTERN;
    }
    
    /**
     * Creates new PdfPattern
     *
     * @param wr the <CODE>PdfWriter</CODE>
     */
    
    PdfPatternPainter(PdfWriter wr) {
        super(wr);
        type = TYPE_PATTERN;
    }
    
    PdfPatternPainter(PdfWriter wr, BaseColor defaultColor) {
        this(wr);
        stencil = true;
        if (defaultColor == null)
            this.defaultColor = BaseColor.GRAY;
        else
            this.defaultColor = defaultColor;
    }
    
    /**
     * Sets the horizontal interval of this pattern.
     *
     * @param xstep the xstep in horizontal painting
     */
    
    public void setXStep(float xstep) {
        this.xstep = xstep;
    }
    
    /**
     * Sets the vertical interval of this pattern.
     *
     * @param ystep in vertical painting
     */
    
    public void setYStep(float ystep) {
        this.ystep = ystep;
    }
    
    /**
     * Returns the horizontal interval when repeating the pattern.
     * @return a value
     */
    public float getXStep() {
        return this.xstep;
    }
    
    /**
     * Returns the vertical interval when repeating the pattern.
     * @return a value
     */
    public float getYStep() {
        return this.ystep;
    }
    
    /**
     * Tells you if this pattern is colored/uncolored (stencil = uncolored, you need to set a default color).
     * @return true if the pattern is an uncolored tiling pattern (stencil).
     */
    public boolean isStencil() {
        return stencil;
    }
    
    /**
     * Sets the transformation matrix for the pattern.
     * @param a
     * @param b
     * @param c
     * @param d
     * @param e
     * @param f
     */
    public void setPatternMatrix(float a, float b, float c, float d, float e, float f) {
        setMatrix(a, b, c, d, e, f);
    }
    /**
     * Gets the stream representing this pattern
     * @return the stream representing this pattern
     */
    PdfPattern getPattern() {
        return new PdfPattern(this);
    }
    
    /**
     * Gets the stream representing this pattern
     * @param	compressionLevel	the compression level of the stream
     * @return the stream representing this pattern
     * @since	2.1.3
     */
    PdfPattern getPattern(int compressionLevel) {
        return new PdfPattern(this, compressionLevel);
    }
    
    /**
     * Gets a duplicate of this <CODE>PdfPatternPainter</CODE>. All
     * the members are copied by reference but the buffer stays different.
     * @return a copy of this <CODE>PdfPatternPainter</CODE>
     */
    
    public PdfContentByte getDuplicate() {
        PdfPatternPainter tpl = new PdfPatternPainter();
        tpl.writer = writer;
        tpl.pdf = pdf;
        tpl.thisReference = thisReference;
        tpl.pageResources = pageResources;
        tpl.bBox = new Rectangle(bBox);
        tpl.xstep = xstep;
        tpl.ystep = ystep;
        tpl.matrix = matrix;
        tpl.stencil = stencil;
        tpl.defaultColor = defaultColor;
        return tpl;
    }
    
    /**
     * Returns the default color of the pattern.
     * @return a BaseColor
     */
    public BaseColor getDefaultColor() {
        return defaultColor;
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setGrayFill(float)
     */
    public void setGrayFill(float gray) {
        checkNoColor();
        super.setGrayFill(gray);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#resetGrayFill()
     */
    public void resetGrayFill() {
        checkNoColor();
        super.resetGrayFill();
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setGrayStroke(float)
     */
    public void setGrayStroke(float gray) {
        checkNoColor();
        super.setGrayStroke(gray);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#resetGrayStroke()
     */
    public void resetGrayStroke() {
        checkNoColor();
        super.resetGrayStroke();
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setRGBColorFillF(float, float, float)
     */
    public void setRGBColorFillF(float red, float green, float blue) {
        checkNoColor();
        super.setRGBColorFillF(red, green, blue);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#resetRGBColorFill()
     */
    public void resetRGBColorFill() {
        checkNoColor();
        super.resetRGBColorFill();
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setRGBColorStrokeF(float, float, float)
     */
    public void setRGBColorStrokeF(float red, float green, float blue) {
        checkNoColor();
        super.setRGBColorStrokeF(red, green, blue);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#resetRGBColorStroke()
     */
    public void resetRGBColorStroke() {
        checkNoColor();
        super.resetRGBColorStroke();
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setCMYKColorFillF(float, float, float, float)
     */
    public void setCMYKColorFillF(float cyan, float magenta, float yellow, float black) {
        checkNoColor();
        super.setCMYKColorFillF(cyan, magenta, yellow, black);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#resetCMYKColorFill()
     */
    public void resetCMYKColorFill() {
        checkNoColor();
        super.resetCMYKColorFill();
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setCMYKColorStrokeF(float, float, float, float)
     */
    public void setCMYKColorStrokeF(float cyan, float magenta, float yellow, float black) {
        checkNoColor();
        super.setCMYKColorStrokeF(cyan, magenta, yellow, black);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#resetCMYKColorStroke()
     */
    public void resetCMYKColorStroke() {
        checkNoColor();
        super.resetCMYKColorStroke();
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#addImage(com.itextpdf.text.Image, float, float, float, float, float, float)
     */
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        if (stencil && !image.isMask())
            checkNoColor();
        super.addImage(image, a, b, c, d, e, f);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setCMYKColorFill(int, int, int, int)
     */
    public void setCMYKColorFill(int cyan, int magenta, int yellow, int black) {
        checkNoColor();
        super.setCMYKColorFill(cyan, magenta, yellow, black);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setCMYKColorStroke(int, int, int, int)
     */
    public void setCMYKColorStroke(int cyan, int magenta, int yellow, int black) {
        checkNoColor();
        super.setCMYKColorStroke(cyan, magenta, yellow, black);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setRGBColorFill(int, int, int)
     */
    public void setRGBColorFill(int red, int green, int blue) {
        checkNoColor();
        super.setRGBColorFill(red, green, blue);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setRGBColorStroke(int, int, int)
     */
    public void setRGBColorStroke(int red, int green, int blue) {
        checkNoColor();
        super.setRGBColorStroke(red, green, blue);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setColorStroke(com.itextpdf.text.BaseColor)
     */
    public void setColorStroke(BaseColor color) {
        checkNoColor();
        super.setColorStroke(color);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setColorFill(com.itextpdf.text.BaseColor)
     */
    public void setColorFill(BaseColor color) {
        checkNoColor();
        super.setColorFill(color);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setColorFill(com.itextpdf.text.pdf.PdfSpotColor, float)
     */
    public void setColorFill(PdfSpotColor sp, float tint) {
        checkNoColor();
        super.setColorFill(sp, tint);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setColorStroke(com.itextpdf.text.pdf.PdfSpotColor, float)
     */
    public void setColorStroke(PdfSpotColor sp, float tint) {
        checkNoColor();
        super.setColorStroke(sp, tint);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setPatternFill(com.itextpdf.text.pdf.PdfPatternPainter)
     */
    public void setPatternFill(PdfPatternPainter p) {
        checkNoColor();
        super.setPatternFill(p);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setPatternFill(com.itextpdf.text.pdf.PdfPatternPainter, com.itextpdf.text.BaseColor, float)
     */
    public void setPatternFill(PdfPatternPainter p, BaseColor color, float tint) {
        checkNoColor();
        super.setPatternFill(p, color, tint);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setPatternStroke(com.itextpdf.text.pdf.PdfPatternPainter, com.itextpdf.text.BaseColor, float)
     */
    public void setPatternStroke(PdfPatternPainter p, BaseColor color, float tint) {
        checkNoColor();
        super.setPatternStroke(p, color, tint);
    }
    
    /**
     * @see com.itextpdf.text.pdf.PdfContentByte#setPatternStroke(com.itextpdf.text.pdf.PdfPatternPainter)
     */
    public void setPatternStroke(PdfPatternPainter p) {
        checkNoColor();
        super.setPatternStroke(p);
    }
    
    void checkNoColor() {
        if (stencil)
            throw new RuntimeException(MessageLocalization.getComposedMessage("colors.are.not.allowed.in.uncolored.tile.patterns"));
    }
}
