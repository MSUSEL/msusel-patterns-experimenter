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

/** The graphic state dictionary.
 *
 * @author Paulo Soares
 */
public class PdfGState extends PdfDictionary {
    /** A possible blend mode */
    public static final PdfName BM_NORMAL = new PdfName("Normal");
    /** A possible blend mode */
    public static final PdfName BM_COMPATIBLE = new PdfName("Compatible");
    /** A possible blend mode */
    public static final PdfName BM_MULTIPLY = new PdfName("Multiply");
    /** A possible blend mode */
    public static final PdfName BM_SCREEN = new PdfName("Screen");
    /** A possible blend mode */
    public static final PdfName BM_OVERLAY = new PdfName("Overlay");
    /** A possible blend mode */
    public static final PdfName BM_DARKEN = new PdfName("Darken");
    /** A possible blend mode */
    public static final PdfName BM_LIGHTEN = new PdfName("Lighten");
    /** A possible blend mode */
    public static final PdfName BM_COLORDODGE = new PdfName("ColorDodge");
    /** A possible blend mode */
    public static final PdfName BM_COLORBURN = new PdfName("ColorBurn");
    /** A possible blend mode */
    public static final PdfName BM_HARDLIGHT = new PdfName("HardLight");
    /** A possible blend mode */
    public static final PdfName BM_SOFTLIGHT = new PdfName("SoftLight");
    /** A possible blend mode */
    public static final PdfName BM_DIFFERENCE = new PdfName("Difference");
    /** A possible blend mode */
    public static final PdfName BM_EXCLUSION = new PdfName("Exclusion");
    
    /**
     * Sets the flag whether to apply overprint for stroking.
     * @param ov
     */
    public void setOverPrintStroking(boolean ov) {
        put(PdfName.OP, ov ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Sets the flag whether to apply overprint for non stroking painting operations.
     * @param ov
     */
    public void setOverPrintNonStroking(boolean ov) {
        put(PdfName.op, ov ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }

    /**
     * Sets the flag whether to toggle knockout behavior for overprinted objects.
     * @param ov - accepts 0 or 1
     */
    public void setOverPrintMode(int ov) {
        put(PdfName.OPM, new PdfNumber(ov==0 ? 0 : 1));
    }
    
    /**
     * Sets the current stroking alpha constant, specifying the constant shape or
     * constant opacity value to be used for stroking operations in the transparent
     * imaging model.
     * @param n
     */
    public void setStrokeOpacity(float n) {
        put(PdfName.CA, new PdfNumber(n));
    }
    
    /**
     * Sets the current stroking alpha constant, specifying the constant shape or
     * constant opacity value to be used for nonstroking operations in the transparent
     * imaging model.
     * @param n
     */
    public void setFillOpacity(float n) {
        put(PdfName.ca, new PdfNumber(n));
    }
    
    /**
     * The alpha source flag specifying whether the current soft mask
     * and alpha constant are to be interpreted as shape values (true)
     * or opacity values (false). 
     * @param v
     */
    public void setAlphaIsShape(boolean v) {
        put(PdfName.AIS, v ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }
    
    /**
     * Determines the behavior of overlapping glyphs within a text object
     * in the transparent imaging model.
     * @param v
     */
    public void setTextKnockout(boolean v) {
        put(PdfName.TK, v ? PdfBoolean.PDFTRUE : PdfBoolean.PDFFALSE);
    }
    
    /**
     * The current blend mode to be used in the transparent imaging model.
     * @param bm
     */
    public void setBlendMode(PdfName bm) {
        put(PdfName.BM, bm);
    }
    
    /**
     * Set the rendering intent, possible values are: PdfName.ABSOLUTECOLORIMETRIC,
     * PdfName.RELATIVECOLORIMETRIC, PdfName.SATURATION, PdfName.PERCEPTUAL.
     * @param ri
     * @since 5.0.2
     */
    public void setRenderingIntent(PdfName ri) {
    	put(PdfName.RI, ri);
    }
}
