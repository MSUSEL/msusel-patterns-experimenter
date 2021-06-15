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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ListIterator;

import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author kevin
 * @since 5.0.1
 */
public class ContentByteUtils {
    private ContentByteUtils() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Gets the content bytes from a content object, which may be a reference
     * a stream or an array.
     * @param contentObject the object to read bytes from
     * @return the content bytes
     * @throws IOException
     */
    public static byte[] getContentBytesFromContentObject(final PdfObject contentObject) throws IOException {
        final byte[] result;
        switch (contentObject.type())
        {
            case PdfObject.INDIRECT:
                final PRIndirectReference ref = (PRIndirectReference) contentObject;
                final PdfObject directObject = PdfReader.getPdfObject(ref);
                result = getContentBytesFromContentObject(directObject);
                break;
            case PdfObject.STREAM:
                final PRStream stream = (PRStream) PdfReader.getPdfObject(contentObject);
                result = PdfReader.getStreamBytes(stream);
                break;
            case PdfObject.ARRAY:
                // Stitch together all content before calling processContent(), because
                // processContent() resets state.
                final ByteArrayOutputStream allBytes = new ByteArrayOutputStream();
                final PdfArray contentArray = (PdfArray) contentObject;
                final ListIterator<PdfObject> iter = contentArray.listIterator();
                while (iter.hasNext())
                {
                    final PdfObject element = iter.next();
                    allBytes.write(getContentBytesFromContentObject(element));
                }
                result = allBytes.toByteArray();
                break;
            default:
                final String msg = "Unable to handle Content of type " + contentObject.getClass();
            throw new IllegalStateException(msg);
        }
        return result;
    }
    
    /**
     * Gets the content bytes of a page from a reader
     * @param reader  the reader to get content bytes from
     * @param pageNum   the page number of page you want get the content stream from
     * @return  a byte array with the effective content stream of a page
     * @throws IOException
     * @since 5.0.1
     */
    public static byte[] getContentBytesForPage(PdfReader reader, int pageNum) throws IOException {
        final PdfDictionary pageDictionary = reader.getPageN(pageNum);
        final PdfObject contentObject = pageDictionary.get(PdfName.CONTENTS);
        if (contentObject == null)
            return new byte[0];
        
        final byte[] contentBytes = ContentByteUtils.getContentBytesFromContentObject(contentObject);
        return contentBytes;
    }

}
