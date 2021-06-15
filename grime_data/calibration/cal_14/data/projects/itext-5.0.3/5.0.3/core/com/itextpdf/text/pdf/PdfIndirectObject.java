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
import java.io.OutputStream;

import com.itextpdf.text.DocWriter;

/**
 * <CODE>PdfIndirectObject</CODE> is the Pdf indirect object.
 * <P>
 * An <I>indirect object</I> is an object that has been labeled so that it can be referenced by
 * other objects. Any type of <CODE>PdfObject</CODE> may be labeled as an indirect object.<BR>
 * An indirect object consists of an object identifier, a direct object, and the <B>endobj</B>
 * keyword. The <I>object identifier</I> consists of an integer <I>object number</I>, an integer
 * <I>generation number</I>, and the <B>obj</B> keyword.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.7'
 * section 3.2.9 (page 63-65).
 *
 * @see		PdfObject
 * @see		PdfIndirectReference
 */

public class PdfIndirectObject {
    
    // membervariables
    
/** The object number */
    protected int number;
    
/** the generation number */
    protected int generation = 0;
    
    static final byte STARTOBJ[] = DocWriter.getISOBytes(" obj\n");
    static final byte ENDOBJ[] = DocWriter.getISOBytes("\nendobj\n");
    static final int SIZEOBJ = STARTOBJ.length + ENDOBJ.length;
    PdfObject object;
    PdfWriter writer;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfIndirectObject</CODE>.
 *
 * @param		number			the object number
 * @param		object			the direct object
 */
    
    PdfIndirectObject(int number, PdfObject object, PdfWriter writer) {
        this(number, 0, object, writer);
    }
    
    PdfIndirectObject(PdfIndirectReference ref, PdfObject object, PdfWriter writer) {
        this(ref.getNumber(),ref.getGeneration(),object,writer);
    }
/**
 * Constructs a <CODE>PdfIndirectObject</CODE>.
 *
 * @param		number			the object number
 * @param		generation		the generation number
 * @param		object			the direct object
 */
    
    PdfIndirectObject(int number, int generation, PdfObject object, PdfWriter writer) {
        this.writer = writer;
        this.number = number;
        this.generation = generation;
        this.object = object;
        PdfEncryption crypto = null;
        if (writer != null)
            crypto = writer.getEncryption();
        if (crypto != null) {
            crypto.setHashKey(number, generation);
        }
    }
    
    // methods
    
/**
 * Return the length of this <CODE>PdfIndirectObject</CODE>.
 *
 * @return		the length of the PDF-representation of this indirect object.
 */
    
//    public int length() {
//        if (isStream)
//            return bytes.size() + SIZEOBJ + stream.getStreamLength(writer);
//        else
//            return bytes.size();
//    }
    
    
/**
 * Returns a <CODE>PdfIndirectReference</CODE> to this <CODE>PdfIndirectObject</CODE>.
 *
 * @return		a <CODE>PdfIndirectReference</CODE>
 */
    
    public PdfIndirectReference getIndirectReference() {
        return new PdfIndirectReference(object.type(), number, generation);
    }
    
/**
 * Writes efficiently to a stream
 *
 * @param os the stream to write to
 * @throws IOException on write error
 */
    void writeTo(OutputStream os) throws IOException
    {
        os.write(DocWriter.getISOBytes(String.valueOf(number)));
        os.write(' ');
        os.write(DocWriter.getISOBytes(String.valueOf(generation)));
        os.write(STARTOBJ);
        object.toPdf(writer, os);
        os.write(ENDOBJ);
    }
}
