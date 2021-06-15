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

/**
 * a Literal
 */

public class PdfLiteral extends PdfObject {
    
    /**
     * Holds value of property position.
     */
    private int position;
        
    public PdfLiteral(String text) {
        super(0, text);
    }
    
    public PdfLiteral(byte b[]) {
        super(0, b);
    }

    public PdfLiteral(int size) {
        super(0, (byte[])null);
        bytes = new byte[size];
        java.util.Arrays.fill(bytes, (byte)32);
    }

    public PdfLiteral(int type, String text) {
        super(type, text);
    }
    
    public PdfLiteral(int type, byte b[]) {
        super(type, b);
    }
    
    public void toPdf(PdfWriter writer, java.io.OutputStream os) throws java.io.IOException {
        if (os instanceof OutputStreamCounter)
            position = ((OutputStreamCounter)os).getCounter();
        super.toPdf(writer, os);
    }
    
    /**
     * Getter for property position.
     * @return Value of property position.
     */
    public int getPosition() {
        return this.position;
    }
    
    /**
     * Getter for property posLength.
     * @return Value of property posLength.
     */
    public int getPosLength() {
        if (bytes != null)
            return bytes.length;
        else
            return 0;
    }
    
}