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

public class PRIndirectReference extends PdfIndirectReference {
    
    protected PdfReader reader;
    // membervariables
    
    // constructors
    
/**
 * Constructs a <CODE>PdfIndirectReference</CODE>.
 *
 * @param		reader			a <CODE>PdfReader</CODE>
 * @param		number			the object number.
 * @param		generation		the generation number.
 */
    
    PRIndirectReference(PdfReader reader, int number, int generation) {
        type = INDIRECT;
        this.number = number;
        this.generation = generation;
        this.reader = reader;
    }
    
/**
 * Constructs a <CODE>PdfIndirectReference</CODE>.
 *
 * @param		reader			a <CODE>PdfReader</CODE>
 * @param		number			the object number.
 */
    
    PRIndirectReference(PdfReader reader, int number) {
        this(reader, number, 0);
    }
    
    // methods
    
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        int n = writer.getNewObjectNumber(reader, number, generation);
        os.write(PdfEncodings.convertToBytes(new StringBuffer().append(n).append(" 0 R").toString(), null));
    }

    public PdfReader getReader() {
        return reader;
    }
    
    public void setNumber(int number, int generation) {
        this.number = number;
        this.generation = generation;
    }
}