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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;

/**
 * <CODE>PdfContents</CODE> is a <CODE>PdfStream</CODE> containing the contents (text + graphics) of a <CODE>PdfPage</CODE>.
 */

class PdfContents extends PdfStream {
    
    static final byte SAVESTATE[] = DocWriter.getISOBytes("q\n");
    static final byte RESTORESTATE[] = DocWriter.getISOBytes("Q\n");
    static final byte ROTATE90[] = DocWriter.getISOBytes("0 1 -1 0 ");
    static final byte ROTATE180[] = DocWriter.getISOBytes("-1 0 0 -1 ");
    static final byte ROTATE270[] = DocWriter.getISOBytes("0 -1 1 0 ");
    static final byte ROTATEFINAL[] = DocWriter.getISOBytes(" cm\n");
    // constructor
    
/**
 * Constructs a <CODE>PdfContents</CODE>-object, containing text and general graphics.
 *
 * @param under the direct content that is under all others
 * @param content the graphics in a page
 * @param text the text in a page
 * @param secondContent the direct content that is over all others
 * @throws BadPdfFormatException on error
 */
    
    PdfContents(PdfContentByte under, PdfContentByte content, PdfContentByte text, PdfContentByte secondContent, Rectangle page) throws BadPdfFormatException {
        super();
        try {
            OutputStream out = null;
            Deflater deflater = null;
            streamBytes = new ByteArrayOutputStream();
            if (Document.compress)
            {
                compressed = true;
                compressionLevel = text.getPdfWriter().getCompressionLevel();
                deflater = new Deflater(compressionLevel);
                out = new DeflaterOutputStream(streamBytes, deflater);
            }
            else
                out = streamBytes;
            int rotation = page.getRotation();
            switch (rotation) {
                case 90:
                    out.write(ROTATE90);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getTop())));
                    out.write(' ');
                    out.write('0');
                    out.write(ROTATEFINAL);
                    break;
                case 180:
                    out.write(ROTATE180);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getRight())));
                    out.write(' ');
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getTop())));
                    out.write(ROTATEFINAL);
                    break;
                case 270:
                    out.write(ROTATE270);
                    out.write('0');
                    out.write(' ');
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getRight())));
                    out.write(ROTATEFINAL);
                    break;
            }
            if (under.size() > 0) {
                out.write(SAVESTATE);
                under.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (content.size() > 0) {
                out.write(SAVESTATE);
                content.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (text != null) {
                out.write(SAVESTATE);
                text.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (secondContent.size() > 0) {
                secondContent.getInternalBuffer().writeTo(out);
            }
            out.close();
            if (deflater != null) {
                deflater.end();
            }
        }
        catch (Exception e) {
            throw new BadPdfFormatException(e.getMessage());
        }
        put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
        if (compressed)
            put(PdfName.FILTER, PdfName.FLATEDECODE);
    }
}