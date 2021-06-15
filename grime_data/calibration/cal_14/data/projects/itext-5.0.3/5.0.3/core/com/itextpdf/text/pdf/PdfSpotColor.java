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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * A <CODE>PdfSpotColor</CODE> defines a ColorSpace
 *
 * @see		PdfDictionary
 */

public class PdfSpotColor{
    
/**	The color name */
    public PdfName name;
    
/** The alternative color space */
    public BaseColor altcs;
    // constructors
    
    /**
     * Constructs a new <CODE>PdfSpotColor</CODE>.
     *
     * @param		name		a String value
     * @param		altcs		an alternative colorspace value
     */
    
    public PdfSpotColor(String name, BaseColor altcs) {
        this.name = new PdfName(name);
        this.altcs = altcs;
    }
    
    /**
     * Gets the alternative ColorSpace.
     * @return a BaseColor
     */
    public BaseColor getAlternativeCS() {
        return altcs;
    }
    
    protected PdfObject getSpotObject(PdfWriter writer) {
        PdfArray array = new PdfArray(PdfName.SEPARATION);
        array.add(name);
        PdfFunction func = null;
        if (altcs instanceof ExtendedColor) {
            int type = ((ExtendedColor)altcs).type;
            switch (type) {
                case ExtendedColor.TYPE_GRAY:
                    array.add(PdfName.DEVICEGRAY);
                    func = PdfFunction.type2(writer, new float[]{0, 1}, null, new float[]{0}, new float[]{((GrayColor)altcs).getGray()}, 1);
                    break;
                case ExtendedColor.TYPE_CMYK:
                    array.add(PdfName.DEVICECMYK);
                    CMYKColor cmyk = (CMYKColor)altcs;
                    func = PdfFunction.type2(writer, new float[]{0, 1}, null, new float[]{0, 0, 0, 0},
                        new float[]{cmyk.getCyan(), cmyk.getMagenta(), cmyk.getYellow(), cmyk.getBlack()}, 1);
                    break;
                default:
                    throw new RuntimeException(MessageLocalization.getComposedMessage("only.rgb.gray.and.cmyk.are.supported.as.alternative.color.spaces"));
            }
        }
        else {
            array.add(PdfName.DEVICERGB);
            func = PdfFunction.type2(writer, new float[]{0, 1}, null, new float[]{1, 1, 1},
                new float[]{(float)altcs.getRed() / 255, (float)altcs.getGreen() / 255, (float)altcs.getBlue() / 255}, 1);
        }
        array.add(func.getReference());
        return array;
    }
}
