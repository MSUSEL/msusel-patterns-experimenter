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

import java.net.URL;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * Raw Image data that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 *
 * @author  Paulo Soares
 */

public class ImgRaw extends Image {

    ImgRaw(Image image) {
        super(image);
    }

/** Creates an Image in raw mode.
 *
 * @param width the exact width of the image
 * @param height the exact height of the image
 * @param components 1,3 or 4 for GrayScale, RGB and CMYK
 * @param bpc bits per component. Must be 1,2,4 or 8
 * @param data the image data
 * @throws BadElementException on error
 */
    
    public ImgRaw(int width, int height, int components, int bpc, byte[] data) throws BadElementException{
        super((URL)null);
        type = IMGRAW;
        scaledHeight = height;
        setTop(scaledHeight);
        scaledWidth = width;
        setRight(scaledWidth);
        if (components != 1 && components != 3 && components != 4)
            throw new BadElementException(MessageLocalization.getComposedMessage("components.must.be.1.3.or.4"));
        if (bpc != 1 && bpc != 2 && bpc != 4 && bpc != 8)
            throw new BadElementException(MessageLocalization.getComposedMessage("bits.per.component.must.be.1.2.4.or.8"));
        colorspace = components;
        this.bpc = bpc;
        rawData = data;
        plainWidth = getWidth();
        plainHeight = getHeight();
    }
}
