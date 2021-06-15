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

import com.itextpdf.text.pdf.codec.TIFFFaxDecoder;
import java.net.URL;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * CCITT Image data that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 *
 * @author  Paulo Soares
 */

public class ImgCCITT extends Image {

    ImgCCITT(Image image) {
        super(image);
    }

    /** Creates an Image with CCITT compression.
     *
     * @param width the exact width of the image
     * @param height the exact height of the image
     * @param reverseBits reverses the bits in <code>data</code>.
     *  Bit 0 is swapped with bit 7 and so on.
     * @param typeCCITT the type of compression in <code>data</code>. It can be
     * CCITTG4, CCITTG31D, CCITTG32D
     * @param parameters parameters associated with this stream. Possible values are
     * CCITT_BLACKIS1, CCITT_ENCODEDBYTEALIGN, CCITT_ENDOFLINE and CCITT_ENDOFBLOCK or a
     * combination of them
     * @param data the image data
     * @throws BadElementException on error
     */

    public ImgCCITT(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data) throws BadElementException{
        super((URL)null);
        if (typeCCITT != CCITTG4 && typeCCITT != CCITTG3_1D && typeCCITT != CCITTG3_2D)
            throw new BadElementException(MessageLocalization.getComposedMessage("the.ccitt.compression.type.must.be.ccittg4.ccittg3.1d.or.ccittg3.2d"));
        if (reverseBits)
            TIFFFaxDecoder.reverseBits(data);
        type = IMGRAW;
        scaledHeight = height;
        setTop(scaledHeight);
        scaledWidth = width;
        setRight(scaledWidth);
        colorspace = parameters;
        bpc = typeCCITT;
        rawData = data;
        plainWidth = getWidth();
        plainHeight = getHeight();
    }
}
