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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.codec.wmf.InputMeta;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;

/**
 * An <CODE>ImgWMF</CODE> is the representation of a windows metafile
 * that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 */

public class ImgWMF extends Image {
    
    // Constructors
    
    ImgWMF(Image image) {
        super(image);
    }
    
    /**
     * Constructs an <CODE>ImgWMF</CODE>-object, using an <VAR>url</VAR>.
     *
     * @param url the <CODE>URL</CODE> where the image can be found
     * @throws BadElementException on error
     * @throws IOException on error
     */
    
    public ImgWMF(URL url) throws BadElementException, IOException {
        super(url);
        processParameters();
    }
    
    /**
     * Constructs an <CODE>ImgWMF</CODE>-object, using a <VAR>filename</VAR>.
     *
     * @param filename a <CODE>String</CODE>-representation of the file that contains the image.
     * @throws BadElementException on error
     * @throws MalformedURLException on error
     * @throws IOException on error
     */
    
    public ImgWMF(String filename) throws BadElementException, MalformedURLException, IOException {
        this(Utilities.toURL(filename));
    }
    
    /**
     * Constructs an <CODE>ImgWMF</CODE>-object from memory.
     *
     * @param img the memory image
     * @throws BadElementException on error
     * @throws IOException on error
     */
    
    public ImgWMF(byte[] img) throws BadElementException, IOException {
        super((URL)null);
        rawData = img;
        originalData = img;
        processParameters();
    }
    
/**
 * This method checks if the image is a valid WMF and processes some parameters.
 * @throws BadElementException
 * @throws IOException
 */
    
    private void processParameters() throws BadElementException, IOException {
        type = IMGTEMPLATE;
        originalType = ORIGINAL_WMF;
        InputStream is = null;
        try {
            String errorID;
            if (rawData == null){
                is = url.openStream();
                errorID = url.toString();
            }
            else{
                is = new java.io.ByteArrayInputStream(rawData);
                errorID = "Byte array";
            }
            InputMeta in = new InputMeta(is);
            if (in.readInt() != 0x9AC6CDD7)	{
                throw new BadElementException(MessageLocalization.getComposedMessage("1.is.not.a.valid.placeable.windows.metafile", errorID));
            }
            in.readWord();
            int left = in.readShort();
            int top = in.readShort();
            int right = in.readShort();
            int bottom = in.readShort();
            int inch = in.readWord();
            dpiX = 72;
            dpiY = 72;
            scaledHeight = (float)(bottom - top) / inch * 72f;
            setTop(scaledHeight);
            scaledWidth = (float)(right - left) / inch * 72f;
            setRight(scaledWidth);
        }
        finally {
            if (is != null) {
                is.close();
            }
            plainWidth = getWidth();
            plainHeight = getHeight();
        }
    }
    
    /** Reads the WMF into a template.
     * @param template the template to read to
     * @throws IOException on error
     * @throws DocumentException on error
     */    
    public void readWMF(PdfTemplate template) throws IOException, DocumentException {
        setTemplateData(template);
        template.setWidth(getWidth());
        template.setHeight(getHeight());
        InputStream is = null;
        try {
            if (rawData == null){
                is = url.openStream();
            }
            else{
                is = new java.io.ByteArrayInputStream(rawData);
            }
            MetaDo meta = new MetaDo(is, template);
            meta.readAll();
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
