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

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfReader;

/**
 * Represents image data from a PDF
 * @since 5.0.1
 */
public class ImageRenderInfo {
    /** The coordinate transformation matrix that was in effect when the image was rendered */
    private final Matrix ctm;
    /** A reference to the image XObject */
    private final PdfIndirectReference ref;
    
    private ImageRenderInfo(Matrix ctm, PdfIndirectReference ref) {
        this.ctm = ctm;
        this.ref = ref;
    }
    
    /**
     * Create an ImageRenderInfo object based on an XObject (this is the most common way of including an image in PDF)
     * @param ctm the coordinate transformation matrix at the time the image is rendered
     * @param ref a reference to the image XObject
     * @return the ImageRenderInfo representing the rendered XObject
     * @since 5.0.1
     */
    public static ImageRenderInfo createForXObject(Matrix ctm, PdfIndirectReference ref){
        return new ImageRenderInfo(ctm, ref);
    }
    
    /**
     * Create an ImageRenderInfo object based on embedded image data.  This is nowhere near completely thought through
     * and really just acts as a placeholder.
     * @param ctm the coordinate transformation matrix at the time the image is rendered
     * @param imageDictionary a dictionary containing parameters of the embedded image (note that the key/value pairs of this dictionary can have abbreviations in them)
     * @param streamBytes the bytes of the image data
     * @return the ImageRenderInfo representing the rendered embedded image
     * @since 5.0.1
     */
    protected static ImageRenderInfo createdForEmbeddedImage(Matrix ctm, PdfDictionary imageDictionary, byte[] streamBytes){
        return new ImageRenderInfo(ctm, null);
    }
    
    /**
     * Gets an object containing the image dictionary and bytes.
     * @return an object containing the image dictionary and byte[]
     * @since 5.0.2
     */
    public PdfImageObject getImage() {
		PRStream stream = (PRStream)PdfReader.getPdfObject(ref);
		return new PdfImageObject(stream);
    }
    
    /**
     * @return a vector in User space representing the start point of the xobject
     */
    public Vector getStartPoint(){ 
        return new Vector(0, 0, 1).cross(ctm); 
    }

    /**
     * @return the size of the image, in User space units
     * @since 5.0.3
     */
    public float getArea(){
        // the image space area is 1, so we multiply that by the determinant of the CTM to get the transformed area
        return ctm.getDeterminant();
    }
    
    /**
     * @return an indirect reference to the image
     * @since 5.0.2
     */
    public PdfIndirectReference getRef() {
    	return ref;
    }
}
