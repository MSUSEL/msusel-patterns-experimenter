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
import java.io.IOException;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;

/** Represents an imported page.
 *
 * @author Paulo Soares
 */
public class PdfImportedPage extends com.itextpdf.text.pdf.PdfTemplate {

    PdfReaderInstance readerInstance;
    int pageNumber;
    
    PdfImportedPage(PdfReaderInstance readerInstance, PdfWriter writer, int pageNumber) {
        this.readerInstance = readerInstance;
        this.pageNumber = pageNumber;
        this.writer = writer;
        bBox = readerInstance.getReader().getPageSize(pageNumber);
        setMatrix(1, 0, 0, 1, -bBox.getLeft(), -bBox.getBottom());
        type = TYPE_IMPORTED;
    }

    /** Reads the content from this <CODE>PdfImportedPage</CODE>-object from a reader.
     *
     * @return self
     *
     */
    public PdfImportedPage getFromReader() {
      return this;
    }

    public int getPageNumber() {
        return pageNumber;
    }


    /** Always throws an error. This operation is not allowed.
     * @param image dummy
     * @param a dummy
     * @param b dummy
     * @param c dummy
     * @param d dummy
     * @param e dummy
     * @param f dummy
     * @throws DocumentException  dummy */    
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        throwError();
    }
    
    /** Always throws an error. This operation is not allowed.
     * @param template dummy
     * @param a dummy
     * @param b dummy
     * @param c dummy
     * @param d dummy
     * @param e dummy
     * @param f  dummy */    
    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        throwError();
    }
    
    /** Always throws an error. This operation is not allowed.
     * @return  dummy */    
    public PdfContentByte getDuplicate() {
        throwError();
        return null;
    }

    /**
     * Gets the stream representing this page.
     *
     * @param	compressionLevel	the compressionLevel
     * @return the stream representing this page
     * @since	2.1.3	(replacing the method without param compressionLevel)
     */
    PdfStream getFormXObject(int compressionLevel) throws IOException {
         return readerInstance.getFormXObject(pageNumber, compressionLevel);
    }
    
    public void setColorFill(PdfSpotColor sp, float tint) {
        throwError();
    }
    
    public void setColorStroke(PdfSpotColor sp, float tint) {
        throwError();
    }
    
    PdfObject getResources() {
        return readerInstance.getResources(pageNumber);
    }
    
    /** Always throws an error. This operation is not allowed.
     * @param bf dummy
     * @param size dummy */    
    public void setFontAndSize(BaseFont bf, float size) {
        throwError();
    }
    
    /**
     * Always throws an error. This operation is not allowed.
     * @param group New value of property group.
     * @since	2.1.6
     */ 
    public void setGroup(PdfTransparencyGroup group) {
        throwError();
	}

	void throwError() {
        throw new RuntimeException(MessageLocalization.getComposedMessage("content.can.not.be.added.to.a.pdfimportedpage"));
    }
    
    PdfReaderInstance getPdfReaderInstance() {
        return readerInstance;
    }
}
